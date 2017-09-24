package no.noko.pointsystem;

import util.Window;
import util.swing.NBTextField;
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
		
		Tab backgroundT = new Tab(this, "Backgrond"); 
		this.addTab(backgroundT);
		
		Tab fontsT = new Tab(this, "Fonts");
		this.addTab(fontsT);
	}
	
	private Tab makeHomeT() {
		Tab homeT = new Tab(this, "Home");
		XStrip x = new XStrip();
		
		YStrip y = new YStrip();
		y.append(new NBTextField(window));
		x.append(y);
		
		homeT.getFrameBox().setBoxObject(x);
		return homeT;
	}

}
