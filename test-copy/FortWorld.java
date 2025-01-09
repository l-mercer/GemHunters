import greenfoot.*;

public class FortWorld extends World {
    private Player player;
    private int playerScore;

    public FortWorld(int score, int health, boolean hasSword) {
        super(1058, 531, 1);
        setBackground("Assets/Levels/FortLevel.jpg");
        
        playerScore = score;  // Gets the score from the previous level and starts the game off with that 
        Player.setHealth(health);
        Player.setHasSword(hasSword);

        spawnOrcs();
        spawnGems();

        // adds the player to this world 
        player = new Player();
        addObject(player, getWidth() / 2, getHeight() / 2);

        // will display the HUD for health and score 
        updateHUD();
    }
    
    private void spawnOrcs() {
        // Spawns 11 orcs on this level 
        for (int i = 0; i < 11; i++) {
            Orc orc = new Orc(true);  // Takes the orcs and makes it so they are double the speed of the ones from the previous level 
            // Spawns orcs within a random position on the world  
            int x = 100 + Greenfoot.getRandomNumber(getWidth() - 200);
            int y = 100 + Greenfoot.getRandomNumber(getHeight() - 200);
            
            addObject(orc, x, y);
            orc.startWalking();
        }
    }

    private void spawnGems() {
        // Spawns 15 gems for this level 
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
        updateHUD();  // Makes sure that when the player collects a gem it updates the HUD 
        
        // Sees if the player has collects all the gems on the map
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