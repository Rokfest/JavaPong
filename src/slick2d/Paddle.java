package slick2d;

import org.newdawn.slick.geom.Rectangle;

public class Paddle 
{
    private static int PADDLE_WIDTH = 10;
    private static int PADDLE_HEIGHT = 100;
    private static final int PADDLE_SPEED = 3;
    
    private Rectangle hitBox;
    private int direction;
    private int score;
    
    public Paddle(float horizontalCenter, float verticalCenter)
    {
        this.hitBox = new Rectangle(horizontalCenter, verticalCenter, PADDLE_WIDTH, PADDLE_HEIGHT);
        this.direction = 0;
        this.score = 0;
    }
    public float getX()
    {
        return this.hitBox.getX();
    }
    public float getY()
    {
        return this.hitBox.getY();
    }
    public int getDirection()
    {
        return this.direction;
    }
    public int getScore()
    {
        return this.score;
    }
    public Rectangle getHitBox()
    {
        return this.hitBox;
    }
    public void moveDown()
    {
        this.direction = PADDLE_SPEED;
        this.hitBox.setLocation(getX(), getY() + direction);
    }
    public void moveUp()
    {
        this.direction = -PADDLE_SPEED;
        this.hitBox.setLocation(getX(), getY() + direction);
    }
    public void resetDir()
    {
        this.direction = 0;
    }
    public void updateScore()
    {
        this.score++;
    }
}
