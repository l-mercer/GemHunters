import greenfoot.*;

public class Button extends Actor {
    private String label;

    public Button(String text) {
        label = text;

        // Create button appearance
        GreenfootImage img = new GreenfootImage(200, 50); // Button size
        img.setColor(Color.LIGHT_GRAY);
        img.fillRect(0, 0, 200, 50); // Button background
        img.setColor(Color.BLACK);
        img.drawRect(0, 0, 200, 50); // Button border
        img.drawString(label, 20, 30); // Button text
        setImage(img);
    }

    public String getLabel() {
        return label;
    }

    public void act() {
        // Detect mouse clicks on the button
        if (Greenfoot.mouseClicked(this)) {
            if (getWorld() instanceof ShopWorld) {
                ((ShopWorld) getWorld()).handleButtonClick(this);
            } else if (getWorld() instanceof VictoryWorld) {
                ((VictoryWorld) getWorld()).handleButtonClick(this);
            }
        }
    }

    public boolean isClicked() {
        return Greenfoot.mouseClicked(this);
    }
}
