package ca.menushka.statos;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerNumberModel;


public class AddState extends JFrame implements ActionListener {
	
	JTextField name;
	//JTextField start;
	JSpinner start;
	//JTextField end;
	JSpinner end;
	JButton desktop;
	JLabel dLabel;
	String desktopPath = "";
	
	JButton hiddenFiles;
	
	JButton addState;
	JButton saveState;
	JButton deleteState;
	
	int idEdit = -1;
	
	ArrayList<String> folders = new ArrayList<String>();
	
	public AddState() {
		init();
		
		addState = new JButton("Add State...");
		addState.setSize(200, 25);
		addState.setLocation(200, 275);
		addState.addActionListener(this);
		add(addState);
	}
	
	public AddState(int i){
		init();
		
		State state = Info.states.get(i);
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
		
		name.setText(state.getName());
		try {
			start.setValue(dateFormat.parse(state.getStart()));
			end.setValue(dateFormat.parse(state.getEnd()));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dLabel.setText(state.getDesktopImage());
		desktopPath = state.getDesktopImage();
		
		folders = state.getHiddenFolders();
		
		saveState = new JButton("Save State");
		saveState.setSize(200, 25);
		saveState.setLocation(50, 275);
		saveState.addActionListener(this);
		add(saveState);
		
		deleteState = new JButton("Delete State");
		deleteState.setSize(200, 25);
		deleteState.setLocation(350, 275);
		deleteState.addActionListener(this);
		add(deleteState);
		
		idEdit = i;
	}
	
	public void init(){
		setLayout(null);
		
		JLabel nt = new JLabel("Name:");
		nt.setSize(100, 25);
		nt.setLocation(100, 25);
		add(nt);
		
		name = new JTextField();
		name.setSize(300, 25);
		name.setLocation(205, 25);
		add(name);
		
		/*start = new JTextField("Start Time");
		start.setSize(400, 25);
		start.setLocation(100, 75);
		add(start);*/
		
		SpinnerNumberModel hour = new SpinnerNumberModel(0, 0, 24, 1);
		SpinnerNumberModel minute = new SpinnerNumberModel(0, 0, 60, 1);
		
		SpinnerDateModel stime = new SpinnerDateModel();
		stime.setCalendarField(Calendar.MINUTE);
		
		SpinnerDateModel etime = new SpinnerDateModel();
		etime.setCalendarField(Calendar.MINUTE);
		
		JLabel st = new JLabel("Starting Time:");
		st.setSize(100, 25);
		st.setLocation(100, 75);
		add(st);
		
		start = new JSpinner(stime);
		start.setEditor(new JSpinner.DateEditor(start , "HH:mm"));
		start.setSize(100, 25);
		start.setLocation(195, 75);
		add(start);
		
		/*end = new JTextField("End Time");
		end.setSize(400, 25);
		end.setLocation(100, 125);
		add(end);*/
		
		JLabel et = new JLabel("Ending Time:");
		et.setSize(100, 25);
		et.setLocation(305, 75);
		add(et);
		
		end = new JSpinner(etime);
		end.setEditor(new JSpinner.DateEditor(end , "HH:mm"));
		end.setSize(100, 25);
		end.setLocation(400, 75);
		add(end);
		
		JLabel dt = new JLabel("Desktop Image:");
		dt.setSize(100, 25);
		dt.setLocation(100, 175);
		add(dt);
		
		desktop = new JButton("Browse");
		desktop.setSize(100, 25);
		desktop.setLocation(200, 175);
		desktop.addActionListener(this);
		add(desktop);
		
		dLabel = new JLabel("None");
		dLabel.setSize(200, 25);
		dLabel.setLocation(305, 175);
		add(dLabel);
		
		hiddenFiles = new JButton("Hidden Files");
		hiddenFiles.setSize(200, 25);
		hiddenFiles.setLocation(200, 225);
		hiddenFiles.addActionListener(this);
		add(hiddenFiles);
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(600, 350);
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		
		if (obj.equals(desktop)){
			JFileChooser chooser = new JFileChooser();
			chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			int returnVal = chooser.showOpenDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				desktopPath = chooser.getSelectedFile().getAbsolutePath();
				dLabel.setText(desktopPath);
			}
		} else if (obj.equals(hiddenFiles)){
			HiddenFiles hidden = new HiddenFiles(this);
		} else if (obj.equals(addState)){
			Date s = (Date) start.getValue();
			Date t = (Date) end.getValue();
			Info.addState(name.getText(), s.getHours() + ":" + s.getMinutes(), t.getHours() + ":" + t.getMinutes(), desktopPath, folders);
			setVisible(false);
			dispose();
		} else if (obj.equals(saveState)){
			Date s = (Date) start.getValue();
			Date t = (Date) end.getValue();
			Info.editState(name.getText(), s.getHours() + ":" + s.getMinutes(), t.getHours() + ":" + t.getMinutes(), desktopPath, folders, idEdit);
			setVisible(false);
			dispose();
		} else if (obj.equals(deleteState)){
			Info.deleteState(idEdit);
			setVisible(false);
			dispose();
		}
	}
}
