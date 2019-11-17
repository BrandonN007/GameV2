import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
* Write a description of class CannonBall here.
* 
* @author (your name) 
* @version (a version number or a date)
*/
public class CannonBall extends PlatformActor
{
    protected static final double MOVE_DECELERATION = 2.0;
    
    protected Point2D lastPosition;
    
    public CannonBall()
    {
        super(null, new Vector2D(0.0, 0.0), new Vector2D(0.0, GRAVITY));
        lastPosition = position;
    }
    
    public void act() 
    {
        super.act();
        
        decelerate();
        rollBall();    
    }    
    
    public void decelerate()
    {
        if (onPlatform)
        {
            double dt = getSimulationWorld().getTimeStepDuration();
            
            if (velocity.getX() < 0.0)
            {
                velocity.setX(Math.min(velocity.getX() + MOVE_DECELERATION * dt, 0.0));
            }
            else if (velocity.getX() > 0.0)
            {
                velocity.setX(Math.max(velocity.getX() - MOVE_DECELERATION * dt, 0.0));
            }
        }
    }
    
    public void rollBall()
    {
        if (onPlatform == true)
        {
            double circumference = 2 * Math.PI * getRadius();
            double dx = worldToWindow(position.getX() - lastPosition.getX());
            double angleVariation = dx * 360 / circumference;
            
            setRotation(getRotation() + (int) angleVariation);
        }
        
        lastPosition = new Point2D(position);
    }    
}
















