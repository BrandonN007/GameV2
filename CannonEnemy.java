import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class CannonEnemy here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class CannonEnemy extends Cannon
{
    protected final static double TIME_BEFORE_SHOOTING = 2.0;
    protected double timeUntilShooting;
    
    public CannonEnemy()
    {
        super();
        
        timeUntilShooting = TIME_BEFORE_SHOOTING;
        alignWithVector(new Vector2D(-1, 0));
    }   
    
    public void act() 
    {
        double dt = getSimulationWorld().getTimeStepDuration();
        timeUntilShooting -= dt;
        
        if (timeUntilShooting < 0.0)
        {
            Vector2D ballVelocity = new Vector2D(-1 * CANNON_BALL_VELOCITY, 0.0);
            shoot(ballVelocity);
            timeUntilShooting += TIME_BEFORE_SHOOTING;
        }
    }    
}
