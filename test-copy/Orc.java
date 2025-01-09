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
    private int moveSpeed = 1;
    private int turnCounter = 0;
    private boolean facingRight = true;
    private String currentDirection = "right"; // Can be "up", "down", "left", "right"

    public Orc() {
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
        turnCounter++;
        
        if (turnCounter >= 60) {
            turnCounter = 0;
            
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
        
        // Move based on direction without rotating the sprite
        switch(currentDirection) {
            case "right":
                setLocation(getX() + moveSpeed, getY());
                break;
            case "left":
                setLocation(getX() - moveSpeed, getY());
                break;
            case "up":
                setLocation(getX(), getY() - moveSpeed);
                break;
            case "down":
                setLocation(getX(), getY() + moveSpeed);
                break;
        }
        
        // World edge checking
        World world = getWorld();
        if (world != null) {
            int margin = 50;
            
            // If near edges, change direction
            if (getX() <= margin) {
                currentDirection = "right";
                if (!facingRight) {  // CHANGED: Flip to face right when moving right
                    flipSprites();
                    facingRight = true;
                }
            } else if (getX() >= world.getWidth() - margin) {
                currentDirection = "left";
                if (facingRight) {  // CHANGED: Flip to face left when moving left
                    flipSprites();
                    facingRight = false;
                }
            }
            if (getY() <= margin) {
                currentDirection = "down";
            } else if (getY() >= world.getHeight() - margin) {
                currentDirection = "up";
            }
        }
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
