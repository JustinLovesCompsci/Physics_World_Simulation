package springies;

import org.jbox2d.common.Vec2;

import jboxGlue.PhysicalObjectCircle;
import jgame.JGColor;
import jgame.JGObject;
/**
 * @author Justin (Zihao) Zhang, Nick (Pengyi) Pan
 */
public class Mass extends PhysicalObjectCircle {
	
	protected static final double MASS_RADIUS = 5;
	protected static final int MASS_CID = 1;
	protected static final int MASS_DEFAULT = 1;
	protected static final int MASS_XVEL_DEFAULT = 0;
	protected static final int MASS_YVEL_DEFAULT = 0;
    protected static final double DAMPING_FACTOR = 0.8;
	
	protected double initX;
	protected double initY;
	protected double myMass;

	public Mass(String id, double xPos, double yPos, double mass, double xVel, double yVel,JGColor color,double radius) {
		super(id,MASS_CID,color,radius,mass);
   		initX = xPos;
   		initY = yPos;
   		myMass = mass;
		Vec2 velocity = myBody.getLinearVelocity();
   		velocity.x = (float) xVel;
   		velocity.y = (float) yVel;
        myBody.setLinearVelocity(velocity); // apply the change
	}
	
	// mass not given; use default mass
	public Mass(String id, double xPos, double yPos, double xVel, double yVel,JGColor color,double radius) { 
		this(id, xPos, yPos, MASS_DEFAULT, xVel, yVel,color,radius);
	}
	
	// velocities and mass not given; use default velocities and mass
	public Mass(String id, double xPos, double yPos,JGColor color,double radius) {
		this(id, xPos, yPos, MASS_DEFAULT, MASS_XVEL_DEFAULT, MASS_YVEL_DEFAULT,color,radius);
	}
		
	public Mass(String id, double xPos, double yPos,double mass,JGColor color,double radius) {// the drag mass constructor
		this(id, xPos, yPos, mass, MASS_XVEL_DEFAULT, MASS_YVEL_DEFAULT,color,radius);
	}
	
	
	public double getMass(){ return myMass; }
	
	public double getX(){ return this.x; }
	
	public double getY(){ return this.y; }
	
	public double getInitX(){ return initX; }
	
	public double getInitY(){ return initY; }
	
	public double getRadius(){ return MASS_RADIUS; }
	
	public void hit (JGObject other)
    {
		if(other.colid != 2) return;
        Vec2 velocity = myBody.getLinearVelocity();
        boolean isSide = other.getBBox().height > other.getBBox().width;
        
        if (isSide) { velocity.x *= -DAMPING_FACTOR; }
        else { velocity.y *= -DAMPING_FACTOR; }
        
        myBody.setLinearVelocity(velocity);              
    }
	
    @Override
    public String toString(){
    	return "Mass";
    }
    
    
}
