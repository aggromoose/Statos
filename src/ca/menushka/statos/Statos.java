package ca.menushka.statos;
import java.awt.AWTException;
import java.awt.Desktop;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.CheckboxMenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;


public class Statos implements ActionListener, ItemListener{
	
	PopupMenu menu;
	PopupMenu emenu;
	PopupMenu smenu;
	State current;
	int selected = 0;
	
	TrayIcon tray;
	
	public Statos() {
		
		Info.statos = this;
		
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				checkTime();
			}
		}, 0, 1000);
		
		File appsup = new File(Info.getApplicationSupportDirectory());
		if (!appsup.isDirectory()){
			appsup.mkdir();
			Info.writeDataXML();
		}
		
		Info.readDataXML();
		
		menu = new PopupMenu();
		menu.add("Add State...");
		//menu.add("Edit States");
		
		emenu = new PopupMenu("Edit State");
		for (int i = 0; i < Info.states.size(); i++) {
			CheckboxMenuItem item = new CheckboxMenuItem(Info.states.get(i).getName(), false);
			item.addItemListener(this);
			emenu.add(item);
		}
		menu.add(emenu);
		
		smenu = new PopupMenu("Set State");
		CheckboxMenuItem item = new CheckboxMenuItem("Auto", true);
		item.addItemListener(this);
		smenu.add(item);
		for (int i = 0; i < Info.states.size(); i++) {
			item = new CheckboxMenuItem(Info.states.get(i).getName(), false);
			item.addItemListener(this);
			smenu.add(item);
		}
		menu.add(smenu);
		
		menu.addSeparator();
		menu.add("About Statos");
		menu.add("Donate to the Project");
		menu.add("Quit Statos");
		for (int i = 0; i < menu.getItemCount(); i++) {
			menu.getItem(i).addActionListener(this);
		}
		
		//Creates the icon popup menu
		tray = new TrayIcon(new ImageIcon(getClass().getResource("/resources/statos_icon.png")).getImage(), "Statos", menu);
		
		//Adds it to the os
		try {
			SystemTray.getSystemTray().add(tray);
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}
	
	public void updateSubMenus(){
		emenu.removeAll();
		for (int i = 0; i < Info.states.size(); i++) {
			CheckboxMenuItem item = new CheckboxMenuItem(Info.states.get(i).getName(), false);
			item.addItemListener(this);
			emenu.add(item);
		}
		
		smenu.removeAll();
		CheckboxMenuItem item = new CheckboxMenuItem("Auto", true);
		item.addItemListener(this);
		smenu.add(item);
		for (int i = 0; i < Info.states.size(); i++) {
			item = new CheckboxMenuItem(Info.states.get(i).getName(), false);
			item.addItemListener(this);
			smenu.add(item);
		}
	}

	//Change the visiable on a file with the path and a boolean value whether it is hidden or not
	public void setHidden(String path, boolean value){
		File f = new File(path);
		String filename = path.split("/")[path.split("/").length - 1];
		if (f.isDirectory() && value == true){
			f.renameTo(new File(path.substring(0, path.length() - filename.length()) + "." + filename));
		} else if (!f.isDirectory() && value == false){
			f = new File(path.substring(0, path.length() - filename.length()) + "." + filename);
			f.renameTo(new File(path));
		}
	}
	
	//Change wallpaper with the path
	public void changeWallpaper(String path){
		String chWallString = "tell application \"Finder\" \n set p to POSIX file \"%s\" \n set desktop picture to p as alias \n end tell";
		
		try {
			Runtime runtime = Runtime.getRuntime();
			String[] args = { "osascript", "-e", String.format(chWallString, path) };
			Process process = runtime.exec(args);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	public void checkTime(){
		if (selected == 0){
			Date date = new Date();
			SimpleDateFormat hours = new SimpleDateFormat("HH");
			SimpleDateFormat minutes = new SimpleDateFormat("mm");
			int currentTime = Integer.parseInt(hours.format(date)) * 60 + Integer.parseInt(minutes.format(date));
			int stateNum = Info.currentState[currentTime] - 1;
			if (stateNum >= 0){
				State state = Info.states.get(stateNum);
				if (current != null){
					if (!current.getName().equals(state.getName())){
						unhideFolders(current);
					}
				}
				applyState(state);
				current = state;
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		
		//Add State
		if (obj.equals(menu.getItem(0))){
			AddState addstate = new AddState();
		} else if (obj.equals(menu.getItem(1))){
			
		} else if (obj.equals(menu.getItem(2))){
			//This is the state chooser
		} else if (obj.equals(menu.getItem(3))){
			//This is the separtor
		} else if (obj.equals(menu.getItem(4))){
			AboutStatos aboutstatos = new AboutStatos();
		} else if (obj.equals(menu.getItem(5))){
			try {
				Desktop.getDesktop().browse(new URL("http://menushka.ca/Statos/").toURI());
			} catch (IOException | URISyntaxException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} else if (obj.equals(menu.getItem(6))){
			Info.writeDataXML();
			System.exit(0);
		}
	}
	
	@Override
	public void itemStateChanged(ItemEvent e) {
		Object obj = e.getSource();
		
		for (int i = 0; i < emenu.getItemCount(); i++) {
			if (obj.equals(emenu.getItem(i))){
				if (((CheckboxMenuItem) emenu.getItem(i)).getState()){
					((CheckboxMenuItem) emenu.getItem(i)).setState(false);
				} else {
					((CheckboxMenuItem) emenu.getItem(i)).setState(true);
				}
				
				AddState addState = new AddState(i);
			}
		}
		
		for (int i = 0; i < smenu.getItemCount(); i++) {
			if (obj.equals(smenu.getItem(i))){
				//Uncheck Previous
				switchCheck(selected);

				unhideFolders(current);
				if (i >= 1){
					State state = Info.states.get(i - 1);
					applyState(state);
					current = state;
				}
				
				selected = i;
			}
		}
	}
	
	//Switch Check mark on Set State Menu
	public void switchCheck(int i){
		if (((CheckboxMenuItem) smenu.getItem(i)).getState()){
			((CheckboxMenuItem) smenu.getItem(i)).setState(false);
		} else {
			((CheckboxMenuItem) smenu.getItem(i)).setState(true);
		}
	}
	
	//Apples changed basiced on state passed in
	public void applyState(State state){
		changeWallpaper(state.getDesktopImage());
		for (String file: state.getHiddenFolders()){
			setHidden(file, true);
		}
	}
	
	public void unhideFolders(State state){
		for (String file: state.getHiddenFolders()){
			setHidden(file, false);
		}
	}
	
	public static void main(String[] args) {
		Statos start = new Statos();
	}
}
