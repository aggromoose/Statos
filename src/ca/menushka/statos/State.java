package ca.menushka.statos;

import java.util.ArrayList;

public class State {
	
	//Name
	private String name;
	
	//Start and end times
	private String start, end;
	
	//Desktop Image Path
	private String desktopImage;
	
	//Hidden Folders Paths
	private ArrayList<String> hiddenFolders = new ArrayList<String>();
	
	public State(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStart() {
		return start;
	}
	
	public int getStartHour() {
		return Integer.parseInt(start.split(":")[0]);
	}
	
	public int getStartMinute() {
		return Integer.parseInt(start.split(":")[1]);
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getEnd() {
		return end;
	}
	
	public int getEndHour() {
		return Integer.parseInt(end.split(":")[0]);
	}
	
	public int getEndMinute() {
		return Integer.parseInt(end.split(":")[1]);
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public String getDesktopImage() {
		return desktopImage;
	}

	public void setDesktopImage(String desktopImage) {
		this.desktopImage = desktopImage;
	}

	public ArrayList<String> getHiddenFolders() {
		return hiddenFolders;
	}

	public void setHiddenFolders(ArrayList<String> hiddenFolders) {
		this.hiddenFolders = hiddenFolders;
	}
}
