import greenfoot.*;

public class ShopMenu extends World {
    private int playerScore;
    private GreenfootImage ShopMenu;

    public ShopMenu(int score) {
        super(800, 600, 1); // Creates the menu for the shop world
        playerScore = score;

 
        ShopMenu = new GreenfootImage(getWidth(), getHeight());
        ShopMenu.setColor(new Color(0, 0, 0, 128)); 
        ShopMenu.fillRect(200, 150, 400, 300); 
        getBackground().drawImage(ShopMenu, 0, 0);

        // Display menu text
        showText("SHOP MENU", getWidth() / 2, 170); 
        showText("Your Gems: " + playerScore, getWidth() / 2, 200); 
        showText("1. Buy Health (+20) - 5 Gems", getWidth() / 2, 250);
        showText("2. Increase Speed - 10 Gems", getWidth() / 2, 300);
        showText("Press Enter to Return to the Map", getWidth() / 2, 350);
    }

    public void act() {
        // Handless purchases
        if (Greenfoot.isKeyDown("1") && playerScore >= 5) {
            playerScore -= 5;
            Player.upgradeHealth(20);
            showText("Your Gems: " + playerScore, getWidth() / 2, 200); // updates the gem count
        }

        if (Greenfoot.isKeyDown("2") && playerScore >= 10) {
            playerScore -= 10;
            Player.upgradeSpeed(1);
            showText("Your Gems: " + playerScore, getWidth() / 2, 200); // pdates the gem count
        }

        if (Greenfoot.isKeyDown("enter")) {
            Greenfoot.setWorld(new ShopWorld(playerScore));
        }
    }
}
