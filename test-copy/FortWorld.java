import greenfoot.*;

public class FortWorld extends World {
    private Player player;
    private int playerScore;

    public FortWorld(int score, int health, boolean hasSword) {
        super(1058, 531, 1); // Same size as other levels

        setBackground("Assets/Levels/FortLevel.jpg");
        
        spawnOrcs(); // We can keep orcs as enemies or modify this later
        spawnGems();

        // Set player score and health
        playerScore = score;
        Player.setHealth(health);
        Player.setHasSword(hasSword);

        // Add the player to the world
        player = new Player();
        addObject(player, getWidth() / 2, getHeight() / 2);

        // Display score and health
        showText("Score: " + score, 70, 20);
        showText("Health: " + health, 70, 40);
    }
    
    private void spawnOrcs() {
        // Spawn 6 Orcs for increased difficulty
        for (int i = 0; i < 6; i++) {
            Orc orc = new Orc();
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
        showText("Score: " + playerScore, 70, 20);
    }

    private void checkNextLevel() {
        if (getObjects(Gem.class).isEmpty()) {
            ShopWorld.setCurrentLevel(3); // Set to level 3 complete
            Greenfoot.setWorld(new ShopWorld(playerScore));
        }
    }
} 