package springies;

import java.util.ArrayList;
/**
 * @author Justin (Zihao) Zhang, Nick (Pengyi) Pan
 */
public class WallRepulsion extends Force {
	protected final static double WALL_MARGIN = 0;
	protected final static double DISPLAY_WIDTH = 853;
	protected final static double DISPLAY_HEIGHT = 480;
	protected final static int POSITIVE_SIGN = 1;
	protected final static int NEGATIVE_SIGN = -1;
	
	public WallRepulsion(int id, double magnitude, double exponent){
		super(id, magnitude, exponent);
	}

	public void applyForce(ArrayList<Mass> masses){
		double wallDistance;
		double wallXForce = 0;
		double wallYForce = 0;
		
    	for(Mass curMass: masses){ 		// loop a list of movable Masses
    		if(myId == 1){   							// if top wall
    			wallDistance = Math.abs(curMass.getY() - WALL_MARGIN);
    			wallYForce = computeForce(wallDistance, POSITIVE_SIGN);
    		}
    		else if(myId == 2){  						// if right wall
        		wallDistance = Math.abs(curMass.getX() - (DISPLAY_WIDTH - WALL_MARGIN));
    			wallXForce = computeForce(wallDistance, NEGATIVE_SIGN);
    		}
    		else if(myId == 3){							// if bottom wall
        		wallDistance = Math.abs(curMass.getY() - (DISPLAY_HEIGHT - WALL_MARGIN));
    			wallYForce = computeForce(wallDistance, NEGATIVE_SIGN);
    		}
    		else if(myId == 4){							// if left wall
        		wallDistance = Math.abs(curMass.getX() - WALL_MARGIN);
    			wallXForce = computeForce(wallDistance, POSITIVE_SIGN);
    		}	
    		curMass.setForce(wallXForce, wallYForce);
    	}
	}
	
	private double computeForce(double distance, int sign){
		return sign * myMagnitude / Math.pow(distance, myExponent);
	}
	
	@Override
	public String toString(){
		if(myId == 1){   							// if top wall
			return "Top Repulsion";
		}
		else if(myId == 2){  						// if right wall
			return "Right Repulsion";
		}
		else if(myId == 3){							// if bottom wall
			return "Bot Repulsion";
		}
		else {										// if left wall
			return "Left Repulsion";
		}
	}
}
