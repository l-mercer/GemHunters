import greenfoot.*;

public class MyWorld extends World {
    private int score = 0; // Used to track the players score
    private Player player; // Used to get the Player from the player class 
    private GreenfootSound backgroundMusic = new GreenfootSound("video-game-music-loop-27629.mp3");

    public MyWorld() {
        this(0, 100); // Starts the score of with 0 and the player health is set too 100 
        Player.resetStats(); // Makes it so when the player restarts teh game it resets everything 
        ShopWorld.resetLevelCounter(); 
    }

    public MyWorld(int initialScore, int initialHealth) {
        super(800, 445, 1); // sets the dimentions of the world
        score = initialScore; // Sets the score for the player

        // Reset the players health
        Player.setHealth(initialHealth);

        prepare();
        setBackground("Assets/Levels/Level-1.jpg");

        // Sets the background music 
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
        // spawns the player in the middle of the map 
        player = new Player();
        addObject(player, getWidth() / 2, getHeight() / 2);

        // spawns 5 gems in the world
        for (int i = 0; i < 5; i++) {
            Gem gem = new Gem();
            int x = Greenfoot.getRandomNumber(800);
            int y = Greenfoot.getRandomNumber(600);
            addObject(gem, x, y);
        }

        // spawns 3 spiders 
        for (int i = 0; i < 3; i++) {
            Enemy enemy = new Enemy();
            int x = Greenfoot.getRandomNumber(800);
            int y = Greenfoot.getRandomNumber(600);
            addObject(enemy, x, y);
        }
    }

    private void checkNextLevel() {
        if (getObjects(Gem.class).isEmpty()) {
            ShopWorld.setCurrentLevel(1); // after the player has collected all the gems sets the level to the shop world
            Greenfoot.setWorld(new ShopWorld(score));
        }
    }

    public void increaseScore() {
        score++;
        showText("Score: " + score, 70, 20); // updates the score for when the player has collected a gem 
    }

    public void showGameOver() {
        Greenfoot.playSound("game-over.mp3"); // Plays game over sound
        showText("Game Over!", getWidth() / 2, getHeight() / 2); // shows game over message 
    }

    public void act() {
        showText("Health: " + Player.getHealth(), 70, 40);
        showText("Score: " + score, 70, 20);

        checkNextLevel(); 
    }
}
