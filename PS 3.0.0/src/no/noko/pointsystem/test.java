package no.noko.pointsystem;

import java.awt.Button;
import java.awt.Panel;

import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JPanel;

import util.Platform;
import util.Window;
import util.swing.gride.YStrip;

public class test {
	
	public static void main(String[] args) {
		new Platform();
		JFrame frame = new JFrame("Heu");
		frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
		frame.setSize(500, 500);
		frame.setLocationRelativeTo(null);

		JPanel p = new JPanel();
		Gr
		Button b = new Button("Hey");
		b.setSize(100, 50);
		
		p.add(b);
		p.setLo
		
		frame.add(p);		
		
		frame.setVisible(true);
		
		
		
	}

}
