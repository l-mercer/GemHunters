import greenfoot.*;
import java.util.ArrayList;

public class ShopWorld extends World {
    private int playerScore;
    private boolean shopMenuVisible = false; // Track if the shop menu is open

    public ShopWorld(int score) {
        super(852, 496, 1);
        setBackground("Assets/Levels/townv2.jpg"); // Town background
        playerScore = score;

        // Reset health to 100 when entering the shop
        Player.setHealth(100);

        // Display health and gems in the top-left corner
        updateHUD();

        showText("Click on the barn to open the shop!", getWidth() / 2, 50);
    }

    public void act() {
        if (!shopMenuVisible) {
            checkBuildingInteraction(); // Check for shop interaction only when menu is closed
        }
    }

    private void checkBuildingInteraction() {
        // Check for a mouse click within the barn's area
        if (Greenfoot.mouseClicked(null)) {
            MouseInfo mouse = Greenfoot.getMouseInfo();
            if (mouse != null) {
                int x = mouse.getX();
                int y = mouse.getY();

                // Shop area coordinates
                if (x >= 365 && x <= 515 && y >= 225 && y <= 359) {
                    openShopMenu(); // Open the shop menu
                }
            }
        }
    }

    private void openShopMenu() {
        shopMenuVisible = true; // Menu is now visible

        // Draw semi-transparent overlay
        GreenfootImage overlay = new GreenfootImage(getWidth(), getHeight());
        overlay.setColor(new Color(0, 0, 0, 150)); // Semi-transparent black
        overlay.fillRect(0, 0, getWidth(), getHeight());
        getBackground().drawImage(overlay, 0, 0);

        // Add text for the shop menu
        showText("SHOP MENU", getWidth() / 2, 100);
        showText("Your Gems: " + playerScore, getWidth() / 2, 140);

        // Add buttons
    Button buyHealthButton = new Button("Buy Health (5 Gems)");
    Button speedBoostButton = new Button("Speed Boost (10 Gems)");
    Button buySwordButton = new Button("Buy Sword (5 Gems)"); // New button
    Button closeButton = new Button("Close");
    
    addObject(buyHealthButton, getWidth() / 2, 200);
    addObject(speedBoostButton, getWidth() / 2, 250);
    addObject(buySwordButton, getWidth() / 2, 300); // Add sword button
    addObject(closeButton, getWidth() / 2, 350);
    }

public void handleButtonClick(Button button) {
    String label = button.getLabel();

    if (label.equals("Buy Health (5 Gems)") && playerScore >= 5) {
        playerScore -= 5;
        Player.upgradeHealth(100);
        showText("Your Gems: " + playerScore, getWidth() / 2, 140);
        showText("Health: " + Player.getHealth(), getWidth() / 2, 180);

    } else if (label.equals("Speed Boost (10 Gems)") && playerScore >= 10) {
        playerScore -= 10;
        Player.upgradeSpeed(1);
        showText("Your Gems: " + playerScore, getWidth() / 2, 140);

    } else if (label.equals("Buy Sword (5 Gems)") && playerScore >= 5) {
        playerScore -= 5;
        Player.setHasSword(true);
        showText("Your Gems: " + playerScore, getWidth() / 2, 140);
    } else if (label.equals("Close")) {
        closeShopMenu();
    } else if (label.equals("Next Level")) {
        Greenfoot.setWorld(new ForestWorld(playerScore, Player.getHealth(), Player.hasSword()));
    }
}


    private void updateHUD() {
        showText("Health: " + Player.getHealth(), 70, 20); // Display health in the top-left corner
        showText("Your Gems: " + playerScore, 70, 50); // Display gems in the top-left corner
    }

    private void closeShopMenu() {
        shopMenuVisible = false; // Menu is now closed
        removeObjects(getObjects(Button.class)); // Remove all buttons
        showText("", getWidth() / 2, 100); // Clear shop menu text
        showText("", getWidth() / 2, 140); // Clear gems text

        // Add "Next Level" button
        Button nextLevelButton = new Button("Next Level");
        addObject(nextLevelButton, getWidth() / 2, getHeight() - 50); // Place near the bottom
    }
}
