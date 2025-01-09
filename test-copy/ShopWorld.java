import greenfoot.*;
import java.util.ArrayList;

public class ShopWorld extends World {
    private int playerScore;
    private boolean shopMenuVisible = false; // Track if the shop menu is open
    private static int currentLevel = 1; // Track which level was just completed

    public ShopWorld(int score) {
        super(852, 496, 1);
        setBackground("Assets/Levels/townv2.jpg"); // Town background
        playerScore = score;

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
        shopMenuVisible = true;

        // Draw semi-transparent overlay
        GreenfootImage overlay = new GreenfootImage(getWidth(), getHeight());
        overlay.setColor(new Color(0, 0, 0, 150));
        overlay.fillRect(0, 0, getWidth(), getHeight());
        getBackground().drawImage(overlay, 0, 0);

        // Add text for the shop menu
        showText("SHOP MENU", getWidth() / 2, 100);
        showText("Your Gems: " + playerScore, getWidth() / 2, 140);

        int yPosition = 200;  // Starting Y position for buttons

        // Only show health upgrade button if health is less than max (e.g., 300)
        if (Player.getHealth() < 300) {
            Button buyHealthButton = new Button("Buy Health (5 Gems)");
            addObject(buyHealthButton, getWidth() / 2, yPosition);
            yPosition += 50;
        }

        // Speed boost option is always available
        Button speedBoostButton = new Button("Speed Boost (10 Gems)");
        addObject(speedBoostButton, getWidth() / 2, yPosition);
        yPosition += 50;

        // Only show sword button if player doesn't have it
        if (!Player.hasSword()) {
            Button buySwordButton = new Button("Buy Sword (5 Gems)");
            addObject(buySwordButton, getWidth() / 2, yPosition);
            yPosition += 50;
        }

        Button closeButton = new Button("Close");
        addObject(closeButton, getWidth() / 2, yPosition);
    }

    public void handleButtonClick(Button button) {
        String label = button.getLabel();

        if (label.equals("Next Level")) {
            switch(currentLevel) {
                case 1:
                    // After first level, go to level 2 (Forest)
                    Greenfoot.setWorld(new ForestWorld(playerScore, Player.getHealth(), Player.hasSword()));
                    break;
                case 2:
                    // After second level, go to level 3 (Fort)
                    Greenfoot.setWorld(new FortWorld(playerScore, Player.getHealth(), Player.hasSword()));
                    break;
                case 3:
                    // After final level, start new game
                    currentLevel = 0; // Reset to 0 because MyWorld will increment it
                    Greenfoot.setWorld(new MyWorld());
                    break;
            }
        } else if (label.equals("Buy Health (5 Gems)") && playerScore >= 5) {
            playerScore -= 5;
            Player.upgradeHealth(100);
            updateShopDisplay();
        } else if (label.equals("Speed Boost (10 Gems)") && playerScore >= 10) {
            playerScore -= 10;
            Player.upgradeSpeed(1);
            updateShopDisplay();
        } else if (label.equals("Buy Sword (5 Gems)") && playerScore >= 5) {
            playerScore -= 5;
            Player.setHasSword(true);
            updateShopDisplay();
        } else if (label.equals("Close")) {
            closeShopMenu();
        }
    }

    private void updateShopDisplay() {
        showText("Your Gems: " + playerScore, getWidth() / 2, 140);
        updateHUD();
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

    // Add this method to reset level counter when starting new game
    public static void resetLevelCounter() {
        currentLevel = 1;
    }

    public static void setCurrentLevel(int level) {
        currentLevel = level;
    }
}
