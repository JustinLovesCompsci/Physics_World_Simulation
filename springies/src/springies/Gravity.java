package springies;

import java.util.ArrayList;
/**
 * @author Justin (Zihao) Zhang, Nick (Pengyi) Pan
 */
public class Gravity extends Force {
	private static final int GRAVITY_ID = 'G';
	
	private double myDirection;
	private float myXForce;
	private float myYForce;
	
	public Gravity(double magnitude, double direction){
		super(GRAVITY_ID, magnitude);
		myDirection = direction;
	}
    
    public void setDirection(double direction){
    	myDirection = direction;
    }

	@Override
	public void applyForce(ArrayList<Mass> masses) {
    	double degree = myDirection * Math.PI/180;
    	myXForce = (float) (Math.cos(degree) * myMagnitude);
    	myYForce = (float) (Math.sin(degree) * myMagnitude);
    	for(Mass curMass: masses){ 				// loop a list of movable Masses
    		curMass.setForce(myXForce, myYForce);
    	}
	}
	
	@Override
	public String toString(){
		return "Gravity";
	}
}
