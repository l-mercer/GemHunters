import greenfoot.*;  // Import the Greenfoot library

public class Gem extends Actor {
    public Gem() {
        GreenfootImage gemImage = new GreenfootImage("gem.png");
        gemImage.scale(42, 42); // Match the player's size
        setImage(gemImage);
    }

    public static void playSound() {
        Greenfoot.playSound("gem-collection.mp3"); // Play gem collection sound
    }
}
