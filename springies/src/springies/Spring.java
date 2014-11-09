package springies;

import jboxGlue.PhysicalObjectRect;
import jgame.JGColor;
import jgame.JGObject;
/**
 * @author Justin (Zihao) Zhang, Nick (Pengyi) Pan
 */
public class Spring extends PhysicalObjectRect {
	protected double myRestlength;
	protected double myConstant;
	protected Mass myLeft;
	protected Mass myRight;
	protected double initX;
	protected double initY;
	protected double middleX;
	protected double middleY;
	protected JGColor curColor = SPRING_COLOR;
	
	protected static final JGColor SPRING_COLOR = JGColor.yellow;
	protected static final JGColor SPRING_COMPRESS_COLOR = JGColor.red;
	protected static final JGColor SPRING_EXTEND_COLOR = JGColor.green;
	protected static final int SPRING_CID = 3;
	protected static final double DEFAULT_CONSTANT = 1;
	protected double SPRING_WIDTH = 5;
	
	protected double myLeftRadius;
	protected double myRightRadius;
	protected double radiusBetween;

	public Spring(String id, Mass left, Mass right, double restlength, double constant,double springWidth) {
        super(id, SPRING_CID, SPRING_COLOR, 0, 0);
		myRestlength = restlength;
		myConstant = constant;
		myLeft = left;
		myRight = right;
		myLeftRadius = left.getRadius(); 
		myRightRadius = right.getRadius(); 
		radiusBetween = myLeftRadius + myRightRadius;
		myWidth = computeDistance(left.getInitX(), left.getInitY(), right.getInitX(), right.getInitY()) - radiusBetween;
    	initX = 0;
    	initY = 0;
    	myHeight = springWidth;
	}
	
	// restlength and constant not given; use default restlength and constant
	public Spring(String id, Mass left, Mass right,double springWidth) {
		this(id, left, right, computeDistance(left.getInitX(), left.getInitY(), right.getInitX(), right.getInitY()) - left.getRadius() - right.getRadius(), DEFAULT_CONSTANT,springWidth);
	}
	
	public Spring(String id, Mass left, Mass right, double restlength,double springWidth) {
		this(id, left, right, restlength, DEFAULT_CONSTANT,springWidth);
	}
	
    public static double computeDistance(double x1, double y1, double x2, double y2){
		return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
	}

	@Override
    public void move ()
    {
    	super.move();
        adjustWidth(myLeft, myRight);
        setMiddlePos();
        compressOrExtend();
        undateColor();
    }
    
    // constantly set x and y coordinates of spring
    public void setMiddlePos(){
    	middleX = (myLeft.getX()+myRight.getX())/2;
    	middleY = (myLeft.getY()+myRight.getY())/2;
    }
	
    // constantly adjust the length of spring
	public void adjustWidth(Mass left, Mass right){
		myWidth = computeDistance(myLeft.getX(), myLeft.getY(), myRight.getX(), myRight.getY()) - radiusBetween;
	}
	
	// check if compressed or extended, and then act accordingly
	public void compressOrExtend(){
		if(myWidth == 0) return;						// make sure denominator is non-zero
		if(!ifCompressed() && !ifExtended()){			// return if neither compressed or extended
			return;
		}
		double force = computeAbsoluteForce();
		double sinForce = computeAbsoluteSinForce(force);
		double cosForce = computeAbsoluteCosForce(force);
		
		if(( firstOrSecondQuadrant() && leftRightReversed() && ifCompressed() ) || ( !firstOrSecondQuadrant() && !leftRightReversed() && ifExtended() )){
			setAxisOppositeForce(cosForce, sinForce);
		}
		else if(( firstOrSecondQuadrant() && leftRightReversed() && ifExtended() ) || ( !firstOrSecondQuadrant() && !leftRightReversed() && ifCompressed() )){
			setAxisOppositeForce(-cosForce, -sinForce);
		}
		else if(( firstOrSecondQuadrant() && !leftRightReversed() && ifCompressed() ) || ( !firstOrSecondQuadrant() && leftRightReversed() && ifExtended() )){
			setDiagonalOppositeForce(cosForce, sinForce);
		}
		else if(( firstOrSecondQuadrant() && !leftRightReversed() && ifExtended() ) || ( !firstOrSecondQuadrant() && leftRightReversed() && ifCompressed() )){
			setDiagonalOppositeForce(-cosForce, -sinForce);
		}
	}

	
	public double computeAbsoluteForce(){ return Math.abs(myConstant * myWidth - myRestlength); }
	
	public double computeAbsoluteSinForce(double force){
		double sinAngle = Math.abs(myLeft.getY() - myRight.getY()) / myWidth;
		return sinAngle * force;
	}
	
	public double computeAbsoluteCosForce(double force){
		double cosAngle = Math.abs(myLeft.getX() - myRight.getX()) / myWidth;
		return cosAngle * force;
	}
	
	public boolean ifCompressed(){ return (myWidth - myRestlength) < 0; }
	
	public boolean ifExtended(){ return (myWidth - myRestlength) > 0; }
	
	public boolean firstOrSecondQuadrant(){ return myLeft.getY() >= myRight.getY(); }
	
	public boolean leftRightReversed(){ return myLeft.getX() >= myRight.getX(); }
	
	public void setAxisOppositeForce(double cosForce, double sinForce){
		myRight.setForce(-cosForce, -sinForce);
		myLeft.setForce(cosForce, sinForce);
	}
	
	public void setDiagonalOppositeForce(double cosForce, double sinForce){
		myRight.setForce(cosForce, -sinForce);
		myLeft.setForce(-cosForce, sinForce);
	}
	
	public void undateColor(){
		if(ifCompressed()){
			curColor = SPRING_COMPRESS_COLOR;
		}
		if(ifExtended()){
			curColor = SPRING_EXTEND_COLOR;
		}
	}
		
    @Override
    public void setForce (double x, double y)
    {
        // do nothing
    }
    
    @Override
	public void hit (JGObject other){
    	// do nothing
    }
    
    @Override
    public String toString(){
    	return "Spring";
    }
	
	public double getInitX(){ return initX; }
	
	public double getInitY(){ return initY; }
	
	public double getX(){ return middleX; }
	
	public double getY(){ return middleY; }
	
    @Override
    public void paintShape () 
    {   	
    	// Part of the codes below were copied from PhysicalObjectRect class with some minor and necessaray changes
        myRotation = (float) Math.atan((-(myRight.getY()-myLeft.getY())/(myRight.getX()-myLeft.getX())));
        if (myPolyx == null || myPolyy == null)
        {
            // allocate memory for the polygon
            myPolyx = new double[4];
            myPolyy = new double[4];
        }
        // draw a rotated polygon
        myEngine.setColor(curColor);
        double cos = Math.cos(myRotation);
        double sin = Math.sin(myRotation);
        double halfWidth = myWidth / 2;
        double halfHeight = myHeight / 2;
        myPolyx[0] = (int) (middleX - halfWidth * cos - halfHeight * sin);
        myPolyy[0] = (int) (middleY + halfWidth * sin - halfHeight * cos);
        myPolyx[1] = (int) (middleX + halfWidth * cos - halfHeight * sin);
        myPolyy[1] = (int) (middleY - halfWidth * sin - halfHeight * cos);
        myPolyx[2] = (int) (middleX + halfWidth * cos + halfHeight * sin);
        myPolyy[2] = (int) (middleY - halfWidth * sin + halfHeight * cos);
        myPolyx[3] = (int) (middleX - halfWidth * cos + halfHeight * sin);
        myPolyy[3] = (int) (middleY + halfWidth * sin + halfHeight * cos);
        myEngine.drawPolygon(myPolyx, myPolyy, null, 4, true, true);
    }
}


