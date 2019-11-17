import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class PlatformActor here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class PlatformActor extends SimulationActor
{
    protected boolean onPlatform;    
    
    public PlatformActor()
    {
        super();
        onPlatform = false;
    }
    
    public PlatformActor(Point2D position, Vector2D velocity, Vector2D acceleration)
    {
        super(position, velocity, acceleration);
        onPlatform = false;
    }
    
    public void act() 
    {
        super.act();
        landOnPlatform();
    } 
    
    public void landOnPlatform()
    {
        Platform p1 = (Platform) getOneObjectAtOffset(-getRadius(), getRadius() + 1, Platform.class);
        Platform p2 = (Platform) getOneObjectAtOffset( getRadius(), getRadius() + 1, Platform.class);
        Platform p = (p1 == null) ? p2 : p1;
        
        if (p != null  && velocity.getY() < 0.0)
        {
            // Update position to lie on the platform
            setLocation(getX(), p.getY() - p.getHeight() / 2 - getRadius());
            
            // Update the velocity to stop falling
            velocity.setY(Math.max(velocity.getY(), 0.0));

            onPlatform = true;
        }
        else
        {
            onPlatform = false;
        }        
    }    
}
