import greenfoot.*;

public class ForestWorld extends World {
    private Player player;
    private int playerScore;

    public ForestWorld(int score, int health, boolean hasSword) {
        super(785, 441, 1); // Set world size

        setBackground("Assets/Levels/forest.jpg");
        
        spawnOrcs();
        spawnGems();

        // Set player score and health
        playerScore = score;
        Player.setHealth(health); // Reset health
        Player.setHasSword(hasSword); // Set the sword status

        // Add the player to the world
        player = new Player();
        addObject(player, getWidth() / 2, getHeight() / 2);

        // Display score and health
        showText("Score: " + score, 70, 20);
        showText("Health: " + health, 70, 40);
    }
    
     private void spawnOrcs() {
        // Spawn 4 Orcs at different random positions
        for (int i = 0; i < 4; i++) {
            Orc orc = new Orc();
            // Random position within safe margins
            int x = 100 + Greenfoot.getRandomNumber(getWidth() - 200);
            int y = 100 + Greenfoot.getRandomNumber(getHeight() - 200);
            
            addObject(orc, x, y);
            orc.startWalking();
        }
    }

    private void spawnGems() {
        // Spawn 10 gems at random positions
        for (int i = 0; i < 10; i++) {
            Gem gem = new Gem();
            // Random position within safe margins
            int x = 50 + Greenfoot.getRandomNumber(getWidth() - 100);  // Keep 50px from edges
            int y = 50 + Greenfoot.getRandomNumber(getHeight() - 100); // Keep 50px from edges
            addObject(gem, x, y);
        }
    }

    public void act() {
        updateHUD();
        checkNextLevel();
    }

    private void updateHUD() {
        showText("Score: " + playerScore, 70, 20); // Update the score display
        showText("Health: " + Player.getHealth(), 70, 40); // Continuously update health display
    }

    public void increaseScore() {
        playerScore++;
        showText("Score: " + playerScore, 70, 20); // Update the score display
    }

    private void checkNextLevel() {
        if (getObjects(Gem.class).isEmpty()) {
            ShopWorld.setCurrentLevel(2); // Set to level 2 complete
            Greenfoot.setWorld(new ShopWorld(playerScore));
        }
    }
}


