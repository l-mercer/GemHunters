import greenfoot.*;

public class MyWorld extends World {
    private int score = 0; // Track the player's score
    private Player player; // Reference to the Player object
    private GreenfootSound backgroundMusic = new GreenfootSound("video-game-music-loop-27629.mp3");

    // Default constructor (no arguments)
    public MyWorld() {
        this(0, 100); // Start with a score of 0 and health of 100
        Player.resetStats(); // Reset all player stats when starting a new game
        ShopWorld.resetLevelCounter(); // Add this line
    }

    // New constructor that accepts an initial score and health
    public MyWorld(int initialScore, int initialHealth) {
        super(800, 445, 1); // Create the world
        score = initialScore; // Set the initial score

        // Reset the player's health
        Player.setHealth(initialHealth);

        prepare();
        setBackground("Assets/Levels/Level-1.jpg");

        // Set background music
        backgroundMusic.setVolume(70);
        backgroundMusic.playLoop();
    }

    @Override
    public void stopped() {
        backgroundMusic.pause();
    }

    @Override
    public void started() {
        backgroundMusic.play();
    }

    private void prepare() {
        // Add the player to the centre of the world
        player = new Player();
        addObject(player, getWidth() / 2, getHeight() / 2);

        // Add 5 gems at random positions
        for (int i = 0; i < 5; i++) {
            Gem gem = new Gem();
            int x = Greenfoot.getRandomNumber(800);
            int y = Greenfoot.getRandomNumber(600);
            addObject(gem, x, y);
        }

        // Add 3 enemies at random positions
        for (int i = 0; i < 3; i++) {
            Enemy enemy = new Enemy();
            int x = Greenfoot.getRandomNumber(800);
            int y = Greenfoot.getRandomNumber(600);
            addObject(enemy, x, y);
        }
    }

    private void checkNextLevel() {
        if (getObjects(Gem.class).isEmpty()) {
            ShopWorld.setCurrentLevel(1); // Set to level 1 complete
            Greenfoot.setWorld(new ShopWorld(score));
        }
    }

    public void increaseScore() {
        score++;
        showText("Score: " + score, 70, 20); // Display the score
    }

    public void showGameOver() {
        Greenfoot.playSound("game-over.mp3"); // Play game-over sound
        showText("Game Over!", getWidth() / 2, getHeight() / 2); // Display Game Over message
    }

    public void act() {
        showText("Health: " + Player.getHealth(), 70, 40);
        showText("Score: " + score, 70, 20);

        checkNextLevel(); // Check if it's time to transition to the shop
    }
}
