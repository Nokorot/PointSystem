package no.nokorot.pointsystem.Windows;

import java.awt.GraphicsDevice;
import java.awt.Rectangle;

import no.nokorot.pointsystem.PSData;
import util.Window;
import util.math.Maths;
import util.swing.Docker;
import util.swing.Label;
import util.swing.PopDownTextField;
import util.swing.NBTextField;
import util.swing.gride.BoxGrid;

@SuppressWarnings({ "serial" })
public class EditSize {

	private final static int MIN_WIDTH = 200;
	private final static int MIN_HEIGHT = 100;

	private static Window window;

	private static PopDownTextField screen;
	private static NBTextField x, y, width, height;

	private static void create() {
		GraphicsDevice[] devices = PSData.environment.getScreenDevices();
		
		window = new Window("Size Editor", 200, 200) {

			public void Init() {
				panel2.labelSets.setFontSize(20f);

				setVisible(true);
				BoxGrid grid = getGrid(1, new double[] { 1, .75, 1, .75, 1 });

				screen = new PopDownTextField(this);
				grid.getBox(0).setComponent(screen);

				Label posL = new Label(this, "Position");
				posL.setHorizontalAlignment(Label.LEFT);
				grid.getBox(1).setComponent(posL);

				BoxGrid g0 = grid.getBox(2).getInsideGrid(new double[] { 3, 1, 3 }, 1);
				x = new NBTextField(this);
				g0.getBox(0).setComponent(x);
				y = new NBTextField(this);
				g0.getBox(2).setComponent(y);
				g0.getBox(1).setComponent(new Label(this, ","));

				Label sizeL = new Label(this, "Size");
				sizeL.setHorizontalAlignment(Label.LEFT);
				grid.getBox(3).setComponent(sizeL);

				BoxGrid g1 = grid.getBox(4).getInsideGrid(new double[] { 3, 1, 3 }, 1);
				width = new NBTextField(this);
				height = new NBTextField(this);
				g1.getBox(0).setComponent(width);
				g1.getBox(2).setComponent(height);
				g1.getBox(1).setComponent(new Label(this, "x"));
			}

			public void PopDownTextFieldAction(PopDownTextField source) {
				if (source == screen) {
					String selected = source.getSelectedItem();
					if (selected.startsWith("Screen")) {
						int i = Integer.parseInt(selected.substring("Screen ".length(), selected.length()));
						setCurentBounds(devices[i].getDefaultConfiguration().getBounds());
						setEditable(false);
						PSData.screen = i;
					} else {
						setCurentBounds(PSData.custumScreenBounds);
						setEditable(true);
					}
					PSData.screen = source.getSelectedIndex() - 1;
					LiveWindow.update();
				}
			}

			public void TextFieldAction(NBTextField field) {

				int ax = Integer.parseInt(x.getText());
				int ay = Integer.parseInt(y.getText());
				int awidth = Integer.parseInt(width.getText());
				int aheight = Integer.parseInt(height.getText());

				awidth = Math.max(awidth, MIN_WIDTH);
				aheight = Math.max(aheight, MIN_HEIGHT);
				
				PSData.custumScreenBounds.setBounds(ax, ay, awidth, aheight);
				setCurentBounds(PSData.custumScreenBounds);
				
				LiveWindow.update();
			}

			protected void onCloseing() {
				MainMenu.size.actionPerformed(null);
			}
		};

		screen.addElement("Custom");
		for (int i = 0; i < devices.length; i++)
			screen.addElement("Screen " + i);

		screen.setSelectedIndex(Maths.clamp(PSData.screen + 1, 0, devices.length));

		MainMenu.docker.registerDockee(window, Docker.WEST_DOCKED);
	}

	private static void setEditable(boolean b){
		x.setEditable(b);
		y.setEditable(b);
		width.setEditable(b);
		height.setEditable(b);
	}
	
	private static void setCurentBounds(Rectangle r) {
		x.setText(r.x + "");
		y.setText(r.y + "");
		width.setText(r.width + "");
		height.setText(r.height + "");
	}
	
	public static void Open(boolean viwe) {
		if (window == null)
			create();

		window.setVisible(viwe);
	}

}
