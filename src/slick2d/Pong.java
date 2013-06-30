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
import org.newdawn.slick.geom.Rectangle;
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
    private static int PADDLE_SPEED = 3;
    private static int BALL_SIZE = 12;
    //Initiate all actors
    private static Rectangle player1;
    private static Rectangle player2;
    private static RoundedRectangle ball;
    //Set ball speed and initiate directional vectors
    private static int ballSpeed = 3;
    private static int x;
    private static int y;
    //Player variables
    private static int player1Move = 0;
    private static int player2Move = 0;
    private static int player1Score = 0;
    private static int player2Score = 0;
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
        x = -ballSpeed;
        y = -ballSpeed;

        int x_player1 = 1;
        int x_player2 = (SCREEN_WIDTH - PADDLE_WIDTH - 1);
        int y_center = (SCREEN_HEIGHT - PADDLE_HEIGHT) / 2;

        player1 = new Rectangle(x_player1, y_center, PADDLE_WIDTH, PADDLE_HEIGHT);
        player2 = new Rectangle(x_player2, y_center, PADDLE_WIDTH, PADDLE_HEIGHT);
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
            if (player1.getY() < SCREEN_HEIGHT - 1 - PADDLE_HEIGHT)
            {
                player1.setLocation(player1.getX(), player1.getY() + PADDLE_SPEED);
                player1Move = PADDLE_SPEED;
            }
        } else if (input.isKeyDown(Input.KEY_UP))
        {
            if (player1.getY() > 1)
            {
                player1.setLocation(player1.getX(), player1.getY() - PADDLE_SPEED);
                player1Move = -PADDLE_SPEED;
            }
        }
        //Input detect for Player 2
        if (input.isKeyDown(Input.KEY_S))
        {
            if (player2.getY() < SCREEN_HEIGHT - 1 - PADDLE_HEIGHT)
            {
                player2.setLocation(player2.getX(), player2.getY() + PADDLE_SPEED);
                player2Move = PADDLE_SPEED;
            }
        } else if (input.isKeyDown(Input.KEY_W))
        {
            if (player2.getY() > 1)
            {
                player2.setLocation(player2.getX(), player2.getY() - PADDLE_SPEED);
                player2Move = -PADDLE_SPEED;
            }
        }
        //Ball physics | Currently inverts directions at all walls
        if (detectCollision())
        {
            //Collisions are simple for now but take into account paddle movement            
            x *= -1;
            //Random mechanic to speed up ball speed after 5 seconds, just for fun.
            if (System.currentTimeMillis() - curTime >= 5000)
            {
                ballSpeed++;
                if (x < 0)
                {
                    x = -ballSpeed;
                } else
                {
                    x = ballSpeed;
                }
                curTime = System.currentTimeMillis();
            }
        } else if (ball.getX() <= 1 || ball.getX() >= SCREEN_WIDTH - 1 - BALL_SIZE)
        {
            //Reset the ball if it goes pass the paddles & Update player score
            ball.setLocation((SCREEN_WIDTH - BALL_SIZE) / 2, SCREEN_HEIGHT / 2);
            curTime = System.currentTimeMillis();
            ballSpeed = 2;
            if (x < 0)
            {
                player2Score++;
                x = ballSpeed;
            } else
            {
                player1Score++;
                x = -ballSpeed;
            }
            y = -ballSpeed;

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
            y *= -1;
        }
        
        player1Move = 0;
        player2Move = 0;
        
        ball.setLocation(ball.getX() + x, ball.getY() + y);
        gc.getGraphics().draw(ball);
    }

    //Detect collisions with the paddles
    private static boolean detectCollision()
    {
        //When the ball is going to the left
        if (x < 0)
        {
            //Ball should be within paddle horizontal range
            if (ball.getX() <= player1.getX() + PADDLE_WIDTH + 1)
            {
                //Ball should be within vertical range as well
                if (ball.getY() >= player1.getY() && ball.getY() <= (player1.getY() + PADDLE_HEIGHT))
                {
                    //Here we change the vertical speed of the ball depending on player1
                    //movement
                    if(player1Move > 0)
                    {
                        if(y > 0)
                        {
                            y++;
                        }
                        else
                            y--;
                    }
                    else if(player1Move < 0)
                    {
                        if(y > 0)
                        {
                            y--;
                        }
                        else
                            y++;
                    }
                    return true;
                }
            }
        } else
        {
            //Ball should be within paddle horizontal range
            if (ball.getX() + BALL_SIZE >= player2.getX() - 1)
            {
                //Ball should be within vertical range as well
                if (ball.getY() >= player2.getY() && ball.getY() <= (player2.getY() + PADDLE_HEIGHT))
                {
                    //Here we change the vertical speed of the ball depending on player2
                    //movement
                    if(player2Move > 0)
                    {
                        if(y > 0)
                        {
                            y++;
                        }
                        else
                            y--;
                    }
                    else if(player2Move < 0)
                    {
                        if(y > 0)
                        {
                            y--;
                        }
                        else
                            y++;
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
        g.drawString("" + player1Score, (SCREEN_WIDTH) / 2 - 30, 10);
        g.drawString("" + player2Score, (SCREEN_WIDTH) / 2 + 20, 10);
        g.drawLine(SCREEN_WIDTH / 2, 0, SCREEN_WIDTH / 2, SCREEN_HEIGHT);

        
        g.setColor(Color.green);
        g.draw(player1);
        g.draw(player2);
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
