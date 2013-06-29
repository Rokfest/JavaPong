package slick2d;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import org.newdawn.slick.geom.Rectangle;

public class Pong extends BasicGame
{
    //Screen Dynamics

    private static final int SCREEN_WIDTH = 640;
    private static final int SCREEN_HEIGHT = 480;
    //Paddle and ball sizes
    private static int PADDLE_WIDTH = 10;
    private static int PADDLE_HEIGHT = 100;
    private static int BALL_SIZE = 10;
    //Initiate all actors
    private static Rectangle player1;
    private static Rectangle player2;
    private static Rectangle ball;
    //Set ball speed and initiate directional vectors
    private static int ballSpeed = 2;
    private static int x;
    private static int y;
    
    //Random variables necessary for time tracking
    private static long curTime;

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
        ball = new Rectangle((SCREEN_WIDTH - BALL_SIZE) / 2, SCREEN_HEIGHT / 2, BALL_SIZE, BALL_SIZE);
    }

    @Override
    public void update(GameContainer gc, int i) throws SlickException
    {
        //Ball physics (Let's just get it bouncing) Inverts directions at walls
        if (ball.getY() <= 1 || ball.getY() >= SCREEN_HEIGHT - 1 - BALL_SIZE) {
            y *= -1;
        }
        if (ball.getX() <= 1 || ball.getX() >= SCREEN_WIDTH - 1 - BALL_SIZE) {
            x *= -1;
        }

        ball.setLocation(ball.getX() + x, ball.getY() + y);
        gc.getGraphics().draw(ball);
        
        //Random mechanic to speed up ball speed after 5 seconds, just for fun.
        if(System.currentTimeMillis() - curTime >= 5000)
        {
            ballSpeed++;
            if(x < 0)
                x = -ballSpeed;
            else
                x = ballSpeed;
            if(y < 0)
                y = -ballSpeed;
            else
                y = ballSpeed;
            curTime = System.currentTimeMillis();
        }
    }

    @Override
    public void render(GameContainer gc, Graphics g) throws SlickException
    {
        //Draw game background
        g.drawString("Pong", (SCREEN_WIDTH) / 4, 0);
        g.drawLine(SCREEN_WIDTH / 2, 0, SCREEN_WIDTH / 2, SCREEN_HEIGHT);

        g.draw(player1);
        g.draw(player2);
        g.draw(ball);

    }

    public static void main(String[] args)
    {
        try {
            AppGameContainer appgc;
            appgc = new AppGameContainer(new Pong("Pong"));
            appgc.setDisplayMode(SCREEN_WIDTH, SCREEN_HEIGHT, false);
            appgc.start();
        } catch (SlickException ex) {
            Logger.getLogger(Pong.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
