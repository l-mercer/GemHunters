import greenfoot.*; // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.ArrayList;

public class Orc extends Actor {
    private ArrayList<GreenfootImage> walkFrames = new ArrayList<>();
    private ArrayList<GreenfootImage> attackFrames = new ArrayList<>();
    private int currentFrame = 0;
    private String currentAction = "walk";
    private int animationCounter = 0;
    private int animationSpeed = 5;
    private int damage = 10;
    private int health = 100;
    private int attackCooldown = 0;
    private int moveSpeed = 2;
    private int turnCounter = 0;
    private boolean facingRight = true;
    private String currentDirection = "right"; // Can be "up", "down", "left", "right"
    private int minMoveTime = 120;  // Minimum time to move in one direction (2 seconds)
    private int maxMoveTime = 240;  // Maximum time to move in one direction (4 seconds)
    private int currentMoveTime;    // How long to move in current direction
    private int detectionRange = 200;  // How far the orc can "see" the player
    private int followSpeed = 1;      // Speed when following player
    private int wanderSpeed = 1;      // Speed when wandering
    private int moveCounter = 0;      // Counter for slowing down movement
    private int moveDelay = 2;        // Default delay for normal speed

    public Orc(boolean isFaster) {
        // Load walk frames (no flipping - keep original orientation)
        for (int i = 1; i <= 8; i++) {
            GreenfootImage img = new GreenfootImage("Orc/Orc_Walk/orc_walk_frame_" + i + ".png");
            img.scale(52, 42);
            walkFrames.add(img);
        }
        
        // Load attack frames (no flipping - keep original orientation)
        for (int i = 1; i <= 6; i++) {
            GreenfootImage img = new GreenfootImage("Orc/Orc_Attack/orc_attack_frame_" + i + ".png");
            img.scale(52, 52);
            attackFrames.add(img);
        }
        
        setImage(walkFrames.get(0));
        facingRight = true;  // Changed: Start facing right (original sprite orientation)
        currentDirection = "right"; // Changed: Start moving right
        currentMoveTime = minMoveTime + Greenfoot.getRandomNumber(maxMoveTime - minMoveTime);
        
        if (isFaster) {
            moveDelay = 1;  // Move twice as fast
        }
    }

    // Add default constructor
    public Orc() {
        this(false);  // Normal speed by default
    }

    public void act() {
        if (attackCooldown > 0) {
            attackCooldown--;
        }

        if (currentAction.equals("walk")) {
            randomMovement();
        }
        
        animate();
        checkForPlayer();
        checkSwordHit();
    }

    public void startWalking() {
        currentAction = "walk";
        // Instead of random rotation, pick a random direction
        int direction = Greenfoot.getRandomNumber(4);
        switch(direction) {
            case 0: // Right
                currentDirection = "right";
                if (!facingRight) {  // CHANGED: Flip to face right when moving right
                    flipSprites();
                    facingRight = true;
                }
                break;
            case 1: // Down
                currentDirection = "down";
                break;
            case 2: // Left
                currentDirection = "left";
                if (facingRight) {  // CHANGED: Flip to face left when moving left
                    flipSprites();
                    facingRight = false;
                }
                break;
            case 3: // Up
                currentDirection = "up";
                break;
        }
    }

    private void randomMovement() {
        Player player = getNearestPlayer();
        
        // If player is within detection range, follow them
        if (player != null && getDistance(player) < detectionRange) {
            followPlayer(player);
        } else {
            wander();  // Normal random movement when no player is near
        }
        
        // Keep the edge checking
        checkWorldBounds();
    }

    private Player getNearestPlayer() {
        return (Player) getObjectsInRange(detectionRange, Player.class).stream()
            .findFirst()
            .orElse(null);
    }

    private void followPlayer(Player player) {
        // Calculate direction to player
        int dx = player.getX() - getX();
        int dy = player.getY() - getY();
        
        // Update facing direction based on player position
        if (dx > 0 && !facingRight) {
            flipSprites();
            facingRight = true;
        } else if (dx < 0 && facingRight) {
            flipSprites();
            facingRight = false;
        }

        moveCounter++;
        if (moveCounter >= moveDelay) {
            moveCounter = 0;
            // Move towards player
            if (Math.abs(dx) > followSpeed) {
                setLocation(getX() + (dx > 0 ? followSpeed : -followSpeed), getY());
            }
            if (Math.abs(dy) > followSpeed) {
                setLocation(getX(), getY() + (dy > 0 ? followSpeed : -followSpeed));
            }
        }
    }

    private void wander() {
        turnCounter++;
        
        // Only change direction after moving for the random time period
        if (turnCounter >= currentMoveTime) {
            turnCounter = 0;
            currentMoveTime = minMoveTime + Greenfoot.getRandomNumber(maxMoveTime - minMoveTime);
            
            // 70% chance to keep moving in same direction
            if (Greenfoot.getRandomNumber(100) > 70) {
                changeDirection();
            }
        }
        
        // Move in current direction at wander speed
        move();
    }

    private void move() {
        moveCounter++;
        if (moveCounter >= moveDelay) {
            moveCounter = 0;
            switch(currentDirection) {
                case "right":
                    setLocation(getX() + wanderSpeed, getY());
                    break;
                case "left":
                    setLocation(getX() - wanderSpeed, getY());
                    break;
                case "up":
                    setLocation(getX(), getY() - wanderSpeed);
                    break;
                case "down":
                    setLocation(getX(), getY() + wanderSpeed);
                    break;
            }
        }
    }

    private void changeDirection() {
        int direction = Greenfoot.getRandomNumber(4);
        switch(direction) {
            case 0: // Right
                currentDirection = "right";
                if (!facingRight) {
                    flipSprites();
                    facingRight = true;
                }
                break;
            case 1: // Down
                currentDirection = "down";
                break;
            case 2: // Left
                currentDirection = "left";
                if (facingRight) {
                    flipSprites();
                    facingRight = false;
                }
                break;
            case 3: // Up
                currentDirection = "up";
                break;
        }
    }

    private void checkWorldBounds() {
        World world = getWorld();
        if (world != null) {
            int margin = 80;
            
            if (getX() <= margin) {
                currentDirection = "right";
                if (!facingRight) {
                    flipSprites();
                    facingRight = true;
                }
                turnCounter = 0;
            } else if (getX() >= world.getWidth() - margin) {
                currentDirection = "left";
                if (facingRight) {
                    flipSprites();
                    facingRight = false;
                }
                turnCounter = 0;
            }
            if (getY() <= margin) {
                currentDirection = "down";
                turnCounter = 0;
            } else if (getY() >= world.getHeight() - margin) {
                currentDirection = "up";
                turnCounter = 0;
            }
        }
    }

    private double getDistance(Actor other) {
        int dx = other.getX() - getX();
        int dy = other.getY() - getY();
        return Math.sqrt(dx * dx + dy * dy);
    }

    private void checkForPlayer() {
        if (attackCooldown <= 0) {
            Player player = (Player) getOneObjectAtOffset(0, 0, Player.class);
            if (player != null && currentAction.equals("walk")) {
                startAttack();
            }
        }
    }

    private void startAttack() {
        currentAction = "attack";
        currentFrame = 0;
    }

    private void animate() {
        animationCounter++;
        if (animationCounter >= animationSpeed) {
            animationCounter = 0;
            
            ArrayList<GreenfootImage> currentFrames = 
                currentAction.equals("attack") ? attackFrames : walkFrames;
            
            currentFrame++;
            if (currentFrame >= currentFrames.size()) {
                currentFrame = 0;
                
                // Handle end of attack animation
                if (currentAction.equals("attack")) {
                    Player player = (Player) getOneObjectAtOffset(0, 0, Player.class);
                    if (player != null) {
                        player.takeDamage(damage);
                    }
                    attackCooldown = 100; // 4 second cooldown
                    currentAction = "walk";
                }
            }
            
            setImage(currentFrames.get(currentFrame));
        }
    }

    private void checkSwordHit() {
        if (currentAction.equals("walk")) {  // Only take damage when not attacking
            Player player = (Player) getOneObjectAtOffset(0, 0, Player.class);
            if (player != null && player.isAttacking() && player.hasSword()) {
                takeDamage(25);
            }
        }
    }

    public void takeDamage(int amount) {
        health -= amount;
        if (health <= 0) {
            getWorld().removeObject(this);
        }
    }

    private void flipSprites() {
        // Flip all walk frames
        for (GreenfootImage img : walkFrames) {
            img.mirrorHorizontally();
        }
        
        // Flip all attack frames
        for (GreenfootImage img : attackFrames) {
            img.mirrorHorizontally();
        }
    }
}
