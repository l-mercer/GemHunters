import greenfoot.*;

public class VictoryWorld extends World {
    public VictoryWorld(int finalScore) {
        super(553, 450, 1);
        setBackground("Assets/Levels/YouWin.jpg");
        
        // restart button to restart the game after the player has finished it
        Button restartButton = new Button("Start Again");
        addObject(restartButton, getWidth() / 2, getHeight() - 150);
        
        // shows players final score
        showText("Final Score: " + finalScore, getWidth() / 2, getHeight() - 100);
    }
    
    public void handleButtonClick(Button button) {
        if (button.getLabel().equals("Start Again")) {
            // resets everything
            Player.resetStats();
            ShopWorld.resetLevelCounter();
            Greenfoot.setWorld(new MyWorld());
        }
    }
} 