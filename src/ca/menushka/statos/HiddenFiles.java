package ca.menushka.statos;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;

public class HiddenFiles extends JFrame implements ActionListener {
	
	ArrayList<String> folders;
	
	JList<String> list = new JList<String>();
	JScrollPane scroll;
	
	JButton add;
	JButton remove;
	JButton finish;
	
	AddState parent;
	
	public HiddenFiles(AddState parent) {
		setLayout(null);
		
		this.parent = parent;
		this.folders = parent.folders;
		
		updateList(folders);
		
		scroll = new JScrollPane(list);
		scroll.setSize(350, 400);
		scroll.setLocation(25, 25);
		add(scroll);
		
		add = new JButton("Add Folder");
		add.setSize(180, 25);
		add.setLocation(10, 440);
		add.addActionListener(this);
		add(add);
		
		remove = new JButton("Remove Folder");
		remove.setSize(180, 25);
		remove.setLocation(210, 440);
		remove.addActionListener(this);
		add(remove);
		
		finish = new JButton("Finish");
		finish.setSize(200, 25);
		finish.setLocation(100, 480);
		finish.addActionListener(this);
		add(finish);
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(400, 550);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	public void updateList(ArrayList<String> files){
		String[] f = new String[files.size()];
		for (int i = 0; i < files.size(); i++) {
			f[i] = files.get(i);
		}
		list.setListData(f);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		
		if (obj.equals(add)){
			JFileChooser chooser = new JFileChooser();
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int returnVal = chooser.showOpenDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				folders.add(chooser.getSelectedFile().getAbsolutePath());
				updateList(folders);
			}
		} else if (obj.equals(remove)){
			folders.remove(list.getSelectedValue());
			updateList(folders);
		} else if (obj.equals(finish)){
			parent.folders = folders;
			dispose();
		}
	}
}
