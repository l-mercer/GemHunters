import greenfoot.*;

public class Enemy extends Actor {
    public void act() {
        move(2); // moves the enemy forward and sets it to the speed of 2

        // Randomly turn to change direction
        if (Greenfoot.getRandomNumber(100) < 10) { // 10% chance 
            turn(Greenfoot.getRandomNumber(90) - 45); // will turn randomly between -45 and 45 degrees
        }

        // Turns around if at the edge of the world
        if (isAtEdge()) {
            turn(180); // Reverses direction of the enenmy
        }
    }
}
