package no.noko.pointsystem;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JTextField;

import util.Window;
import util.swing.NBPanel;
import util.swing.gride.BoxObject;
import util.swing.gride.XStrip;
import util.swing.gride.YStrip;
import util.swing.tabLabel.Tab;
import util.swing.tabLabel.TabLabel;

public class ToolBar extends TabLabel implements BoxObject {
	private static final long serialVersionUID = 1L;

	public ToolBar(Window window) {
		super(window, MainMenu.getTabLabelLayout());
		setButtonHeight(20);
		
		
		this.addTab(makeHomeT());
		
		this.addTab(makeBackgroundT());
		
		this.addTab(makeFontsT());
	}
	
	private Tab makeHomeT() {
		Tab homeT = new Tab(this, "Home");
		XStrip x = new XStrip();
		
		NBPanel p1 = new NBPanel();
		p1.setLayout(new BoxLayout(p1, BoxLayout.X_AXIS));
		p1.add(new JTextField("Hey"));
		p1.add(new JTextField());
		
		NBPanel panel = new NBPanel();
		panel.setLayout(null);
		panel.add(p1, BorderLayout.CENTER);
		panel.setBorder(BorderFactory.createTitledBorder("Drop area"));
		
		window.add(panel);
		
		x.append(panel);
		
//		YStrip y = new YStrip();
//		y.append(new Label(window, "Size:"), 2);
//		y.append(new NBTextField(window), 2);
//		y.append(new NBTextField(window), 2);
		
		
		x.append(new YStrip(), 5);
		
		homeT.getFrameBox().setBoxObject(x);
		return homeT;
	}
	
	private Tab makeBackgroundT(){
		Tab backgroundT = new Tab(this, "Backgrond"); 
		return backgroundT;
	}
	
	private Tab makeFontsT(){
		Tab fontsT = new Tab(this, "Fonts"); 
		return fontsT;
	}

}
