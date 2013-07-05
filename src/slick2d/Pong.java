package slick2d;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
//A bit of a hack to change the ball into a circle
import org.newdawn.slick.geom.RoundedRectangle;

public class Pong extends BasicGame
{
    //Screen Dynamics
    private static final int SCREEN_WIDTH = 640;
    private static final int SCREEN_HEIGHT = 480;
    //Paddle and ball sizes
    private static int PADDLE_WIDTH = 10;
    private static int PADDLE_HEIGHT = 100;
    private static int BALL_SIZE = 12;
    //Initiate all actors
    private static Paddle leftPaddle;
    private static Paddle rightPaddle;
    private static RoundedRectangle ball;
    //Set ball speed and initiate directional vectors
    private static int ballSpeed = 3;
    private static int horizontalSpeed;
    private static int verticalSpeed;
    //Random variables necessary for time tracking
    private static long curTime;
    //Some hack to make the ball a circle
    private static int CURVE_RADIUS = 10;

    public Pong(String gamename)
    {
        super(gamename);
    }

    @Override
    public void init(GameContainer gc) throws SlickException
    {
        curTime = System.currentTimeMillis();
        horizontalSpeed = -ballSpeed;
        verticalSpeed = -ballSpeed;

        int paddle1 = 1;
        int paddle2 = (SCREEN_WIDTH - PADDLE_WIDTH - 1);
        int y_center = (SCREEN_HEIGHT - PADDLE_HEIGHT) / 2;

        leftPaddle = new Paddle(paddle1, y_center);
        rightPaddle = new Paddle(paddle2, y_center);
        ball = new RoundedRectangle((SCREEN_WIDTH - BALL_SIZE) / 2
                , SCREEN_HEIGHT / 2, BALL_SIZE, BALL_SIZE, CURVE_RADIUS);
    }

    @Override
    public void update(GameContainer gc, int i) throws SlickException
    {
        //Input detect for Player 1
        Input input = gc.getInput();
        if (input.isKeyDown(Input.KEY_DOWN))
        {
            if (leftPaddle.getY() < SCREEN_HEIGHT - 1 - PADDLE_HEIGHT)
            {
                leftPaddle.moveDown();
            }
        } else if (input.isKeyDown(Input.KEY_UP))
        {
            if (leftPaddle.getY() > 1)
            {
                leftPaddle.moveUp();
            }
        }
        //Input detect for Player 2
        if (input.isKeyDown(Input.KEY_S))
        {
            if (rightPaddle.getY() < SCREEN_HEIGHT - 1 - PADDLE_HEIGHT)
            {
                rightPaddle.moveDown();
            }
        } else if (input.isKeyDown(Input.KEY_W))
        {
            if (rightPaddle.getY() > 1)
            {
                rightPaddle.moveUp();
            }
        }
        //Ball physics | Currently inverts directions at all walls
        if (detectCollision())
        {
            //Collisions are simple for now but take into account paddle movement            
            horizontalSpeed *= -1;
            //Random mechanic to speed up ball speed after 5 seconds, just for fun.
            if (System.currentTimeMillis() - curTime >= 5000)
            {
                ballSpeed++;
                if (horizontalSpeed < 0)
                {
                    horizontalSpeed = -ballSpeed;
                } else
                {
                    horizontalSpeed = ballSpeed;
                }
                curTime = System.currentTimeMillis();
            }
        } else if (ball.getX() <= 1 || ball.getX() >= SCREEN_WIDTH - 1 - BALL_SIZE)
        {
            //Reset the ball if it goes pass the paddles & Update player score
            ball.setLocation((SCREEN_WIDTH - BALL_SIZE) / 2, SCREEN_HEIGHT / 2);
            curTime = System.currentTimeMillis();
            ballSpeed = 2;
            if (horizontalSpeed < 0)
            {
                rightPaddle.updateScore();
                horizontalSpeed = ballSpeed;
            } else
            {
                leftPaddle.updateScore();
                horizontalSpeed = -ballSpeed;
            }
            verticalSpeed = -ballSpeed;

            try
            {
                Thread.sleep(1000);
            } catch (Exception ex)
            {
                throw new RuntimeException(ex);
            }

            //Give score here
        }
        if (ball.getY() <= 1 || ball.getY() >= SCREEN_HEIGHT - 1 - BALL_SIZE)
        {
            verticalSpeed *= -1;
        }
        
        leftPaddle.resetDir();
        rightPaddle.resetDir();
        
        ball.setLocation(ball.getX() + horizontalSpeed, ball.getY() + verticalSpeed);
        //gc.getGraphics().draw(ball);
    }

    //Detect collisions with the paddles
    private static boolean detectCollision()
    {
        //When the ball is going to the left
        if (horizontalSpeed < 0)
        {
            //Ball should be within paddle horizontal range
            if (ball.getX() <= leftPaddle.getX() + PADDLE_WIDTH + 1)
            {
                //Ball should be within vertical range as well
                if (ball.getY() >= leftPaddle.getY() && ball.getY() <= (leftPaddle.getY() + PADDLE_HEIGHT))
                {
                    //Here we change the vertical speed of the ball depending on player1
                    //movement
                    if(leftPaddle.getDirection() > 0)
                    {
                        if(verticalSpeed > 0)
                        {
                            verticalSpeed++;
                        }
                        else
                            verticalSpeed--;
                    }
                    else if(leftPaddle.getDirection() < 0)
                    {
                        if(verticalSpeed > 0)
                        {
                            verticalSpeed--;
                        }
                        else
                            verticalSpeed++;
                    }
                    return true;
                }
            }
        } else
        {
            //Ball should be within paddle horizontal range
            if (ball.getX() + BALL_SIZE >= rightPaddle.getX() - 1)
            {
                //Ball should be within vertical range as well
                if (ball.getY() >= rightPaddle.getY() && ball.getY() <= (rightPaddle.getY() + PADDLE_HEIGHT))
                {
                    //Here we change the vertical speed of the ball depending on player2
                    //movement
                    if(rightPaddle.getDirection() > 0)
                    {
                        if(verticalSpeed > 0)
                        {
                            verticalSpeed--;
                        }
                        else
                            verticalSpeed++;
                    }
                    else if(rightPaddle.getDirection() < 0)
                    {
                        if(verticalSpeed > 0)
                        {
                            verticalSpeed++;
                        }
                        else
                            verticalSpeed--;
                    }
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void render(GameContainer gc, Graphics g) throws SlickException
    {
        
        //Draw game background
        g.setColor(Color.white);
        g.drawString("Pong", (SCREEN_WIDTH) / 4, 0);
        g.drawString("" + leftPaddle.getScore(), (SCREEN_WIDTH) / 2 - 30, 10);
        g.drawString("" + rightPaddle.getScore(), (SCREEN_WIDTH) / 2 + 20, 10);
        g.drawLine(SCREEN_WIDTH / 2, 0, SCREEN_WIDTH / 2, SCREEN_HEIGHT);

        
        g.setColor(Color.green);
        g.draw(leftPaddle.getHitBox());
        g.draw(rightPaddle.getHitBox());
        g.setColor(Color.white);
        g.draw(ball);

    }

    public static void main(String[] args)
    {
        try
        {
            AppGameContainer appgc;
            appgc = new AppGameContainer(new Pong("Pong"));
            appgc.setDisplayMode(SCREEN_WIDTH, SCREEN_HEIGHT, false);
            //Setting the FrameRate took away the need to use Thread.sleep()
            appgc.setTargetFrameRate(60);
            appgc.start();
        } catch (SlickException ex)
        {
            Logger.getLogger(Pong.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
