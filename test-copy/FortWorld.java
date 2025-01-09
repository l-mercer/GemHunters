import greenfoot.*;

public class FortWorld extends World {
    private Player player;
    private int playerScore;

    public FortWorld(int score, int health, boolean hasSword) {
        super(1058, 531, 1);
        setBackground("Assets/Levels/FortLevel.jpg");
        
        playerScore = score;  // Start with previous score
        Player.setHealth(health);
        Player.setHasSword(hasSword);

        spawnOrcs();
        spawnGems();

        // Add the player to the world
        player = new Player();
        addObject(player, getWidth() / 2, getHeight() / 2);

        // Display initial score and health
        updateHUD();
    }
    
    private void spawnOrcs() {
        // Spawn 11 Orcs (increased from 6)
        for (int i = 0; i < 11; i++) {
            Orc orc = new Orc(true);  // Create faster orcs
            // Random position within safe margins
            int x = 100 + Greenfoot.getRandomNumber(getWidth() - 200);
            int y = 100 + Greenfoot.getRandomNumber(getHeight() - 200);
            
            addObject(orc, x, y);
            orc.startWalking();
        }
    }

    private void spawnGems() {
        // Spawn 15 gems for the final level
        for (int i = 0; i < 15; i++) {
            Gem gem = new Gem();
            int x = 50 + Greenfoot.getRandomNumber(getWidth() - 100);
            int y = 50 + Greenfoot.getRandomNumber(getHeight() - 100);
            addObject(gem, x, y);
        }
    }

    public void act() {
        updateHUD();
        checkNextLevel();
    }

    private void updateHUD() {
        showText("Score: " + playerScore, 70, 20);
        showText("Health: " + Player.getHealth(), 70, 40);
    }

    public void increaseScore() {
        playerScore++;
        updateHUD();  // Make sure HUD updates when score changes
        
        // Check if all gems are collected
        if (getObjects(Gem.class).isEmpty()) {
            ShopWorld.setCurrentLevel(3);
            Greenfoot.setWorld(new ShopWorld(playerScore));
        }
    }

    private void checkNextLevel() {
        if (getObjects(Gem.class).isEmpty()) {
            ShopWorld.setCurrentLevel(3);
            Greenfoot.setWorld(new ShopWorld(playerScore));
        }
    }

    public void showGameOver() {
        Greenfoot.playSound("game-over.mp3");
        showText("Game Over!", getWidth() / 2, getHeight() / 2);
        Greenfoot.stop();
    }
} 