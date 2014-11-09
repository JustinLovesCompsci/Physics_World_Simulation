=======
Springies
=========

**Class Tree Design:**

abstract class **Nodes:**  
&nbsp;&nbsp;&nbsp;&nbsp;instance variable: id ; x; y;
<br>&nbsp;&nbsp;&nbsp;&nbsp;move() // move the object
<br>&nbsp;&nbsp;&nbsp;&nbsp;hit() // called by collisions
<br>&nbsp;&nbsp;&nbsp;&nbsp;isAttachSpring() // return true if attached by springs
<br>&nbsp;&nbsp;&nbsp;&nbsp;isAttachMuscle() // return true if attached by muscles
<br>&nbsp;&nbsp;&nbsp;&nbsp;getSpring() // return the spring attached to it
<br>&nbsp;&nbsp;&nbsp;&nbsp;getMuscle() // return the muscle attached to it
<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;class **MovingMass**: 
<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;vx; vy;
<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;vx; vy; mass;
<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;follow(double sx, double sy); // moves in response to the spring/muscle attached; called by move()
<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;class **FixedMass**: 
<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;//just call super(id, x, y) (default constructor)


abstract class **Links:**  
&nbsp;&nbsp;&nbsp;&nbsp;instance variable:a; b; 
<br>&nbsp;&nbsp;&nbsp;&nbsp;move() // move the object
<br>&nbsp;&nbsp;&nbsp;&nbsp;hit() // called by collisions
<br>&nbsp;&nbsp;&nbsp;&nbsp;getMasses() // return the two masses attached to the spring
<br>&nbsp;&nbsp;&nbsp;&nbsp;connect(Mass a, Mass b) // connect with two masses
<br>&nbsp;&nbsp;&nbsp;&nbsp;isConnectLeftMass() // return true if left end is connected with a mass
<br>&nbsp;&nbsp;&nbsp;&nbsp;isConnectRightMass() // return true if right end is connected with a mass
<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;class **Spring:** 
<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;//just call super(a, b) (default constructor)
<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;restlength // constructor
<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;restlength;constant; // constructor
<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;oscillate() // compress and extend to move masses; called by move()
<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;class **Muscle:**  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;restlength;amplitude; // constructor
<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;oscillate() // contract and expand to move masses; called by move()


**Explanation:**

Since there are two types of object: one representing the nodes and the other representing the links connecting them, we need to create two abstract classes:  Nodes and Links. 

Node should have the id and the xy position as its instance variable. Then we need to create two subclasses under nodes, namely Moving Mass and Fixed Mass, which has 3 constructors in total taking different number of parameters.One constructor takes no additional parameters (in the class FixedMass). One takes only x speed and y speed and another one takes in one more parameter, the mass (in the class MovingMass). The class MovingMass has a method follow() that enables it to be influenced by external forces and move according to the spring. The class FixedMass does not have such a method.

Links should have the id of the nodes it connect as its instance variable. Under links, two subclasses should be created: The spring and the muscle. Each one of them should contain one or more constructors that take different number of parameters. For Spring, it should have 3 constructors. One just calls the super constructor. One takes in restlength as the only parameter. Another one takes in constant as an additional parameter. For Muscle, it should have only one constructor, that takes in restlength and amplitude as parameters. The two subclasses, Spring and Muscle, have a lot of similarities and thus share some common functions, such as connect(), getMasses(), etc. However, they differ in the way they move and influence the masses. Thus we need to implement different oscilate() methods for the two classes separately. 


In order to implement the classes above, we need to use or extend following classes:  


jboxGlue.PhysicalObject; (create object; setting positions and forces; collision )  
jboxGlue.PhysicalObjectCircle; (create circle object,subclass of PhysicalObject)  
jboxGlue.PhysicalObjectRect; (create rectangle object,subclass of PhysicalObject)  
jboxGlue.WorldManager; (other background setup)  
jgame.JGColor; 	(set the color)  
jgame.JGObject; (superclass of PhysicalObject)  
jgame.platform.JGEngine; (base code for JGame to work)  
org.jbox2d  (jbox base code)  


More classes from source code may be needed when actually coding.

