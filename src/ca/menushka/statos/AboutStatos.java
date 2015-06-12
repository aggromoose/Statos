package ca.menushka.statos;

import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class AboutStatos extends JFrame{
	public AboutStatos() {
		setLayout(null);
		
		JLabel title = new JLabel("STATOS", JLabel.CENTER);
		title.setFont(new Font("San-Serif", Font.BOLD, 48));
		title.setSize(250, 100);
		title.setLocation(25, 0);
		add(title);
		
		String s = "This is gonna be a sick explanation about this application.";
		JLabel explain = new JLabel("<html><div style=\"text-align: center;\">" + s + "</html>", JLabel.CENTER);
		explain.setFont(new Font("San-Serif", Font.PLAIN, 18));
		explain.setSize(250, 100);
		explain.setLocation(25, 100);
		add(explain);
		
		String c = "Copyright 2015 Menushka Weeratunga. All rights reserved";
		JLabel copyright = new JLabel("<html><div style=\"text-align: center;\">" + c + "</html>", JLabel.CENTER);
		copyright.setFont(new Font("San-Serif", Font.PLAIN, 12));
		copyright.setSize(250, 100);
		copyright.setLocation(25, 200);
		add(copyright);
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(300, 300);
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
	}
}
