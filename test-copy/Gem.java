import greenfoot.*;  // Import the Greenfoot library

public class Gem extends Actor {
    public Gem() {
        GreenfootImage gemImage = new GreenfootImage("gem.png");
        gemImage.scale(20, 20);
        setImage(gemImage);
    }

    public static void playSound() {
        Greenfoot.playSound("gem-collection.mp3"); // Plays gem collection sound
    }
}
