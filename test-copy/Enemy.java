import greenfoot.*;

public class Enemy extends Actor {
    public void act() {
        move(2); // Move forward at a speed of 2

        // Randomly turn to change direction
        if (Greenfoot.getRandomNumber(100) < 10) { // 10% chance each frame
            turn(Greenfoot.getRandomNumber(90) - 45); // Turn randomly between -45 and 45 degrees
        }

        // Turn around if at the edge of the world
        if (isAtEdge()) {
            turn(180); // Reverse direction
        }
    }
}
