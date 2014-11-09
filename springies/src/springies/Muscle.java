package springies;

import jgame.JGColor;

/**
 * @author Justin (Zihao) Zhang, Nick (Pengyi) Pan
 */
public class Muscle extends Spring {
	protected double timer;
	private double myAmplitude;
	private static final double INITIAL_TIMER = 0;
	private static final double INCREAMENT_TIMER = 0.01;
	private static final JGColor POSITIVE_COLOR = JGColor.orange;
	private static final JGColor NEGATIVE_COLOR = JGColor.yellow;

	public Muscle(String id, Mass left, Mass right, double restlength, double constant, double amplitude,double springWidth) {
		super(id, left, right, restlength, constant,springWidth);
		timer = INITIAL_TIMER;
		myAmplitude = amplitude;
	}
	
	// restlength not given; use default restlength
	public Muscle(String id, Mass left, Mass right, double amplitude,double springWidth) {
		this(id, left, right, computeDistance(left.getInitX(), left.getInitY(), right.getInitX(), right.getInitY()) - left.getRadius() - right.getRadius(), DEFAULT_CONSTANT, amplitude,springWidth);
	}
	
    @Override
	public String toString(){
		return "Muscle";
	}
	
	public void setAmplitude(double change){
		if(myAmplitude >= Math.abs(change)){
			myAmplitude += change;	
		}
	}
	
	@Override
	public void undateColor(){
		if(Math.sin(timer)>=0){
			curColor = POSITIVE_COLOR;
		}
		if(Math.sin(timer)<0){
			curColor = NEGATIVE_COLOR;
		}
	}
	
    @Override
	public void compressOrExtend(){
    	super.compressOrExtend();
    	timer += INCREAMENT_TIMER;
    	double restLengthDiff = myAmplitude * Math.sin(timer);
    	myRestlength += restLengthDiff;
	}
}
