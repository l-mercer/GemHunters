import greenfoot.*;
import java.util.ArrayList;

public class ShopWorld extends World {
    private int playerScore;
    private boolean shopMenuVisible = false; // See if the shop menu is open
    private static int currentLevel = 1; // tracks the level the player is at 

    public ShopWorld(int score) {
        super(852, 496, 1);
        setBackground("Assets/Levels/townv2.jpg"); // sets background img
        playerScore = score;


        updateHUD();

        showText("Click on the barn to open the shop!", getWidth() / 2, 50);
    }

    public void act() {
        if (!shopMenuVisible) {
            checkBuildingInteraction(); 
        }
    }

    private void checkBuildingInteraction() {
        // Check for a mouse click
        if (Greenfoot.mouseClicked(null)) {
            MouseInfo mouse = Greenfoot.getMouseInfo();
            if (mouse != null) {
                int x = mouse.getX();
                int y = mouse.getY();

                if (x >= 365 && x <= 515 && y >= 225 && y <= 359) {
                    openShopMenu(); 
                }
            }
        }
    }

    private void openShopMenu() {
        shopMenuVisible = true;

        GreenfootImage overlay = new GreenfootImage(getWidth(), getHeight());
        overlay.setColor(new Color(0, 0, 0, 150));
        overlay.fillRect(0, 0, getWidth(), getHeight());
        getBackground().drawImage(overlay, 0, 0);

        // Adds shop menu text 
        showText("SHOP MENU", getWidth() / 2, 100);
        showText("Your Gems: " + playerScore, getWidth() / 2, 140);

        int yPosition = 200;  // the y position for the button

        // will only show health upgrade if the player has less that 300 hp
        if (Player.getHealth() < 300) {
            Button buyHealthButton = new Button("Buy Health (5 Gems)");
            addObject(buyHealthButton, getWidth() / 2, yPosition);
            yPosition += 50;
        }

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
                    Greenfoot.setWorld(new ForestWorld(playerScore, Player.getHealth(), Player.hasSword()));
                    break;
                case 2:
                    Greenfoot.setWorld(new FortWorld(playerScore, Player.getHealth(), Player.hasSword()));
                    break;
                case 3:
                    Greenfoot.setWorld(new GrassWorld(playerScore, Player.getHealth(), Player.hasSword()));
                    break;
                case 4:
                    // Game completed, return to start
                    currentLevel = 1;
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
        showText("Health: " + Player.getHealth(), 70, 20); // displays the players health 
        showText("Your Gems: " + playerScore, 70, 50); // displays the players score 
    }

    private void closeShopMenu() {
        shopMenuVisible = false; // Menu is now closed
        removeObjects(getObjects(Button.class));
        showText("", getWidth() / 2, 100);
        showText("", getWidth() / 2, 140); 

        // adds the next level button
        Button nextLevelButton = new Button("Next Level");
        addObject(nextLevelButton, getWidth() / 2, getHeight() - 50); // Place near the bottom
    }

  
    public static void resetLevelCounter() {
        currentLevel = 1;
    }

    public static void setCurrentLevel(int level) {
        currentLevel = level;
    }
}
