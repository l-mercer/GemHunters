import greenfoot.*;

public class VictoryWorld extends World {
    public VictoryWorld(int finalScore) {
        super(553, 450, 1);
        setBackground("Assets/Levels/YouWin.jpg");
        
        // Add restart button below the "YOU WIN" text
        Button restartButton = new Button("Start Again");
        addObject(restartButton, getWidth() / 2, getHeight() - 150);
        
        // Display final score
        showText("Final Score: " + finalScore, getWidth() / 2, getHeight() - 100);
    }
    
    public void handleButtonClick(Button button) {
        if (button.getLabel().equals("Start Again")) {
            // Reset everything and start new game
            Player.resetStats();
            ShopWorld.resetLevelCounter();
            Greenfoot.setWorld(new MyWorld());
        }
    }
} 