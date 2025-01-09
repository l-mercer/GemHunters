import greenfoot.*;
import java.util.List;

public class Player extends Actor {
    // Core attributes
    private static int health = 100;
    private static int speed = 4;
    private static boolean hasSword = false;
    
    // Animation states
    private boolean isAttacking = false;
    private boolean isMoving = false;
    private boolean facingRight = true;

    // Animation-related variables
    private GreenfootImage[] walkingFrames = new GreenfootImage[8]; // 8 walking frames
    private GreenfootImage[] idleFrames = new GreenfootImage[6];    // 6 idle frames
    private GreenfootImage[] attackFrames = new GreenfootImage[6];  // 6 attack frames
    private int frameIndex = 0;                                     // Current animation frame index
    private int animationDelay = 5;                                // Delay between frame updates
    private int animationCounter = 0;                              // Counter to control frame rate

    public Player() {
        // Load and scale walking frames
        for (int i = 0; i < walkingFrames.length; i++) {
            walkingFrames[i] = new GreenfootImage("Assets/Character/frame_" + (i + 1) + ".png");
            walkingFrames[i].scale(42, 42); // Scale to 42x42 pixels
        }

        // Load and scale idle frames
        for (int i = 0; i < idleFrames.length; i++) {
            idleFrames[i] = new GreenfootImage("Assets/Idle/idle_frame_" + (i + 1) + ".png");
            idleFrames[i].scale(42, 42); // Scale to 42x42 pixels
        }

        // Load and scale attack frames
        for (int i = 0; i < attackFrames.length; i++) {
            attackFrames[i] = new GreenfootImage("Assets/Character/character_attack_frame_" + (i + 1) + ".png");
            attackFrames[i].scale(42, 42);
        }

        // Set the initial image to the first idle frame
        setImage(idleFrames[0]);
    }

    public static void setHasSword(boolean value) {
        hasSword = value;
    }

    public void act() {
        // Check for sword attack
        if (Greenfoot.isKeyDown("space") && !isAttacking && hasSword) {  // Only allow attack if hasSword is true
            isAttacking = true;
            frameIndex = 0;
            
            // Check for nearby orcs when attacking
            List<Orc> nearbyOrcs = getObjectsInRange(50, Orc.class);
            for (Orc orc : nearbyOrcs) {
                if (isFacing(orc)) {
                    orc.takeDamage(25);
                }
            }
        }

        // Handle attack or regular actions
        if (isAttacking) {
            animateAttack(); // Play attack animation
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
        isMoving = false; // Assume no movement until keys are pressed

        if (Greenfoot.isKeyDown("right")) {
            setLocation(getX() + speed, getY()); // Adjust x-coordinate, keep y-coordinate
            isMoving = true;
        
            if (!facingRight) {
                flipImages(); // Flip to face right
                facingRight = true;
            }
        }

        if (Greenfoot.isKeyDown("left")) {
            setLocation(getX() - speed, getY()); // Adjust x-coordinate, keep y-coordinate
            isMoving = true;
        
            if (facingRight) {
                flipImages(); // Flip to face left
                facingRight = false;
            }
        }
        
        if (Greenfoot.isKeyDown("up")) {
            setLocation(getX(), getY() - speed); // Keep x-coordinate, adjust y-coordinate
            isMoving = true;
        }
        
        if (Greenfoot.isKeyDown("down")) {
            setLocation(getX(), getY() + speed); // Keep x-coordinate, adjust y-coordinate
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
                isAttacking = false; // End the attack animation
                frameIndex = 0; // Reset animation frame index
            }
        }
    }
    
    private void die() {
        System.out.println("Player has died!");
        Greenfoot.stop(); // Stop the game (or add a game-over screen)
    }

    private void checkCollisions() {
        Actor gem = getOneIntersectingObject(Gem.class);
        if (gem != null) {
            getWorld().removeObject(gem);
            Gem.playSound();
            // Check which world we're in and call the appropriate method
            if (getWorld() instanceof MyWorld) {
                ((MyWorld) getWorld()).increaseScore();
            } else if (getWorld() instanceof ForestWorld) {
                ((ForestWorld) getWorld()).increaseScore();
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
