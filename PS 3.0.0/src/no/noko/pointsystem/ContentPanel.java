package no.noko.pointsystem;

import util.Window;
import util.swing.gride.BoxObject;
import util.swing.tabLabel.Tab;
import util.swing.tabLabel.TabLabel;

public class ContentPanel extends TabLabel implements BoxObject {
	private static final long serialVersionUID = 1L;

	public ContentPanel(Window window) {
		super(window, MainMenu.getTabLabelLayout());
		setButtonHeight(20);
		
		this.addTab(new Tab(this, "Hey"));
		
	}
}
