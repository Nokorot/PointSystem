package no.nokorot.pointsystem.utils;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Frame;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;

public class ColorPicker extends JDialog {
	private static final long serialVersionUID = 1L;

	private boolean read = true;

	private Robot robot;
	private Color color = Color.RED;
	
	private final int max = 10;
	private int index = 0;
	private boolean multipule = false;
	private JLabel[] colorLables = new JLabel[max];
	private List<Color> colors = new ArrayList<>(max);

	public ColorPicker(Frame aFrame) {
		super(aFrame, true);

		try {
			robot = new Robot();
		} catch (AWTException e1) {
			e1.printStackTrace();
		}

		setSize(250, 250);
		setLocationRelativeTo(aFrame);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

		setLayout(null);

		JLabel info = new JLabel("Sellect color by bresing \"P\"");
		info.setHorizontalAlignment(JLabel.CENTER);
		info.setBounds(0, 50, 250, 50);
		add(info);
		
		for (int i = 0; i < 10; i++){
			colorLables[i] = new JLabel();
			colorLables[i].setBounds(25 + i * 20, 5, 20, 20);
			colorLables[i].setBackground(Color.GREEN);
			colorLables[i].setOpaque(true);
			add(colorLables[i]);
		}
		
		JButton close = new JButton("close");
		close.setBounds(25, 150, 200, 50);
		close.addActionListener((ActionEvent e) -> {
			color = null;
			close();
		});
		add(close);
		

		close.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_P) {
					addColor();
					if (!multipule)
						close();
					if (index >= max)
						close();
				}
			}
		});
	}
	
	private void close(){
		read = false;
		setVisible(false);
	}
	
	private void addColor(){
		colors.add(color);
		colorLables[index].setOpaque(true);
		colorLables[index++].setBackground(color);
	}
	
	private void run(){
		new Thread(() -> {
			while (read) {
				Point mouse = MouseInfo.getPointerInfo().getLocation();
				color = robot.getPixelColor(mouse.x, mouse.y);
				getContentPane().setBackground(color);
				try {
					Thread.sleep(20);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	public Color pickColor() {
		multipule = false;
		run();
		setVisible(true);
		return color;
	}
	
	public List<Color> pickColors() {
		multipule = true;
		run();
		setVisible(true);
		return colors;
	}

}