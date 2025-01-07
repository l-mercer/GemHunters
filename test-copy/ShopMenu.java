import greenfoot.*;

public class ShopMenu extends World {
    private int playerScore;
    private GreenfootImage menuBackground;

    public ShopMenu(int score) {
        super(800, 600, 1); // Create the shop menu world
        playerScore = score;

        // Set a transparent grey background over the screen
        menuBackground = new GreenfootImage(getWidth(), getHeight());
        menuBackground.setColor(new Color(0, 0, 0, 128)); // Semi-transparent grey
        menuBackground.fillRect(200, 150, 400, 300); // Centered rectangle (size 400x300)
        getBackground().drawImage(menuBackground, 0, 0);

        // Display menu text
        showText("SHOP MENU", getWidth() / 2, 170); // Title
        showText("Your Gems: " + playerScore, getWidth() / 2, 200); // Player's current gems
        showText("1. Buy Health (+20) - 5 Gems", getWidth() / 2, 250);
        showText("2. Increase Speed - 10 Gems", getWidth() / 2, 300);
        showText("Press Enter to Return to the Map", getWidth() / 2, 350);
    }

    public void act() {
        // Handle purchases
        if (Greenfoot.isKeyDown("1") && playerScore >= 5) {
            playerScore -= 5;
            Player.upgradeHealth(20);
            showText("Your Gems: " + playerScore, getWidth() / 2, 200); // Update gem count
        }

        if (Greenfoot.isKeyDown("2") && playerScore >= 10) {
            playerScore -= 10;
            Player.upgradeSpeed(1);
            showText("Your Gems: " + playerScore, getWidth() / 2, 200); // Update gem count
        }

        // Return to the map
        if (Greenfoot.isKeyDown("enter")) {
            Greenfoot.setWorld(new ShopWorld(playerScore));
        }
    }
}
