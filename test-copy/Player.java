import greenfoot.*;
import java.util.List;

public class Player extends Actor {
    
    private static int health = 100;
    private static int speed = 4;
    private static boolean hasSword = false;
    
   
    private boolean isAttacking = false;
    private boolean isMoving = false;
    private boolean facingRight = true;

  
    private GreenfootImage[] walkingFrames = new GreenfootImage[8]; // 8 walking frames
    private GreenfootImage[] idleFrames = new GreenfootImage[6];    // 6 idle frames
    private GreenfootImage[] attackFrames = new GreenfootImage[6];  // 6 attack frames
    private int frameIndex = 0;                                     // Current animation the player is at
    private int animationDelay = 5;                                // Delay between frame updates
    private int animationCounter = 0;                              // Counter to control frame rate

    public Player() {
        // loads walking animations and sets the size of the player 
        for (int i = 0; i < walkingFrames.length; i++) {
            walkingFrames[i] = new GreenfootImage("Assets/Character/frame_" + (i + 1) + ".png");
            walkingFrames[i].scale(42, 42); // 42x42 pixles 
        }

        // loads idle animations and sets the size 
        for (int i = 0; i < idleFrames.length; i++) {
            idleFrames[i] = new GreenfootImage("Assets/Idle/idle_frame_" + (i + 1) + ".png");
            idleFrames[i].scale(42, 42); // 42x42 pixels
        }

        // loads the attacking animations and sets the size 
        for (int i = 0; i < attackFrames.length; i++) {
            attackFrames[i] = new GreenfootImage("Assets/Character/character_attack_frame_" + (i + 1) + ".png");
            attackFrames[i].scale(42, 42);
        }

        // sets the first image to the first frame 
        setImage(idleFrames[0]);
    }

    public static void setHasSword(boolean value) {
        hasSword = value;
    }

    public void act() {
        // checks for if the player has used sword attack
        if (Greenfoot.isKeyDown("space") && !isAttacking && hasSword) {  // Only allows it if the player has actually bought the sword 
            isAttacking = true;
            frameIndex = 0;
            
            // checks to see if their is a orc near by when attacking 
            List<Orc> nearbyOrcs = getObjectsInRange(50, Orc.class);
            for (Orc orc : nearbyOrcs) {
                if (isFacing(orc)) {
                    orc.takeDamage(25);
                }
            }
        }

        // handles attack and does the animation for it 
        if (isAttacking) {
            animateAttack(); // Plays attack animation
        } else {
            move();
            animate();
        }

        checkCollisions();
    }

    public void takeDamage(int amount) {
        health -= amount;
        System.out.println("Player HP: " + health);
        if (health <= 0) {
            die();
        }
    }

    private void move() {
        isMoving = false; // assumes there is no movement unless a key is pressed 

        if (Greenfoot.isKeyDown("right")) {
            setLocation(getX() + speed, getY()); // moves the player right 
            isMoving = true;
        
            if (!facingRight) {
                flipImages(); // flips image to face right 
                facingRight = true;
            }
        }

        if (Greenfoot.isKeyDown("left")) {
            setLocation(getX() - speed, getY()); // moves the player left 
            isMoving = true;
        
            if (facingRight) {
                flipImages(); // Flips image  to face left
                facingRight = false;
            }
        }
        
        if (Greenfoot.isKeyDown("up")) {
            setLocation(getX(), getY() - speed); // moves the player up 
            isMoving = true;
        }
        
        if (Greenfoot.isKeyDown("down")) {
            setLocation(getX(), getY() + speed); // moves teh play down 
            isMoving = true;
        }
    }

    private void flipImages() {
        for (GreenfootImage image : walkingFrames) {
            image.mirrorHorizontally();
        }

        for (GreenfootImage image : idleFrames) {
            image.mirrorHorizontally();
        }

        for (GreenfootImage image : attackFrames) {
            image.mirrorHorizontally();
        }
    }

    private void animate() {
        animationCounter++;
        if (animationCounter >= animationDelay) {
            animationCounter = 0;
            if (isMoving) {
                frameIndex = (frameIndex + 1) % walkingFrames.length;
                setImage(walkingFrames[frameIndex]);
            } else {
                frameIndex = (frameIndex + 1) % idleFrames.length;
                setImage(idleFrames[frameIndex]);
            }
        }
    }

    private void animateAttack() {
        animationCounter++;
        if (animationCounter >= animationDelay) {
            animationCounter = 0;
            if (frameIndex < attackFrames.length) {
                setImage(attackFrames[frameIndex]);
                frameIndex++;
            } else {
                isAttacking = false; // ends players attack animation
                frameIndex = 0; // resets the players animation frame 
            }
        }
    }
    
    private void die() {
        if (getWorld() instanceof MyWorld) {
            ((MyWorld) getWorld()).showGameOver();
        } else if (getWorld() instanceof ForestWorld) {
            ((ForestWorld) getWorld()).showGameOver();
        } else if (getWorld() instanceof FortWorld) {
            ((FortWorld) getWorld()).showGameOver();
        } else if (getWorld() instanceof GrassWorld) {
            ((GrassWorld) getWorld()).showGameOver();
        }
        Greenfoot.stop();
    }

    private void checkCollisions() {
        Actor gem = getOneIntersectingObject(Gem.class);
        if (gem != null) {
            getWorld().removeObject(gem);
            Gem.playSound();
            
            // make sure the score is increased for the worlds 
            if (getWorld() instanceof MyWorld) {
                ((MyWorld) getWorld()).increaseScore();
            } else if (getWorld() instanceof ForestWorld) {
                ((ForestWorld) getWorld()).increaseScore();
            } else if (getWorld() instanceof FortWorld) {
                ((FortWorld) getWorld()).increaseScore();
            } else if (getWorld() instanceof GrassWorld) {
                ((GrassWorld) getWorld()).increaseScore();
            }
        }

        Actor enemy = getOneIntersectingObject(Enemy.class);
        if (enemy != null) {
            health -= 10;
            if (health <= 0) {
                if (getWorld() instanceof MyWorld) {
                    ((MyWorld) getWorld()).showGameOver();
                }
                Greenfoot.stop();
            }
        }
    }

    public static void setHealth(int newHealth) {
        health = newHealth;
    }

    public static int getHealth() {
        return health;
    }

    public static boolean hasSword() {
        return hasSword;
    }

    public static void upgradeHealth(int amount) {
        health += amount;
    }

    public static void upgradeSpeed(int increment) {
        speed += increment;
    }

    @Override
    public void addedToWorld(World world) {
        setImage(getImage());
    }

    private boolean isFacing(Actor other) {
        int dx = other.getX() - getX();
        return (facingRight && dx > 0) || (!facingRight && dx < 0);
    }

    public boolean isAttacking() {
        return isAttacking;
    }

    public static void resetStats() {
        health = 100;
        speed = 4;
        hasSword = false;
    }
}
