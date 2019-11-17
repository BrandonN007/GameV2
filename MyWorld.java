import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;

/**
 * Write a description of class MyWorld here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class MyWorld extends SimulationWorld
{
    private final static double CAMERA_SPEED = 5.0; // 1 meter per second
    private final static double CAMERA_AVATAR_DISTANCE = 5.0;
    
    public MyWorld()
    {    
        super("gameMusic.mp3", 1024, 768, new Point2D(8.0, 6.0), 16.0); 
        
        prepare();
    }

    public void act()
    {
        super.act();
        
        moveCameraFromAvatar();
        handleCannonBallCollisions();
        // reflectCannonBallsOnWindow();
    }

    public void moveCameraFromAvatar()
    {
        List<CannonBallAvatar> list = getObjects(CannonBallAvatar.class);
        
        if (list.size() > 0)
        {
            CannonBallAvatar avatar = list.get(0);
            double posX = avatar.getPosition().getX() + CAMERA_AVATAR_DISTANCE;
            cameraCenter.setX(posX);
            
            if (avatar.getPosition().getY() < -1.0)
            {
                transitionToWorld(new MyWorld());
            }
        }
    }
    
    public void moveCamera()
    {
        double dt = getTimeStepDuration();
        
        if (Greenfoot.isKeyDown("a")){
            cameraCenter.setX(cameraCenter.getX() - CAMERA_SPEED * dt);
        }
        if (Greenfoot.isKeyDown("d")){
            cameraCenter.setX(cameraCenter.getX() + CAMERA_SPEED * dt);
        }
        if (Greenfoot.isKeyDown("s")){
            cameraCenter.setY(cameraCenter.getY() - CAMERA_SPEED * dt);
        }        
        if (Greenfoot.isKeyDown("w")){
            cameraCenter.setY(cameraCenter.getY() + CAMERA_SPEED * dt);
        }
        if (Greenfoot.isKeyDown("-")){
            cameraWidth += CAMERA_SPEED * dt;
            scaleActors();
        }
        if (Greenfoot.isKeyDown("=") || Greenfoot.isKeyDown("+")){
            cameraWidth -= CAMERA_SPEED * dt;
            scaleActors();
        }    
    }
    
    private void handleCannonBallCollisions()
    {
        List<CannonBall> balls = getObjects(CannonBall.class);
        
        for (int i = 0; i < balls.size() - 1; i++)
        {
            for (int j = i + 1; j < balls.size(); j++)
            {
                CannonBall ball1 = balls.get(i);
                CannonBall ball2 = balls.get(j);
                
                Vector2D ball1ToBall2 = new Vector2D(ball2.getX() - ball1.getX(),
                                                     ball2.getY() - ball1.getY());
                double distance = ball1ToBall2.magnitude();
                
                if (distance < ball1.getRadius() + ball2.getRadius())
                {
                    collisionResponse(ball1, ball2);
                }
            }
        }
    }
    
    private void collisionResponse(CannonBall ball1, CannonBall ball2)
    {
        if (ball1.getPosition() == null || ball2.getPosition() == null)
            return;
        
        Vector2D n = Vector2D.substract(ball2.getPosition(), ball1.getPosition());                
        double distance = n.magnitude();
        double ball1Radius = windowToWorld(ball1.getImage().getHeight() / 2);
        double ball2Radius = windowToWorld(ball2.getImage().getHeight() / 2);
        
        double overlap = distance - ball1Radius - ball2Radius;

        // Compute vectors for the collision axis
        n.normalize();
        Vector2D t = new Vector2D(-n.getY(), n.getX());

        // Separate the circles
        ball1.getPosition().add(Vector2D.multiply(n, overlap / 2));
        ball2.getPosition().add(Vector2D.multiply(n, -overlap / 2));

        // Velocities according to n and t
        Vector2D v1t = Vector2D.multiply(t, Vector2D.dot(ball1.getVelocity(), t));
        Vector2D v1n = Vector2D.multiply(n, Vector2D.dot(ball1.getVelocity(), n));
        Vector2D v2t = Vector2D.multiply(t, Vector2D.dot(ball2.getVelocity(), t));
        Vector2D v2n = Vector2D.multiply(n, Vector2D.dot(ball2.getVelocity(), n));

        // Velocities after collision
        ball1.setVelocity(Vector2D.add(v1t, v2n));
        ball2.setVelocity(Vector2D.add(v2t, v1n));
    }
    
    private void reflectCannonBallsOnWindow()
    {
        List<CannonBall> balls = getObjects(CannonBall.class);
        
        for (int i = 0; i < balls.size(); i++)
        {
            CannonBall ball = balls.get(i);
            
            Vector2D v = ball.getVelocity();
            double ballRadius = ball.getImage().getHeight() / 2;
            
            if (ball.getX() < ballRadius) // left
            {
                ball.setVelocity(new Vector2D(Math.abs(v.getX()), v.getY()));
            }
            
            if (ball.getX() > getWidth() - ballRadius) // right
            {
                ball.setVelocity(new Vector2D(- Math.abs(v.getX()), v.getY()));
            }
            
            if (ball.getY() < ballRadius) // top
            {
                ball.setVelocity(new Vector2D(v.getX(), - Math.abs(v.getY())));
            }
            
            if (ball.getY() > getHeight() - ballRadius ) // bottom
            {
                ball.setVelocity(new Vector2D(v.getX(), 0.9 * Math.abs(v.getY())));
            }
            
        }
    }    
    
    
    private void prepare()
    {
        // Create the ground floor
        for (int i=16; i < 10240; i += 32)
        {
            GroundPlatform gp = new GroundPlatform();
            addObject(gp, i, 752);
        }

        // Add a few brick platforms
        for (int i=16; i < 10240; i += 32)
        {
            // Gap between platforms
            if (i % 336 == 0)
            {  
                i += 320;
            }

            BrickPlatform bp = new BrickPlatform();
            addObject(bp, i, 624);
        }

        CannonBallAvatar cannonBallAvatar = new CannonBallAvatar();
        addObject(cannonBallAvatar,94,399);
        CannonEnemy cannonEnemy = new CannonEnemy();
        addObject(cannonEnemy,781,296);
        TimeOverlay timeOverlay = new TimeOverlay();
        addObject(timeOverlay,924,42);
        MovingBrickPlatform movingBrickPlatform = new MovingBrickPlatform(2, 5);
        addObject(movingBrickPlatform,468,622);
    }
}
