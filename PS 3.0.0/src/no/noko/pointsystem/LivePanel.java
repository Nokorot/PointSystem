package no.noko.pointsystem;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JPanel;

import util.Window;
import util.swing.Label;
import util.swing.NBButton;
import util.swing.gride.BoxObject;
import util.swing.gride.XStrip;
import util.swing.gride.YStrip;

public class LivePanel implements BoxObject {

	public Color background = MainMenu.B;

	private Color lightColor = Color.CYAN;
	private Color darkColor = MainMenu.A;
	
	private int ovalWidth = 2, ovalHeight = 2;
	
	private YStrip root; 
	private Label label;
	
	public LivePanel(Window window) {
		root = new YStrip();
		
		XStrip x = new XStrip();
		
		x.append(new NBButton(window, "<html>Hide</html>"));
		x.append(new NBButton(window, "<html>Logo</html>"));
		x.append(new NBButton(window, "<html>Black</html>"));
		x.append(new NBButton(window, "<html>Clear</html>"));
		
		root.append(x);
		
		YStrip y = new YStrip();
		root.append(y, 8);
		
		label = new Label(window) {
			private static final long serialVersionUID = 1L;

			protected void paintComponent(Graphics g) {
				int width = getWidth() - 2;
				int height = getHeight() - 2;

				if (background != null) {
					g.setColor(background);
					g.fillRect(0, 0, width, height);
				}

				g.setColor(lightColor);
				g.drawArc(0, 0, 2 * ovalWidth, 2 * ovalHeight, 180, -90);
				g.drawArc(width - 2 * ovalWidth, 0, 2 * ovalWidth, 2 * ovalHeight, 90, -90);
				g.drawLine(2 * ovalWidth-1, 0, width - ovalWidth, 0);
				g.drawLine(0, 2 * ovalWidth-1, 0, height-ovalHeight);

				g.setColor(darkColor);
				g.drawLine(width, ovalHeight, width, height - ovalHeight);
				g.drawLine(ovalWidth, height, width - ovalWidth, height);
				g.drawArc(0, height - ovalHeight * 2, ovalWidth * 2, ovalHeight * 2, 180, 90);
				g.drawArc(width - ovalWidth * 2, height - ovalHeight * 2, ovalWidth * 2, ovalHeight * 2, -90, 90);
			}
		};
	}
	
	public void setVisible(boolean aFlag) {
		label.setVisible(aFlag);
		root.setVisible(aFlag);
	}
	
	public void setBounds(Rectangle b) {
		label.setBounds(b);
		root.setBounds(b);
	}
	
	public void setPane(JPanel pane) {
		label.setPane(pane);
		root.setPane(pane);
	}
	
	public void repaint() {
		root.repaint();
	}
	
}
