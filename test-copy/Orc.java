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

    public Orc() {
        // Load walk frames
        for (int i = 1; i <= 8; i++) {
            GreenfootImage img = new GreenfootImage("Orc/Orc_Walk/orc_walk_frame_" + i + ".png");
            img.scale(100, 100);
            walkFrames.add(img);
        }
        
        // Load attack frames
        for (int i = 1; i <= 6; i++) {
            GreenfootImage img = new GreenfootImage("Orc/Orc_Attack/orc_attack_frame_" + i + ".png");
            img.scale(100, 100);
            attackFrames.add(img);
        }
        
        setImage(walkFrames.get(0));
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
        // Set random initial direction
        setRotation(Greenfoot.getRandomNumber(360));
    }

    private void randomMovement() {
        turnCounter++;
        
        // More frequent but gentler turns
        if (turnCounter >= 20) { // Every ~20 acts
            turnCounter = 0;
            // Small random turn (-20 to +20 degrees)
            turn(Greenfoot.getRandomNumber(41) - 20);
        }
        
        // Random chance for additional direction changes
        if (Greenfoot.getRandomNumber(100) < 3) {
            turn(Greenfoot.getRandomNumber(41) - 20);
        }
        
        // Move forward
        move(moveSpeed);
        
        // World edge checking
        World world = getWorld();
        if (world != null) {
            int margin = 50;
            
            // If near edges, turn more dramatically
            if (getX() <= margin || getX() >= world.getWidth() - margin ||
                getY() <= margin || getY() >= world.getHeight() - margin) {
                
                // Turn away from edges
                if (getX() <= margin) {
                    setRotation(0 + Greenfoot.getRandomNumber(180));
                } else if (getX() >= world.getWidth() - margin) {
                    setRotation(180 + Greenfoot.getRandomNumber(180));
                }
                if (getY() <= margin) {
                    setRotation(90 + Greenfoot.getRandomNumber(180));
                } else if (getY() >= world.getHeight() - margin) {
                    setRotation(270 + Greenfoot.getRandomNumber(180));
                }
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
}
