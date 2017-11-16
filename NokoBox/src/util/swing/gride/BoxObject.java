package util.swing.gride;

import java.awt.Rectangle;

import javax.swing.JPanel;

public interface BoxObject {
	
	// public void setBounds(double x, double y, double width, double height);
	
	public void setBounds(Rectangle rectangle);

	public void setVisible(boolean visable);
	

	public void setPane(JPanel pane);

	public interface PreferdBoxObject extends BoxObject {
		public void makePreferdBox(Box box);
	}
	
	public static interface DBoxObject extends BoxObject {
		public void increaseBounds(double dx, double dy, double dw, double dh);
		public void setBounds(double x, double y, double width, double height);
	}
	
}