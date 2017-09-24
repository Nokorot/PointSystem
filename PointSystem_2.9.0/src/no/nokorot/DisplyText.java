package no.nokorot;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class DisplyText {

	public static void display(String s) {

		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(3);
		frame.setSize(500, 500);
		frame.setTitle("Displayer");
		frame.setVisible(true);

		JPanel p = (JPanel) frame.getContentPane();

		JTextArea t = new JTextArea();
		t.setSize(500, 500);
		p.add(t);
		t.setText(s);
	}

}
