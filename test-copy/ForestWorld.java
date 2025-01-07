import greenfoot.*;

public class ForestWorld extends World {
    private Player player;
    private int playerScore;

    public ForestWorld(int score, int health, boolean hasSword) {
        super(785, 441, 1); // Set world size

        setBackground("Assets/Levels/forest.jpg");
        
        spawnOrcs();

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
            
            // Random initial rotation
            int rotation = Greenfoot.getRandomNumber(360);
            
            addObject(orc, x, y);
            orc.setRotation(rotation);
            orc.startWalking();
        }
    }

    public void act() {
        updateHUD();
    }

    private void updateHUD() {
        showText("Score: " + playerScore, 70, 20); // Update the score display
        showText("Health: " + Player.getHealth(), 70, 40); // Continuously update health display
    }
}


