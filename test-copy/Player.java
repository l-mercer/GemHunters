import greenfoot.*;

public class Player extends Actor {
    private int health = 100;

    public void act() {
        move();
        checkCollisions();
    }

    private void move() {
        if (Greenfoot.isKeyDown("up")) setLocation(getX(), getY() - 4);
        if (Greenfoot.isKeyDown("down")) setLocation(getX(), getY() + 4);
        if (Greenfoot.isKeyDown("left")) setLocation(getX() - 4, getY());
        if (Greenfoot.isKeyDown("right")) setLocation(getX() + 4, getY());
    }

    private void checkCollisions() {
        if (isTouching(Gem.class)) {
            removeTouching(Gem.class);
            Gem.playSound(); // Play the sound effect
            ((MyWorld) getWorld()).increaseScore(); // Update the score in MyWorld
        }
    
        if (isTouching(Enemy.class)) {
            health -= 10;
            System.out.println("Health: " + health);
            if (health <= 0) {
                ((MyWorld) getWorld()).showGameOver(); // Display Game Over message
                Greenfoot.stop();
            }
        }
    }

    
        public int getHealth() {
        return health;
    }

    

}
