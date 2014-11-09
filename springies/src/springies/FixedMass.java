package springies;

import jgame.JGColor;
import jgame.JGObject;
/**
 * @author Justin (Zihao) Zhang, Nick (Pengyi) Pan
 */
public class FixedMass extends Mass {
	
	private double initX;
	private double initY;
	protected static final int FIXEDMASS_DEFAULT = 1;

	public FixedMass(String id, double xPos, double yPos, double mass,JGColor color,double radius) {
		super(id, xPos, yPos,0,color,radius); // "Play" fixed mass as having no mass
   		initX = xPos;
   		initY = yPos;
   		myMass = mass;
	}
	
	// mass not given; use default mass
	public FixedMass(String id, double xPos, double yPos,JGColor color,double radius) {
		this(id, xPos, yPos, FIXEDMASS_DEFAULT,color,radius);
	}
	
	public double getX(){ return this.x; }
	
	public double getY(){ return this.y; }
	
	public double getInitX(){ return initX; }
	
	public double getInitY(){ return initY; }
	
    @Override
    public String toString(){
    	return "Fixed Mass";
    }
	
	@Override
	public void hit (JGObject other)
    {
        // do nothing
    }
}
