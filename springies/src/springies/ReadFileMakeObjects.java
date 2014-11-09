package springies;

import java.io.File;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
/**
 * @author Justin (Zihao) Zhang, Nick (Pengyi) Pan
 */
public class ReadFileMakeObjects {

	Springies myGame;

	public ReadFileMakeObjects(Springies game){
		myGame = game;
	}

	public void parse(String filename,String type){

		try{
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder(); 
			Document doc = db.parse(new File(filename));

			///
			if(type.equals("environment")){
			NodeList allEnvirnmentNodes = doc.getChildNodes().item(0).getChildNodes();
			for (int i = 0;i<allEnvirnmentNodes.getLength();i++){
				if (allEnvirnmentNodes.item(i) instanceof Element){
					String attrType = allEnvirnmentNodes.item(i).getNodeName(); //attribute type e.g. gravity; viscosity
					Element innerElement = (Element) allEnvirnmentNodes.item(i);
					NamedNodeMap innerElmntAttr = innerElement.getAttributes(); //attribute list

					HashMap<String,Double> callParaMap = new HashMap<String,Double>();
					for (int j = 0; j < innerElmntAttr.getLength(); ++j)
					{
						Node attr = innerElmntAttr.item(j);  //each attribute	    	        	    	    
						callParaMap.put(attr.getNodeName(), Double.parseDouble(attr.getNodeValue()));

					}
					myGame.setEnvironment(callParaMap,attrType);
				}
			}
			return; //only reading environment data
			}												
			///
			NodeList allNodes = doc.getElementsByTagName(type);

			NodeList childNode = allNodes.item(0).getChildNodes();
			Element cNode = (Element) childNode;
			NodeList innerElmntLst = cNode.getChildNodes();
			Element innerElement = null;
			for (int i = 0; i < innerElmntLst.getLength(); ++i)
			{
				if (innerElmntLst.item(i) instanceof Element)
				{
					innerElement = (Element) innerElmntLst.item(i);
					NamedNodeMap innerElmntAttr = innerElement.getAttributes();//attribute list

					HashMap<String,Double> callParaMap = new HashMap<String,Double>();

					if(type.equals("nodes")){   	        	
						String id = "";

						for (int j = 0; j < innerElmntAttr.getLength(); ++j)
						{
							Node attr = innerElmntAttr.item(j); //each attribute 	    	        	    	    
							if(attr.getNodeName().equals("id")){ //value is a string
								id = attr.getNodeValue();
							}else{ // value not a string
								callParaMap.put(attr.getNodeName(), Double.parseDouble(attr.getNodeValue())); 
							}

						}
						if(innerElement.getNodeName()=="fixed"){
							myGame.addNodes(id, callParaMap,true);
						}else{
							myGame.addNodes(id, callParaMap,false);
						}

					}
					if(type.equals("links")){
						String a = "";
						String b = "";
						String linkName = "s"+i; //links id starts with s, e.g. s0, s1, s2
						for (int j = 0; j < innerElmntAttr.getLength(); ++j)
						{
							Node attr = innerElmntAttr.item(j); //each attribute

							if(attr.getNodeName().equals("a")){ //a is a string
								a = attr.getNodeValue();
							}
							else if(attr.getNodeName().equals("b")){ //b is a string
								b = attr.getNodeValue();       	    	    	
							}
							else{ // value not a string
								callParaMap.put(attr.getNodeName(), Double.parseDouble(attr.getNodeValue())); 
							}       	    	    
						}
						if(innerElement.getNodeName()=="muscle"){
							myGame.addLinks(linkName, callParaMap, a, b,true);
						}else{
							myGame.addLinks(linkName, callParaMap, a, b,false);
						}

					}
				}

			}

		}

		catch ( Exception e ) {
			//e.printStackTrace();
			System.out.println("Please select a valid XML file!");
			throw new NullPointerException();
		}
	}

}
