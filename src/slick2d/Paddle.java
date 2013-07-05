package slick2d;

import org.newdawn.slick.geom.Rectangle;

/**
 * This class controls the paddles.
 * @author Cris
 */
public class Paddle 
{
    private static int PADDLE_WIDTH = 10;
    private static int PADDLE_HEIGHT = 100;
    private static final int PADDLE_SPEED = 3;
    
    private Rectangle hitBox;
    private int direction;
    private int score;
    
    /**
     * When creating a new paddle, pass parameters to where you want the paddle's
     * top left corner. It's a bit weird, but it's how rectangles, and most shapes,
     * are created on screen.
     * @param horizontalTopLeft
     * @param verticalCenter
     */
    public Paddle(float horizontalTopLeft, float verticalCenter)
    {
        this.hitBox = new Rectangle(horizontalTopLeft, verticalCenter, PADDLE_WIDTH, PADDLE_HEIGHT);
        this.direction = 0;
        this.score = 0;
    }
    /**
     * Returns the top left corner's horizontal position.
     * @return
     */
    public float getX()
    {
        return this.hitBox.getX();
    }
    /**
     * Returns the top left corner's vertical position 
     * @return
     */
    public float getY()
    {
        return this.hitBox.getY();
    }
    /**
     * Returns the paddle's current direction, useful for collision physics.
     * @return
     */
    public int getDirection()
    {
        return this.direction;
    }
    /**
     * Returns the paddle's current score.
     * @return
     */
    public int getScore()
    {
        return this.score;
    }
    /**
     * Returns the rectangle responsible for holding the paddle's hitbox.
     * Right now, we're using it to display graphically on-screen as well.
     * @return
     */
    public Rectangle getHitBox()
    {
        return this.hitBox;
    }
    /**
     * Updates movement in the paddle. (Down)
     */
    public void moveDown()
    {
        this.direction = PADDLE_SPEED;
        this.hitBox.setLocation(getX(), getY() + direction);
    }
    /**
     * Updates movement in the paddle. (Up)
     */
    public void moveUp()
    {
        this.direction = -PADDLE_SPEED;
        this.hitBox.setLocation(getX(), getY() + direction);
    }
    /**
     * Resets the direction of the paddle to zero. (standstill)
     */
    public void resetDir()
    {
        this.direction = 0;
    }
    /**
     * Increases the score by one.
     */
    public void updateScore()
    {
        this.score++;
    }
}
