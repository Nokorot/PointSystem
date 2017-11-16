package no.nokorot.pointsystem.Windows;

import java.awt.Color;
import java.awt.GraphicsDevice;
import java.awt.event.ActionEvent;

import no.nokorot.pointsystem.PSData;
import util.Window;
import util.swing.Label;
import util.swing.NBButton;
import util.swing.NBTextField;
import util.swing.PopDownTextField;
import util.swing.gride.Box;
import util.swing.gride.BoxObject;
import util.swing.gride.XStrip;
import util.swing.gride.YStrip;

public class MainToolbar extends YStrip {
	
	private Window window;
	
	/// Home 
	private Color selectedColor = Color.CYAN;
	
	// Entities
 	private NBTextField theams;
	

	private NBButton home, entities, online;
	private NBButton selectedT;
 	
	private Box contentBox;
	private BoxObject homeT, EntitiesT, OnlineT; 
	private BoxObject selectedB; 

	public MainToolbar(Window window) {
		this.window = window;
		
		XStrip x = new XStrip();
		x.append(home     = new NBButton(window, "Home"));
		home.addActionListener((ActionEvent e) -> openHomeT());
		x.append(entities = new NBButton(window, "Entities"));
		entities.addActionListener((ActionEvent e) -> openEntitiesT());
		x.append(online   = new NBButton(window, "Online"));
		online.addActionListener((ActionEvent e) -> openOnlineT());
//		x.append((BoxObject) null, 1);
		
		this.append(x, 1, 0);
		Label line = new Label(window);
		line.setOpaque(false);
		line.setBackground(Color.BLACK);
		this.append(line, 0.1);
		
		contentBox = new Box();
		this.append(contentBox, 9.5);
		
		openHomeT();
	}
	
	private void select(NBButton b, BoxObject box){
		contentBox.setBoxObject(box);
		if (selectedT != null){
			selectedT.setBackground(b.getBackground());
			selectedB.setVisible(false);
		}
		box.setVisible(true);
		b.setBackground(selectedColor);
		selectedT = b;
		selectedB = box;
	}
	
	public void openHomeT(){
		if (selectedT == home)
			return;
		if (homeT == null){
			YStrip root = new YStrip();
			
			//root.append((BoxObject) null, 2);
			
			XStrip x = new XStrip();
			x.append(new Label(window, "Size"));
			
			PopDownTextField pdtf = new PopDownTextField(window);
//			pdtf.setFont(new Font(pdtf.getFont().getFontName(), pdtf.getFont().getStyle(), 11));
			pdtf.setFontSize(14);
			x.append(pdtf);
			
			pdtf.addElement("Custom");
			GraphicsDevice[] devices = PSData.environment.getScreenDevices();
			for (int i = 0; i < devices.length; i++)
				pdtf.addElement("Screen " + i);

			if (devices.length > 1)
				pdtf.setSelectedIndex(1);
			else
				pdtf.setSelectedIndex(0);
			
			root.append(x, 0.75, 0);
			
			x = new XStrip();
			x.append(new NBTextField(window)).setRightInset(0);
			x.append(new Label(window, ","), 0.2).setInsets(0);
			x.append(new NBTextField(window)).setLeftInset(0);
			x.append((BoxObject) null, 0.1).setInsets(0);
			x.append(new NBTextField(window)).setRightInset(0);
			x.append(new Label(window, "x"), 0.2).setInsets(0);
			x.append(new NBTextField(window)).setLeftInset(0);
			root.append(x, 1.25, 0).setBottomInset(20);

			root.append(new NBButton(window, "Background"), 2, 0);
			root.append(new NBButton(window, "Fonts"), 2, 0);
			
			// root.append((BoxObject) null, 2);
			
			homeT = root;
			
		}
		select(home, homeT);
	}
	
	public void openEntitiesT(){
		if (selectedT == entities)
			return;
		if (EntitiesT == null){
			YStrip root = new YStrip();
			
			root.append((BoxObject) null, 2);
			
//			root.append(new Label(window, "PintSystem"));
			
			XStrip x = new XStrip();
			x.append(new Label(window, "Teams"));
			x.append(theams = new NBTextField(window));
			root.append(x, 0.75).setBottomInset(20);
			
			YStrip y = new YStrip();
			y.append(new Label(window, "Points given"), 1);

			x = new XStrip();
			x.append(new Label(window, "--")).setTopBottomInsets(0);
			x.append(new Label(window, "-")).setTopBottomInsets(0);
			x.append(new Label(window, "+")).setTopBottomInsets(0);
			x.append(new Label(window, "++")).setTopBottomInsets(0);
			y.append(x);
			x = new XStrip();
			x.append(new NBTextField(window));
			x.append(new NBTextField(window));
			x.append(new NBTextField(window));
			x.append(new NBTextField(window));
			y.append(x,2);
			
			root.append(y, 1.5).setBottomInset(20);
			
			y = new YStrip();
			
			x = new XStrip();
			x.append(new Label(window, "Default Names"), 8);
			x.append(new NBButton(window, "?"));
			y.append(x);
			y.append(new NBTextField(window, "Team #1"));
			root.append(y);
			
			root.append((BoxObject) null, 2);
			
			EntitiesT = root;
		}
		select(entities, EntitiesT);
	}
	
	public void openOnlineT() {
		if (selectedT == online)
			return;
		if (OnlineT == null){
			YStrip root = new YStrip();
			
			root.append((BoxObject) null, 2);
			
			XStrip x = new XStrip();
			x.append(new Label(window, "Size"));
			
			PopDownTextField pdtf = new PopDownTextField(window);
//			pdtf.setFont(new Font(pdtf.getFont().getFontName(), pdtf.getFont().getStyle(), 11));
			pdtf.setFontSize(14);
			x.append(pdtf);
			
			pdtf.addElement("Custom");
			GraphicsDevice[] devices = PSData.environment.getScreenDevices();
			for (int i = 0; i < devices.length; i++)
				pdtf.addElement("Screen " + i);

			if (devices.length > 1)
				pdtf.setSelectedIndex(1);
			else
				pdtf.setSelectedIndex(0);
			
			root.append(x, 0.75);
			x = new XStrip();
			x.append(new NBTextField(window)).setRightInset(0);
			x.append(new Label(window, ","), 0.2).setInsets(0);
			x.append(new NBTextField(window)).setLeftInset(0);
			x.append((BoxObject) null, 0.1).setInsets(0);
			x.append(new NBTextField(window)).setRightInset(0);
			x.append(new Label(window, "x"), 0.2).setInsets(0);
			x.append(new NBTextField(window)).setLeftInset(0);
			root.append(x, 0.75);

			root.append(new NBButton(window, "Background"), 2);
			root.append(new NBButton(window, "Fonts"), 2);
			
			root.append((BoxObject) null, 2);
			
			OnlineT = root;
		}
		select(online, OnlineT);
	}
	
	
	
}
