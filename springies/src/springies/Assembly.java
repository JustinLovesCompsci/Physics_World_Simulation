package springies;

import java.util.ArrayList;
import jboxGlue.PhysicalObject;
/**
 * @author Justin (Zihao) Zhang, Nick (Pengyi) Pan
 */
public class Assembly {
	private ArrayList<Mass> myMassList;
	private ArrayList<Spring> mySpringList;
	
	public Assembly(ArrayList<PhysicalObject> objectList){
		myMassList = new ArrayList<Mass>();
		mySpringList = new ArrayList<Spring>();
		
		for(int i = 0; i < objectList.size(); i ++){
			PhysicalObject curObject = objectList.get(i);
			if(curObject.getName().startsWith("m")){ // the current object is either a Mass of Fixed Mass
				Mass addObject = (Mass)curObject;
				myMassList.add(addObject);
			}
			else if(curObject.getName().startsWith("s")){ // the current object is either a Spring of Muscle
				Spring addObject = (Spring)curObject;
				mySpringList.add(addObject);
			}
		}
	}
	
	public ArrayList<Mass> getMasses(){
		return myMassList;
	}
	
	public ArrayList<Spring> getSprings(){
		return mySpringList;
	}
	
	// remove the assembly
	public void removeGameObjects(){
		for(Mass mass: myMassList){
			mass.remove();
		}
		for(Spring spring: mySpringList){
			spring.remove();
		}
		myMassList.clear();
		mySpringList.clear();
	}

}
