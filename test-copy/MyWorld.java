import greenfoot.*;

public class MyWorld extends World {
    private int score = 0; // Track the player's score
    private Player player; // Reference to the Player object
    private GreenfootSound backgroundMusic = new GreenfootSound("video-game-music-loop-27629.mp3");

    public MyWorld() {
        // Create an 800x600 world with a cell size of 1x1 pixels
        super(800, 445, 1);

        // Set the background image to Level-1.jpg
        setBackground("Assets/Levels/Level-1.jpg");


        // Prepare the world
        prepare();

        // Set the background music volume and start looping
        backgroundMusic.setVolume(55);
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
            addObject(gem, Greenfoot.getRandomNumber(800), Greenfoot.getRandomNumber(600));
        }

        // Add 3 enemies at random positions
        for (int i = 0; i < 3; i++) {
            Enemy enemy = new Enemy();
            addObject(enemy, Greenfoot.getRandomNumber(800), Greenfoot.getRandomNumber(600));
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
}
