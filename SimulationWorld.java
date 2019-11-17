import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;
import java.awt.Graphics2D; 
import java.awt.image.*; 
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;


public class SimulationWorld extends World
{
    protected final double DEFAULT_CAMERA_WIDTH; // 1 meter per second
    
    protected Point2D cameraCenter;
    protected double cameraWidth;
    private Matrix2D toWindowMatrix;
    private Matrix2D toWorldMatrix;

    private long lastFrameTimeMS;
    private double timeStepDuration;
    
    protected GreenfootSound music;

    public SimulationWorld(String musicFile, int windowWidth, int windowHeight, Point2D cameraCenter, double cameraWidth)
    {    
        super(windowWidth, windowHeight, 1, false); 
        
        if (musicFile != null && musicFile.isEmpty() == false)
        {
            music = new GreenfootSound(musicFile);
        }
        
        // Save the initial width to compute the zooming ratio
        DEFAULT_CAMERA_WIDTH = cameraWidth;
        
        this.cameraCenter = cameraCenter;
        this.cameraWidth = cameraWidth;
        this.toWindowMatrix = Matrix2D.worldToWindow(cameraCenter, cameraWidth, new Vector2D(getWidth(), getHeight()));
        this.toWorldMatrix = Matrix2D.windowToWorld(cameraCenter, cameraWidth, new Vector2D(getWidth(), getHeight()));
    }
    
    public void started()
    {
        if (music != null)
        {
            music.playLoop();
        }
        
        lastFrameTimeMS = System.currentTimeMillis();
    }
    
    public void stopped()
    {
        if (music != null)
        {
            music.pause();
        }
    }
    
    public GreenfootSound getMusic()
    {
        return music;
    }
    
    public void setMusic(GreenfootSound newMusic)
    {
        if (music.isPlaying() == true)
        {
            music.stop();
            newMusic.playLoop();
        }
        
        music = newMusic;
    }
    
    public void transitionToWorld(SimulationWorld world)
    {
        stopped();
        
        if (world.getMusic() == null)
        {
            world.setMusic(music);
        }
        
        Greenfoot.setWorld(world);
        world.started();
    }

    public void act()
    {
        // Update time step duration
        timeStepDuration = (System.currentTimeMillis() - lastFrameTimeMS) / 1000.0;
        lastFrameTimeMS = System.currentTimeMillis();

        // Update Viewport Matrix
        toWindowMatrix = Matrix2D.worldToWindow(cameraCenter, cameraWidth, new Vector2D(getWidth(), getHeight()));
        toWorldMatrix = Matrix2D.windowToWorld(cameraCenter, cameraWidth, new Vector2D(getWidth(), getHeight()));
    }
    
    public double getTimeStepDuration()
    {
        return timeStepDuration;
    }

    public double worldToWindow(double worldValue)
    {
        return worldValue * getWidth() / cameraWidth;
    }

    public Point2D worldToWindow(Point2D worldCoord)
    {
        return toWindowMatrix.transform(worldCoord);
    }

    public Vector2D worldToWindow(Vector2D worldCoord)
    {
        return toWindowMatrix.transform(worldCoord);
    }
    
    public double windowToWorld(double windowValue)
    {
        return windowValue * cameraWidth / getWidth();
    }
    
    public Point2D windowToWorld(Point2D windowCoord)
    {        
        return toWorldMatrix.transform(windowCoord);
    }

    public Vector2D windowToWorld(Vector2D windowCoord)
    {        
        return toWorldMatrix.transform(windowCoord);
    }
    
    public void scaleActors()
    {
        List<SimulationActor> actors = getObjects(SimulationActor.class);
        
        for (int i=0; i<actors.size(); i++)
        {
            SimulationActor actor = actors.get(i);
            actor.scaleImage(getZoomRatio());
        }
    }
    
    public double getZoomRatio()
    {
        return DEFAULT_CAMERA_WIDTH / cameraWidth;
    }
    
    public void addObject(Actor a, int x, int y)
    {
        super.addObject(a,x,y);
        
        if (a instanceof SimulationActor)
        {
            SimulationActor sa = (SimulationActor) a;
            sa.setPosition(windowToWorld(new Point2D(x, y)));
        }            
    }
}
