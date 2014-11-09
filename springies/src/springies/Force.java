package springies;

import java.util.ArrayList;
/**
 * @author Justin (Zihao) Zhang, Nick (Pengyi) Pan
 */
public abstract class Force {
	protected static final int DEFAULT_EXPONENT = 0;
	
	protected double indication_X;
	protected double indication_Y;
	protected int myId;
	protected double myMagnitude;
	protected double myExponent;
	
	public Force(int id, double magnitude, double exponent){
		myId = id;
		myMagnitude = magnitude;
		myExponent = exponent;
	}
	
	public void setIndicationPosition(double x, double y){
		indication_X = x;
		indication_Y = y;
	}
	
	public double getIndicationX(){
		return indication_X;
	}
	
	public double getIndicationY(){
		return indication_Y;
	}
	
	public Force(int id, double magnitude){
		this(id, magnitude, DEFAULT_EXPONENT);
	}
	
	public abstract void applyForce(ArrayList<Mass> masses);
	
	public void setMagnitude(double magnitude){
		myMagnitude = magnitude;
	}
	
	public void setExponent(double exponent){
		myExponent = exponent;
	}
	
	public double getMagnitude(){
		return myMagnitude;
	}
	
	public double getExponent(){
		return myExponent;
	}
	
	public boolean ifForceOn(){
		return (myMagnitude != 0);
	}
	
	public int getID(){
		return myId; 
	}
	
	public String toString(){
		return "Force";
	}
}
