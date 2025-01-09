import greenfoot.*;
import java.util.ArrayList;

public class ForestWorld extends World {
    private Player player;
    private int playerScore;

    public ForestWorld(int score, int health, boolean hasSword) {
        super(785, 441, 1); // sets thee size of the world 

        setBackground("Assets/Levels/forest.jpg");
        
        spawnOrcs();
        spawnGems();

        // Sets the score and health of the player 
        playerScore = score;
        Player.setHealth(health); // resets the players health
        Player.setHasSword(hasSword); // setting the player status of if it has sword or not 

        // adds player to the world
        player = new Player();
        addObject(player, getWidth() / 2, getHeight() / 2);

        // Shows the HUD for the players Health and Score
        showText("Score: " + score, 70, 20);
        showText("Health: " + health, 70, 40);
    }
    
     private void spawnOrcs() {
        ArrayList<Orc> orcs = new ArrayList<>();
        int minDistance = 158; // Min distance a orc can spawn next to each other 
        int maxAttempts = 42;  // Max finds the best spawn point 

        // Spawns 4 orc enemys 
        for (int i = 0; i < 4; i++) {
            Orc orc = new Orc();
            boolean validPosition = false;
            int attempts = 0;

            // If it doesnt find a good position it will keep going till it does
            while (!validPosition && attempts < maxAttempts) {
                // random possition 
                int x = 100 + Greenfoot.getRandomNumber(getWidth() - 200);
                int y = 100 + Greenfoot.getRandomNumber(getHeight() - 200);
                
                // Chcecks distance from other orcs 
                validPosition = true;
                for (Orc other : orcs) {
                    int dx = x - other.getX();
                    int dy = y - other.getY();
                    double distance = Math.sqrt(dx * dx + dy * dy);
                    
                    if (distance < minDistance) {
                        validPosition = false;
                        break;
                    }
                }
                
                if (validPosition) {
                    addObject(orc, x, y);
                    orcs.add(orc);
                    orc.startWalking();
                }
                attempts++;
            }
            
            // If after a certain amount of attempts it cant find a position it will just spawn it anywhere 
            if (!validPosition) {
                int x = 100 + Greenfoot.getRandomNumber(getWidth() - 200);
                int y = 100 + Greenfoot.getRandomNumber(getHeight() - 200);
                addObject(orc, x, y);
                orcs.add(orc);
                orc.startWalking();
            }
        }
    }

    private void spawnGems() {
        // Spawns 10 gems in the world at random positions within it 
        for (int i = 0; i < 10; i++) {
            Gem gem = new Gem();
            // spawns them at Random positions 
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
        showText("Score: " + playerScore, 70, 20); // updates the score display
        showText("Health: " + Player.getHealth(), 70, 40); // updates players health
    }

    public void increaseScore() {
        playerScore++;
        showText("Score: " + playerScore, 70, 20); // update the score displays when they collect a gem 
    }

    private void checkNextLevel() {
        if (getObjects(Gem.class).isEmpty()) {
            ShopWorld.setCurrentLevel(2); // Sets it back to the shop world 
            Greenfoot.setWorld(new ShopWorld(playerScore));
        }
    }

    public void showGameOver() {
        Greenfoot.playSound("game-over.mp3");
        showText("Game Over!", getWidth() / 2, getHeight() / 2);
        Greenfoot.stop();
    }
}


