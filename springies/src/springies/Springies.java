package springies;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;

import jboxGlue.PhysicalObject;
import jboxGlue.PhysicalObjectRect;
import jboxGlue.WorldManager;
import jgame.JGColor;
import jgame.JGFont;
import jgame.platform.JGEngine;
/**
 * @author Justin (Zihao) Zhang, Nick (Pengyi) Pan
 */
@SuppressWarnings("serial")
public class Springies extends JGEngine
{	
	private static final String ENVIRONMENT_FILENAME_DEFAULT = "assets/environment.xml";
	private static final String OBJECT_FILENAME_DEFAULT = "assets/daintywalker.xml";
	private static final double WALL_EXPONENT_DEFAULT = 1.0;
	private static final int WALL_MAGNITUDE_DEFAULT = 10;
	private static final int CENTER_MAGNITUDE_DEFAULT = 10;
	private static final double CENTER_EXPONENT_DEFAULT = 1.0;
	private static final double VISCOSITY_MAGNITUDE_DEFAULT = 0.2;
	private static final double GRAVITY_MAGNITUDE_DEFAULT = 5.0;
	private static final int GRAVITY_DIRECTION_DEFAULT = 90;
	private static double WALL_MARGIN_DEFAULT = 5;
	private static double WALL_THICKNESS_DEFAULT = 10;
	private static final int SPRING_WIDTH_DEFAULT = 5;
	private static final int MASS_RADIUS_DEFAULT = 5;
	private static int CHANGE_WALL_DEFAULT = 5;
	private static int CHANGE_AMPLITUDE_DEFAULT = 2;
	private static final int KEY_INCREASEAREASIZE = KeyUp, KEY_DECREASEAREASIZE = KeyDown;
	private static final int KEY_TRANSFORM_NUM = 48;
	private static final int KEY_LOADASSEMBLY = 'N';
	private static final int KEY_CLEAR = 'C';
	private static final int KEY_SETGRAVITY = 'G';
	private static final int KEY_SETVISCOSITY = 'V';
	private static final int KEY_SETCENTEROFMASS = 'M';
	private static final int KEY_SETTOPWALLREPULSION = KeyEvent.VK_1;
	private static final int KEY_SETRIGHTWALLREPULSION = KeyEvent.VK_2;
	private static final int KEY_SETBOTWALLREPULSION = KeyEvent.VK_3;
	private static final int KEY_SETLEFTWALLREPULSION = KeyEvent.VK_4;
	private static final int KEY_INCREASEAMPLITUDE = KeyEvent.VK_ADD; // same as the key '+'
	private static final int KEY_DECREASEAMPLITUDE = KeyEvent.VK_SUBTRACT; 
	
	private static final int POSITIVE_INDICATOR = 1;
	private static final int NEGATIVE_INDICATOR = -1;
	private static final int LEFT_KEY = 1;
	
	private double topWallViewX;
	private double topWallViewY;
	private double botWallViewX;
	private double botWallViewY;
	private double rightWallViewX;
	private double rightWallViewY;
	private double leftWallViewX;
	private double leftWallViewY;
	private double viscosityViewX;
	private double viscosityViewY;
	private double centerViewX;
	private double centerViewY;
	private double gravityViewX;
	private double gravityViewY;

	//above is all the default constant
	//below is all the global variable
	private double wallWidth;
	private double wallHeight;
    
	private double wallMargin;
	private double wallThickness;
	private int changeSizeWall;
	private int changeSizeAmplitude;
	
	private double centerExponent;
	private double centerMagnitude;
	
	private double viscosityMagnitude;
	
	private double gravityDirection;
	private double gravityMagnitude;
	
	private int topWallId;
	private int rightWallId;
	private int botWallId;
	private int leftWallId;
	private double topWallMagnitude;
	private double topWallExponent;
	private double rightWallMagnitude;
	private double rightWallExponent;
	private double botWallMagnitude;
	private double botWallExponent;
	private double leftWallMagnitude;
	private double leftWallExponent;
	public PhysicalObjectRect myTopWall;
	public PhysicalObjectRect myRightWall;
	public PhysicalObjectRect myBotWall;
	public PhysicalObjectRect myLeftWall;
	
	private int curAssemblyNumber;
	private String curAssemblyID;
	private JGColor curAssemblyColor;
	private ArrayList<Assembly> allAssemblies; 
	private ArrayList<PhysicalObject> currentAddAssembly;
	private ArrayList<JGColor> colorList;
	private HashMap<String,JGColor> allColorMap;
	

	private boolean addedMS = false;
	private String objectFileName = OBJECT_FILENAME_DEFAULT;
	private String environmentFileName = ENVIRONMENT_FILENAME_DEFAULT;

	private boolean dragMassExist;
	private DragMass myDragMass;
	private Spring myDragSpring;
	private ArrayList<Mass> movableMasses;
	private ArrayList<Force> allForces;
	
	private double massRadius;
	private double springWidth;
	
    public Springies ()
    {
        // set the window size
        int height = 480;
        double aspect = 16.0 / 9.0;
        initEngineComponent((int) (height * aspect), height);
    }

    @Override
    public void initCanvas ()
    {
        // I have no idea what tiles do...
        setCanvasSettings(1, // width of the canvas in tiles
                          1, // height of the canvas in tiles
                          displayWidth(), // width of one tile
                          displayHeight(), // height of one tile
                          null,// foreground colour -> use default colour white
                          null,// background colour -> use default colour black
                          null); // standard font -> use default font
    }

    // NOTE:
    //   world coordinates have y pointing down
    //   game coordinates have y pointing up
    // so gravity is up in world coords and down in game coords
    // so set all directions (e.g., forces, velocities) in world coords
    @Override
    public void initGame ()
    {
               
        setFrameRate(60, 2);
        initVariables();
        environmentFileName = chooseFile("Choose the environment setting xml file");
        addObjects(environmentFileName,"environment"); 
        WorldManager.initWorld(this);        
        addWalls();
        objectFileName = chooseFile("Choose assembly xml file");
        addObjects(objectFileName,"nodes");
        allForces.add(new Gravity(gravityMagnitude, gravityDirection));
        allForces.add(new Viscosity(viscosityMagnitude));
        allForces.add(new CenterOfMass(centerMagnitude, centerExponent));
        allForces.add(new WallRepulsion(topWallId, topWallMagnitude, topWallExponent));
        allForces.add(new WallRepulsion(botWallId, botWallMagnitude, botWallExponent));
        allForces.add(new WallRepulsion(rightWallId, rightWallMagnitude, rightWallExponent));
        allForces.add(new WallRepulsion(leftWallId, leftWallMagnitude, leftWallExponent));
        setForceView();
    }
    
    private void initVariables() {
		//setting the default of variable if environment file is not read
		dragMassExist = false;
	    addedMS = false;
	    allAssemblies = new ArrayList<Assembly>();
	    currentAddAssembly = new ArrayList<PhysicalObject>();
	    curAssemblyNumber = 0;
	    curAssemblyID = "A" + curAssemblyNumber;
	    colorList = new ArrayList<JGColor>(Arrays.asList(JGColor.white));
	    curAssemblyColor = colorList.get(curAssemblyNumber%colorList.size());
	    movableMasses = new ArrayList<Mass>();
	    allForces = new ArrayList<Force>();
	    
		gravityDirection = GRAVITY_DIRECTION_DEFAULT;
		gravityMagnitude = GRAVITY_MAGNITUDE_DEFAULT;
		viscosityMagnitude = VISCOSITY_MAGNITUDE_DEFAULT;
		centerExponent = CENTER_EXPONENT_DEFAULT;
		centerMagnitude = CENTER_MAGNITUDE_DEFAULT;    	
		topWallMagnitude = WALL_MAGNITUDE_DEFAULT;
		topWallExponent = WALL_EXPONENT_DEFAULT;
		botWallMagnitude = WALL_MAGNITUDE_DEFAULT;
		botWallExponent = WALL_EXPONENT_DEFAULT;
		rightWallMagnitude = WALL_MAGNITUDE_DEFAULT;
		rightWallExponent = WALL_EXPONENT_DEFAULT;
		leftWallMagnitude = WALL_MAGNITUDE_DEFAULT;
		leftWallExponent = WALL_EXPONENT_DEFAULT;
		wallMargin = WALL_MARGIN_DEFAULT;
		wallThickness = WALL_THICKNESS_DEFAULT;
		changeSizeWall = CHANGE_WALL_DEFAULT;
		changeSizeAmplitude = CHANGE_AMPLITUDE_DEFAULT;
		massRadius = MASS_RADIUS_DEFAULT;
		springWidth = SPRING_WIDTH_DEFAULT;
		
		//setting the display location of text
		topWallViewX = 50;
		topWallViewY = displayHeight() - 30;
		rightWallViewX = 250;
		rightWallViewY = viewHeight() - 30;
		botWallViewX = 450;
		botWallViewY = viewHeight() - 30;
		leftWallViewX = 650;
		leftWallViewY = viewHeight() - 30;
		viscosityViewX = 80;
		viscosityViewY = 1;
		centerViewX = 380;
		centerViewY = 1;
		gravityViewX = 680;
		gravityViewY = 1;
		
		topWallId = 1;
		rightWallId = 2;
		botWallId = 3;
		leftWallId = 4;
	
		allColorMap = new HashMap<String,JGColor>();
		allColorMap.put("blue",JGColor.blue);
		allColorMap.put("cyan",JGColor.cyan);
		allColorMap.put("black",JGColor.black);
		allColorMap.put("gray",JGColor.gray);
		allColorMap.put("green",JGColor.green);
		allColorMap.put("grey",JGColor.grey);
		allColorMap.put("magenta",JGColor.magenta);
		allColorMap.put("orange",JGColor.orange);
		allColorMap.put("pink",JGColor.pink);
		allColorMap.put("red",JGColor.red);
		allColorMap.put("white",JGColor.white);
		allColorMap.put("yellow",JGColor.yellow);
	}

	public String chooseFile(String displayText){
    	try{
    		JFileChooser chooser = new JFileChooser();
            UIManager.put("FileChooser.openDialogTitleText", displayText);
            SwingUtilities.updateComponentTreeUI(chooser);
            System.out.println(displayText);
            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "xml files", "xml");
            chooser.setFileFilter(filter);
            int returnVal = chooser.showOpenDialog(getParent());
            if(returnVal == JFileChooser.APPROVE_OPTION) {
               System.out.println("You chose to open this file: " +
                    chooser.getSelectedFile().getPath());
            }
            return chooser.getSelectedFile().getPath();
    	}catch(Exception e){
			System.out.println("Please select a valid XML file!");
			throw new NullPointerException();
    	}
    	}
    
    private void setForceView(){
    	for(Force force: allForces){
    		if(force.toString().equals("Viscosity")){
    			force.setIndicationPosition(viscosityViewX, viscosityViewY);
    		}
    		else if(force.toString().equals("Gravity")){
    			force.setIndicationPosition(gravityViewX, gravityViewY);
    		}
    		else if(force.toString().equals("Center of Mass")){
    			force.setIndicationPosition(centerViewX, centerViewY);
    		}
    		else if(force.toString().equals("Top Repulsion")){
    			force.setIndicationPosition(topWallViewX, topWallViewY);
    		}
    		else if(force.toString().equals("Bot Repulsion")){
    			force.setIndicationPosition(botWallViewX, botWallViewY);
    		}
    		else if(force.toString().equals("Right Repulsion")){
    			force.setIndicationPosition(rightWallViewX, rightWallViewY);
    		}
    		else if(force.toString().equals("Left Repulsion")){
    			force.setIndicationPosition(leftWallViewX, leftWallViewY);
    		}
    	}
    }

	public void addObjects(String filename,String type){
    	ReadFileMakeObjects doc = new ReadFileMakeObjects(this);
    	doc.parse(filename,type);
    }
  
    public void addNodes(String id, HashMap<String,Double> callParaMap,boolean fixedMass){
		Mass aMass = null;
		if(fixedMass){
			aMass = new FixedMass(id + curAssemblyID, callParaMap.get("x"), callParaMap.get("y"),curAssemblyColor,massRadius);
		}
		else if(callParaMap.size()==2){
			aMass = new Mass(id + curAssemblyID, callParaMap.get("x"), callParaMap.get("y"),curAssemblyColor,massRadius);   	        
	    }
		else if(callParaMap.size()==4){
			aMass = new Mass(id + curAssemblyID, callParaMap.get("x"), callParaMap.get("y"), callParaMap.get("vx"), callParaMap.get("vy"),curAssemblyColor,massRadius);
		}
		else if(callParaMap.size()==5){
			aMass = new Mass(id + curAssemblyID, callParaMap.get("x"), callParaMap.get("y"), callParaMap.get("mass"), callParaMap.get("vx"), callParaMap.get("vy"),curAssemblyColor,massRadius);
		}
		aMass.setPos(aMass.getInitX(),aMass.getInitY());
		currentAddAssembly.add(aMass);
	}

	public void addLinks(String linkName,HashMap<String,Double> callParaMap,String a, String b,boolean muscle){
		Spring aSpring = null;
		if(muscle){
			aSpring = new Muscle(linkName + curAssemblyID, getMassById(a+curAssemblyID), getMassById(b+curAssemblyID), callParaMap.get("amplitude"),springWidth);     		
		}
		else if(callParaMap.size()==2){
			aSpring = new Spring(linkName + curAssemblyID, getMassById(a+curAssemblyID), getMassById(b+curAssemblyID), callParaMap.get("restlength"), callParaMap.get("constant"),springWidth);     		
	    }
		else if(callParaMap.size()==1){
			aSpring = new Spring(linkName + curAssemblyID, getMassById(a+curAssemblyID), getMassById(b+curAssemblyID), callParaMap.get("restlength"),springWidth);     		
	    }
		else if(callParaMap.size()==0){
			aSpring = new Spring(linkName + curAssemblyID, getMassById(a+curAssemblyID), getMassById(b+curAssemblyID),springWidth);     		
		}
		aSpring.setPos(aSpring.getInitX(), aSpring.getInitY());
		currentAddAssembly.add(aSpring);
	}

	private void addWalls ()
	{
	    // add walls to bounce off of NOTE: immovable objects must have no mass
	    wallWidth = displayWidth();
	    wallHeight = displayHeight();
	    
	    myTopWall = new PhysicalObjectRect("wall1", 2, JGColor.blue, wallWidth, wallThickness); //top   
	    myTopWall.setPos(displayWidth() / 2, wallMargin);
	    myBotWall = new PhysicalObjectRect("wall3", 2, JGColor.blue, wallWidth, wallThickness); //bot
	    myBotWall.setPos(displayWidth() / 2, displayHeight()-wallMargin);
	    myLeftWall = new PhysicalObjectRect("wall4", 2, JGColor.blue, wallThickness, wallHeight);  //left      
	    myLeftWall.setPos(wallMargin, displayHeight() / 2);
	    myRightWall = new PhysicalObjectRect("wall2", 2, JGColor.blue, wallThickness, wallHeight);  //right  
	    myRightWall.setPos(displayWidth() - wallMargin, displayHeight() / 2);
	    }

	private Mass getMassById(String id){
    	Object thisObject = this.getObject(id);
    	Mass returnObject = (Mass) thisObject;
    	return returnObject;
    }
        
    public void setEnvironment(HashMap<String,Double> callParaMap,String type){
    	//setting multiple instance variables read from xml
    	if(type.equals("gravity")){
    		gravityDirection = callParaMap.get("direction");
    		gravityMagnitude = callParaMap.get("magnitude");
    	}
    	else if(type.equals("viscosity")){
    		viscosityMagnitude = callParaMap.get("magnitude");
    	}
    	else if(type.equals("centermass")){
    		centerExponent = callParaMap.get("exponent");
    		centerMagnitude = callParaMap.get("magnitude");
    	}
    	else if(type.equals("wall")){
    		int wallId = callParaMap.get("id").intValue();
    		if(wallId == topWallId){
    			topWallMagnitude = callParaMap.get("magnitude");
    	    	topWallExponent = callParaMap.get("exponent");
    		}
    		else if(wallId == rightWallId){
    			rightWallMagnitude = callParaMap.get("magnitude");
    	    	rightWallExponent = callParaMap.get("exponent");
    		}
    		else if(wallId == botWallId){
    			botWallMagnitude = callParaMap.get("magnitude");
    	    	botWallExponent = callParaMap.get("exponent");
    		}
    		else if(wallId == leftWallId){
    			leftWallMagnitude = callParaMap.get("magnitude");
    	    	leftWallExponent = callParaMap.get("exponent");
    		}
    	}
    	else if(type.equals("color")){
    		colorList = new ArrayList<JGColor>(Collections.nCopies(callParaMap.size(), JGColor.black)); 
    		for(String color:callParaMap.keySet()){
    			colorList.set(callParaMap.get(color).intValue(), allColorMap.get(color));
    		}
    	}
    	else if(type.equals("changeSize")){
    		changeSizeWall = callParaMap.get("wall").intValue();
        	changeSizeAmplitude = callParaMap.get("amplitude").intValue();
    	}
    	else if(type.equals("wallData")){
    		wallMargin = callParaMap.get("margin").intValue();
        	wallThickness = callParaMap.get("thickness").intValue();
    	}
    	else if(type.equals("size")){
    		massRadius = callParaMap.get("massRadius");
        	springWidth = callParaMap.get("springWidth");
    	}
    }

    private void checkMouseDragging(){
    	if(!getMouseButton(LEFT_KEY) && dragMassExist){ // not dragging, remove the mass
    		myDragMass.remove();
    		myDragSpring.remove();
    		dragMassExist = false;
    	}
    	else if (getMouseButton(LEFT_KEY)&& !dragMassExist){ // first click, create the mass
    		myDragMass = new DragMass(getMouseX(),getMouseY(),massRadius);
    		myDragMass.setPos(myDragMass.getInitX()+Mass.MASS_RADIUS,myDragMass.getInitY()+Mass.MASS_RADIUS);
    		Mass nearestMass = findNearestMass(getMouseX(),getMouseY());
    		if (nearestMass == null) return;
    		double restLength = Math.sqrt(Math.pow(nearestMass.getX()-getMouseX(), 2) + Math.pow(nearestMass.getY()-getMouseY(), 2));
    		myDragSpring = new Spring("dragSpring",nearestMass, myDragMass,restLength,springWidth);
    		myDragSpring.setPos(myDragSpring.getInitX(), myDragSpring.getInitY());
    		dragMassExist = true;
    	}
    	else if (getMouseButton(LEFT_KEY)&& dragMassExist){ // dragging, refresh the position
    		myDragMass.setPos(getMouseX() + Mass.MASS_RADIUS, getMouseY() + Mass.MASS_RADIUS);
    	}	
    }
    
    private Mass findNearestMass(double xPos, double yPos){
    	Mass nearestMass = null;
    	double curMin = Double.MAX_VALUE;
    	for(Mass curMass: movableMasses){
    		double distance = Math.sqrt(Math.pow(curMass.getX()-xPos, 2) + Math.pow(curMass.getY()-yPos, 2));
    		if (distance<curMin){
    			nearestMass = curMass;
    			curMin = distance;
    		}
    	}
    	return nearestMass;
    }
    
    private void checkOutOfBorder(){
    	double rightBoundary = myRightWall.x - wallThickness/2;
    	double leftBoundary = myLeftWall.x + wallThickness/2;
    	double topBoundary = myTopWall.y + wallThickness/2;
    	double botBoundary = myBotWall.y - wallThickness/2;
    	double radius = Mass.MASS_RADIUS;
    	
    	for(Mass checkMass:movableMasses){
    		if (checkMass.getX() >= rightBoundary){
        		checkMass.setPos(rightBoundary-radius, checkMass.getY());
        	}
        	if (checkMass.getX() <= leftBoundary){
        		checkMass.setPos(leftBoundary+radius, checkMass.getY());
        	}
        	if (checkMass.getY() >= botBoundary){
        		checkMass.setPos(checkMass.getX(), botBoundary-radius);
        	}
        	if (checkMass.getY() <= topBoundary){
        		checkMass.setPos(checkMass.getX(), topBoundary+radius);
        	}
    	}
    }
    
    private void checkWallAreaKeys(){ 
		// increase the size of the walled area by CHANGE_SIZE_PIXELS
		if(getKey(KEY_INCREASEAREASIZE)){ 
			changeWallAreaSize(POSITIVE_INDICATOR); 
			clearKey(KEY_INCREASEAREASIZE);
		}
		// decrease the size of the walled area by CHANGE_SIZE_PIXELS
		if(getKey(KEY_DECREASEAREASIZE)){ 
			changeWallAreaSize(NEGATIVE_INDICATOR); 
			clearKey(KEY_DECREASEAREASIZE);
		}
	}

	private void changeWallAreaSize(int signIndicator) {
		myTopWall.setPos(myTopWall.x, myTopWall.y - signIndicator * changeSizeWall);
		myTopWall.setWidth(myTopWall.getWidth() + signIndicator * changeSizeWall * 2);
		myBotWall.setPos(myBotWall.x, myBotWall.y + signIndicator * changeSizeWall);
		myBotWall.setWidth(myBotWall.getWidth() + signIndicator * changeSizeWall * 2);
		myRightWall.setPos(myRightWall.x + signIndicator * changeSizeWall, myRightWall.y);
		myRightWall.setHeight(myRightWall.getHeight() + signIndicator * changeSizeWall * 2);
		myLeftWall.setPos(myLeftWall.x - signIndicator * changeSizeWall, myLeftWall.y);
		myLeftWall.setHeight(myLeftWall.getHeight() + signIndicator * changeSizeWall * 2);
	}

	private void checkEnvironmentKeys() {
		toggleForce(KEY_SETGRAVITY, gravityMagnitude, false);			// toggle on or off the gravity
		toggleForce(KEY_SETVISCOSITY, viscosityMagnitude, false); 		// toggle on or off the viscosity
		toggleForce(KEY_SETCENTEROFMASS, centerMagnitude, false); 		// toggle on or off the Center of Mass forces
	}

	private void checkAssemblyKeys() {
		if(getKey(KEY_LOADASSEMBLY)){
			loadNewAssembly();
			clearKey(KEY_LOADASSEMBLY);
		}
		if(getKey(KEY_CLEAR)){
			clearAllAssemblies();
			clearKey(KEY_CLEAR);
		}
	}

	private void checkAmplitudeKeys() {
		// increase the amplitude of all muscles by CHANGE_AMPLITUDE
		if(getKey(KEY_INCREASEAMPLITUDE)){
			changeAmplitude(POSITIVE_INDICATOR);
			clearKey(KEY_INCREASEAMPLITUDE);
		}
		// decrease the amplitude of all muscles by CHANGE_AMPLITUDE
		if(getKey(KEY_DECREASEAMPLITUDE)){
			changeAmplitude(NEGATIVE_INDICATOR);
			clearKey(KEY_DECREASEAMPLITUDE);
		}
	}
	
	// toggle on or off the according wall repulsion force
	private void checkWallRepulsionKeys() {
		toggleForce(KEY_SETTOPWALLREPULSION, topWallMagnitude, true);
		toggleForce(KEY_SETRIGHTWALLREPULSION, rightWallMagnitude, true);
		toggleForce(KEY_SETBOTWALLREPULSION, botWallMagnitude, true);
		toggleForce(KEY_SETLEFTWALLREPULSION, leftWallMagnitude, true);
	}
	
	private void toggleForce(int key, double magnitude, boolean wallRepulsion){
		if(getKey(key)){
			int curID = key;
			if(wallRepulsion) curID = key - KEY_TRANSFORM_NUM;
			for(Force force: allForces){
				if(force.getID() == curID){
					if(force.ifForceOn()){ force.setMagnitude(0); }
					else{ force.setMagnitude(magnitude); }	
					break; 								// only one force will be matched at a time
				}
			}
			clearKey(key);
		}
	}
    
    private void changeAmplitude(int signIndicator){
    	for(Assembly curAssembly: allAssemblies){
    		for(Spring current: curAssembly.getSprings()){
    			if(current.toString().equals("Muscle")){
    				Muscle cur = (Muscle) current;
    				cur.setAmplitude(changeSizeAmplitude * signIndicator);
    			}
    		}
    	}
    }

	private void showIndication(Force force){
		if(force.ifForceOn()){
			drawString(force.toString() + " On", force.getIndicationX(), force.getIndicationY(), -1);
		}
		else{
			drawString(force.toString() + " Off", force.getIndicationX(), force.getIndicationY(), -1);
		}
	}

	// If the user presses 'N", then load a new assembly that has its own center of mass
    public void loadNewAssembly(){
    	objectFileName = chooseFile("Choose assembly xml file");
        addObjects(objectFileName,"nodes");
		addedMS = false;
    }
    
    private void finishCurrentAssembly() {
		allAssemblies.add(new Assembly(currentAddAssembly)); // save the current assembly
		currentAddAssembly.clear();
		curAssemblyNumber ++;
		curAssemblyID = "A" + curAssemblyNumber;
		curAssemblyColor = colorList.get(curAssemblyNumber%colorList.size());
	}

	// If the user presses 'C", then clear all loaded assemblies
    private void clearAllAssemblies(){
    	for(Assembly current: allAssemblies){
    		current.removeGameObjects();
    	}
    }

	@Override
	public void paintFrame(){
		setFont(new JGFont("1", JGFont.PLAIN, 12));
		for(Force force: allForces){
			showIndication(force);
		}
	}

	@Override
	public void doFrame ()
	{
		if(!addedMS){
			addObjects(objectFileName,"links");
			addedMS = true;
	        finishCurrentAssembly();
		}
	    // update game objects
	    WorldManager.getWorld().step(1f, 1);
	    moveObjects();
	    checkCollision(2, 1);
	
		movableMasses.clear();
		for(Assembly current: allAssemblies){
			for(Mass mass: current.getMasses()){
	    		movableMasses.add(mass);
			}
	    	for(Force force: allForces){
	    		force.applyForce(current.getMasses());
	    	}
		}
		//check key by category, easier to control
		checkMouseDragging();
		checkAssemblyKeys();
		checkWallAreaKeys();
		checkWallRepulsionKeys();
		checkAmplitudeKeys();
		checkEnvironmentKeys();
		checkOutOfBorder();
	}

}
