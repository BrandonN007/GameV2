import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)


public class Cannon extends SimulationActor
{
    protected final static double CANNON_BALL_VELOCITY = 10.0;
    
    public void act() 
    {
        super.act();
        
        MouseInfo mouse = Greenfoot.getMouseInfo();
        
        if (mouse != null)
        {
            Vector2D cannonToMouse = new Vector2D(mouse.getX() - getX(), 
                                                  mouse.getY() - getY());
                                                  
            alignWithVector(cannonToMouse);
            
            if (Greenfoot.mouseClicked(null))
            {
                // Calculate Velocity Vector
                cannonToMouse = windowToWorld(cannonToMouse);
                cannonToMouse.normalize();
                cannonToMouse = Vector2D.multiply(cannonToMouse, CANNON_BALL_VELOCITY);
                
                shoot(cannonToMouse);
            }
        }
    }    
    
    public void shoot(Vector2D velocity)
    {
        // Shoot cannon ball with velocity
        CannonBall ball = new CannonBall();
        ball.setVelocity(velocity);
        SimulationWorld world = (SimulationWorld) getWorld();
        world.addObject(ball, getX(), getY());
        
        // Adjust ball size according to zoom value
        ball.saveOriginalImage();
        ball.scaleImage(world.getZoomRatio());
        
        Greenfoot.playSound("cannonSound.wav");
    }    
}











