import greenfoot.*;
import java.util.ArrayList;

public class GrassWorld extends World {
    private Player player;
    private int playerScore;
    private int totalGemsCollected = 0;
    private int gemSpawnTimer = 0;
    private final int GEM_SPAWN_DELAY = 250; // About 10 seconds (25 acts per second)
    private final int REQUIRED_GEMS = 10;
    private final int TOTAL_ORCS = 8;

    public GrassWorld(int score, int health, boolean hasSword) {
        super(996, 512, 1);
        setBackground("Assets/Levels/grassmap.jpg");
        
        playerScore = score;
        Player.setHealth(health);
        Player.setHasSword(hasSword);

        // Add the player
        player = new Player();
        addObject(player, getWidth() / 2, getHeight() / 2);

        // Initial orc spawn
        spawnInitialOrcs();

        // Display score and health
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
        Orc orc = new Orc(true); // Fast orcs for final level
        int margin = 100;
        int x, y;
        boolean validPosition;

        do {
            validPosition = true;
            x = margin + Greenfoot.getRandomNumber(getWidth() - 2 * margin);
            y = margin + Greenfoot.getRandomNumber(getHeight() - 2 * margin);

            // Check distance from player
            int dx = x - player.getX();
            int dy = y - player.getY();
            double distanceToPlayer = Math.sqrt(dx * dx + dy * dy);

            if (distanceToPlayer < 200) { // Minimum spawn distance from player
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
        checkVictory();
    }

    private void maintainOrcCount() {
        // Count current orcs
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
        
        // Check if all gems are collected
        if (totalGemsCollected >= REQUIRED_GEMS) {
            ShopWorld.setCurrentLevel(4); // Set to final level complete
            Greenfoot.setWorld(new VictoryWorld(playerScore));
        }
    }

    private void checkVictory() {
        // This can be removed or left empty since victory is checked in increaseScore
    }

    public void showGameOver() {
        Greenfoot.playSound("game-over.mp3");
        showText("Game Over!", getWidth() / 2, getHeight() / 2);
        Greenfoot.stop();
    }
} 