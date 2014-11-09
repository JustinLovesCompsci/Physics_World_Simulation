package springies;

import java.util.ArrayList;
/**
 * @author Justin (Zihao) Zhang, Nick (Pengyi) Pan
 */
public class CenterOfMass extends Force {
	private static final int CENTER_ID = 'M';
	
	public CenterOfMass(double magnitude, double exponent){
		super(CENTER_ID, magnitude, exponent);
	}
	
	@Override
	public void applyForce(ArrayList<Mass> masses) {
    	double xSum = 0;
    	double ySum = 0;
    	double massSum = 0; 				// Sum of Masses
    	
    	for(Mass curMass: masses){ 		// calculate xSum, ySum, massSum
    		xSum += curMass.getX() * curMass.getMass();
    		ySum += curMass.getY() * curMass.getMass();
    		massSum += curMass.getMass();
    	}
 
    	double xCenter = xSum / massSum;
    	double yCenter = ySum / massSum;
    	
    	for(Mass curMass: masses){
    		dragMass(curMass, xCenter, yCenter);
    	}
	}
	
	public void dragMass(Mass mass, double xCenter, double yCenter){
		double distance = Math.sqrt(Math.pow(mass.getX() - xCenter, 2) + Math.pow(mass.getY() - yCenter, 2));
		if(distance == 0){return;}
		
		double force = myMagnitude / Math.pow(distance, myExponent);
		double degree = Math.acos(Math.abs((mass.getX() - xCenter) / distance));
		double dragXF = Math.cos(degree) * force;
		double dragYF = Math.sin(degree) * force;

		if(mass.getX() > xCenter){ 						// Mass is right to Center
			if(mass.getY() > yCenter){ mass.setForce(-dragXF, -dragYF); }
			else if(mass.getY() < yCenter){ mass.setForce(-dragXF, dragYF); }
		}
		else{											// Mass is left to or at xCenter
			if(mass.getY() > yCenter){ mass.setForce(dragXF, -dragYF); }
			else if(mass.getY() < yCenter){ mass.setForce(dragXF, dragYF); }
		}
	}
	
	@Override
	public String toString(){
		return "Center of Mass";
	}
}
