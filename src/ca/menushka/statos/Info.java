package ca.menushka.statos;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Info {
	
	public static Statos statos;
	
	public static Document doc;
	
	public static ArrayList<State> states = new ArrayList<State>();
	public static int[] currentState = new int[24 * 60];
	
	public static String getApplicationSupportDirectory(){
		return System.getProperty("user.home") + "/Library/Application Support/Statos/";
	}
	
	public static void writeDataXML(){
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			
			doc = builder.newDocument();
			Element root = doc.createElement("states");
			doc.appendChild(root);
			
			for (int i = 0; i < states.size(); i++) {
				Element state = doc.createElement("state");
				state.setAttribute("name", states.get(i).getName());
				
				Element time = doc.createElement("time");
				
				Element start = doc.createElement("start");
				start.setTextContent(states.get(i).getStart());
				time.appendChild(start);
				
				Element end = doc.createElement("end");
				end.setTextContent(states.get(i).getEnd());
				time.appendChild(end);
				
				state.appendChild(time);
				
				Element desktop = doc.createElement("desktop");
				desktop.setTextContent(states.get(i).getDesktopImage());
				state.appendChild(desktop);
				
				Element files = doc.createElement("files");
				
				ArrayList<String> f = states.get(i).getHiddenFolders();
				for (int j = 0; j < f.size(); j++) {
					Element file = doc.createElement("file");
					file.setTextContent(f.get(j));
					files.appendChild(file);
				}
				
				state.appendChild(files);
				
				root.appendChild(state);
			}
			
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
	        Transformer transformer = transformerFactory.newTransformer();
	        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
	        DOMSource source = new DOMSource(doc);
	        StreamResult result = new StreamResult(new File(getApplicationSupportDirectory() + "states.xml"));
	        transformer.transform(source, result);
	        
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void readDataXML(){
		
		try {
			File fXmlFile = new File("/Users/mkyong/staff.xml");
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			doc = builder.parse(getApplicationSupportDirectory() + "states.xml");
			
			NodeList nList = doc.getElementsByTagName("state");
			
			for (int i = 0; i < nList.getLength(); i++) {
				Element node = (Element) nList.item(i);
				
				State state = new State(node.getAttribute("name"));
				Element time = (Element) node.getElementsByTagName("time").item(0);
				state.setStart(time.getElementsByTagName("start").item(0).getTextContent());
				state.setEnd(time.getElementsByTagName("end").item(0).getTextContent());
				state.setDesktopImage(node.getElementsByTagName("desktop").item(0).getTextContent());
				
				ArrayList<String> files = new ArrayList<String>();
				NodeList fList = node.getElementsByTagName("file");
				
				for (int j = 0; j < fList.getLength(); j++) {
					files.add(fList.item(j).getTextContent());
				}
				
				state.setHiddenFolders(files);
				
				states.add(state);
				
				updateCurrentState();
			}
			
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void updateCurrentState(){
		currentState = new int[24 * 60];
		for (State state: states){
			int start =  state.getStartHour() * 60 + state.getStartMinute();
			int end =  state.getEndHour() * 60 + state.getEndMinute();
			for (int j = start; j <= end; j++) {
				currentState[j] = states.size();
			}
		}
	}
	
	public static void addState(String name, String start, String end, String desktop, ArrayList<String> files){
		State state = new State(name);
		state.setStart(start);
		state.setEnd(end);
		state.setDesktopImage(desktop);
		state.setHiddenFolders(files);
		states.add(state);
		
		statos.updateSubMenus();
		
		updateCurrentState();
	}
	
	public static void editState(String name, String start, String end, String desktop, ArrayList<String> files, int id){
		State state = new State(name);
		state.setStart(start);
		state.setEnd(end);
		state.setDesktopImage(desktop);
		state.setHiddenFolders(files);
		
		states.set(id, state);
		
		statos.updateSubMenus();
		
		updateCurrentState();
	}
	
	public static void deleteState(int id){
		Info.states.remove(id);
		statos.updateSubMenus();
		
		updateCurrentState();
	}
}
