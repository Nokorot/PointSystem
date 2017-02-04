package no.nokorot.pointsystem.Windows;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import no.nokorot.pointsystem.PointSystem;
import util.handelers.ImageHandeler;

public class Grab {

	public boolean selected = false;
	private JButton button;
	private Icon notSelected, Selected;
	private String s1 = "false", s2 = "true";
	public Color c1 = Color.WHITE, c2 = Color.RED;
	boolean color = true, text = false;
	
	static BufferedImage image = ImageHandeler.cut(PointSystem.icons, 0, 0, 80);
	static BufferedImage image2 = ImageHandeler.cut(PointSystem.icons, 1, 0, 80);

	public Grab(JButton button, BufferedImage img1, BufferedImage img2, boolean b) {
		this.button = button;
		color = b;

		int Size = button.getWidth() < button.getHeight() ? button.getWidth() : button.getHeight();
		try {
			image = ImageHandeler.getScaledImage(image, Size, Size);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			image2 = ImageHandeler.getScaledImage(image2, Size, Size);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		notSelected = new ImageIcon(image);
		Selected = new ImageIcon(image2);
		button.setIcon(notSelected);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				switchS();
			}
		});
		
		update();
	}

	public Grab(JButton button) {
		this(button, image, image2, true);
	}

	public Grab(JButton button, boolean b) {
		this(button, image, image2, b);
	}
	
	public Grab(JButton button, String s1, String s2, boolean b) {
		this.button = button;
		color = b;
		text = true;

		this.s1 = s1;
		this.s2 = s2;
		
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				switchS();
			}
		});
	}
	
	public Grab(JButton button, Color c1, Color c2) {
		this.button = button;

		this.c1 = c1;
		this.c2 = c2;
		
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				switchS();
			}
		});
	}

	public void switchS() {
		selected = !selected;
		update();
		onSetting();
	}

	public void set(boolean b) {
		selected = b;
		update();
		onSetting();
	}
	
	public void onSetting(){
		
	}
	
	private void update(){
		if (selected) {
			if (Selected != null) {
				button.setIcon(Selected);
			}
			if(text){
				button.setText(s2);
			}
			if(color){
				button.setBackground(c2);
			}
		} else {
			if (Selected != null) {
				button.setIcon(notSelected);
			}
			if(text){
				button.setText(s1);
			}
			if(color){
				button.setBackground(c1);
			}
		}
		
	}
	
	public void setImageSelected(BufferedImage img, boolean squear){
		if (img == null)
			return;
		
		int width = 0;
		int height = 0;
		if(squear)
			width = height = button.getWidth() < button.getHeight() ? button.getWidth() : button.getHeight();
		else{
			width = button.getWidth() + 18;
			height = button.getHeight();
		}
		try {
			img = ImageHandeler.getScaledImage(img, width, height);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		Selected = new ImageIcon(img);
	}
	
	public void setImageDotSelected(BufferedImage img){
		notSelected = new ImageIcon(img);
	}

}
