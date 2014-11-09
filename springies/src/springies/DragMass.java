package springies;

import jgame.JGColor;
import jgame.JGObject;
/**
 * This class is used to create an invisible mass at the position of cursor 
 * when mouse button are hit. This mass moves with cursor when dragging.
 *
 * @author Justin (Zihao) Zhang, Nick (Pengyi) Pan
 */

public class DragMass extends Mass {
	private static String DRAG_ID = "dragMass";
	private static final JGColor DRAGMASS_COLOR = JGColor.black; //should be the same as background

	public DragMass(double xPos, double yPos,double radius) {
		super(DRAG_ID, xPos, yPos,0,DRAGMASS_COLOR,radius);
	}
		
	@Override
	public void hit (JGObject other)
    {
		//do nothing           
    }

}
