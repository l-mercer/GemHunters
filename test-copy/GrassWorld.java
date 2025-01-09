import greenfoot.*;
import java.util.ArrayList;

public class GrassWorld extends World {
    private Player player;
    private int playerScore;
    private int totalGemsCollected = 0;
    private int gemSpawnTimer = 0;
    private final int GEM_SPAWN_DELAY = 250; // Gives about a 10 second delay for the spawning of the gems 
    private final int REQUIRED_GEMS = 10;
    private final int TOTAL_ORCS = 8;

    public GrassWorld(int score, int health, boolean hasSword) {
        super(996, 512, 1);
        setBackground("Assets/Levels/grassmap.jpg");
        
        playerScore = score;
        Player.setHealth(health);
        Player.setHasSword(hasSword);

        // Adds the player to the maps
        player = new Player();
        addObject(player, getWidth() / 2, getHeight() / 2);

        // spawns the orcs at the start of the game
        spawnInitialOrcs();

        // displays the score and health hud 
        showText("Score: " + score, 70, 20);
        showText("Health: " + health, 70, 40);
        showText("Gems Collected: " + totalGemsCollected + "/" + REQUIRED_GEMS, getWidth() - 100, 30);
    }

    private void spawnInitialOrcs() {
        for (int i = 0; i < TOTAL_ORCS; i++) {
            spawnOrc();
        }
    }

    private void spawnOrc() {
        Orc orc = new Orc(true); // makes the orcs faster 
        int margin = 100;
        int x, y;
        boolean validPosition;

        do {
            validPosition = true;
            x = margin + Greenfoot.getRandomNumber(getWidth() - 2 * margin);
            y = margin + Greenfoot.getRandomNumber(getHeight() - 2 * margin);

            // checks the distance from the player so it doesnt spawn on them
            int dx = x - player.getX();
            int dy = y - player.getY();
            double distanceToPlayer = Math.sqrt(dx * dx + dy * dy);

            if (distanceToPlayer < 200) { // sets minimum distance and sees if its a vailid position 
                validPosition = false;
            }
        } while (!validPosition);

        addObject(orc, x, y);
        orc.startWalking();
    }

    private void spawnGem() {
        Gem gem = new Gem();
        int margin = 50;
        int x = margin + Greenfoot.getRandomNumber(getWidth() - 2 * margin);
        int y = margin + Greenfoot.getRandomNumber(getHeight() - 2 * margin);
        addObject(gem, x, y);
    }

    public void act() {
        updateHUD();
        maintainOrcCount();
        handleGemSpawning();
    }

    private void maintainOrcCount() {
        // Counts current orcs
        int currentOrcs = getObjects(Orc.class).size();
        // Spawn new orcs if needed
        if (currentOrcs < TOTAL_ORCS) {
            spawnOrc();
        }
    }

    private void handleGemSpawning() {
        gemSpawnTimer++;
        if (gemSpawnTimer >= GEM_SPAWN_DELAY && getObjects(Gem.class).isEmpty() 
            && totalGemsCollected < REQUIRED_GEMS) {
            spawnGem();
            gemSpawnTimer = 0;
        }
    }

    private void updateHUD() {
        showText("Score: " + playerScore, 70, 20);
        showText("Health: " + Player.getHealth(), 70, 40);
        showText("Gems Collected: " + totalGemsCollected + "/" + REQUIRED_GEMS, getWidth() - 100, 30);
    }

    public void increaseScore() {
        playerScore++;
        totalGemsCollected++;
        updateHUD();
        
        // Checks to see if the player has collected all the gems 
        if (totalGemsCollected >= REQUIRED_GEMS) {
            ShopWorld.setCurrentLevel(4); // Sets to the victory world.
            Greenfoot.setWorld(new VictoryWorld(playerScore));
        }
    }

    public void showGameOver() {
        Greenfoot.playSound("game-over.mp3");
        showText("Game Over!", getWidth() / 2, getHeight() / 2);
        Greenfoot.stop();
    }
} 