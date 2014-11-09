package springies;

import java.util.ArrayList;

import org.jbox2d.common.Vec2;
/**
 * @author Justin (Zihao) Zhang, Nick (Pengyi) Pan
 */
public class Viscosity extends Force{
	private static final int VIS_ID = 'V';
	
	public Viscosity (double magnitude){
		super(VIS_ID, magnitude);
	}

	@Override
	public void applyForce(ArrayList<Mass> masses) {
    	for(Mass curMass: masses){ 		// loop a list of movable Masses
    		Vec2 velocity = curMass.getBody().getLinearVelocity();
    		curMass.setForce(-myMagnitude * velocity.x, -myMagnitude * velocity.y);
    	}
	}
	
	@Override
	public String toString(){
		return "Viscosity";
	}
}
