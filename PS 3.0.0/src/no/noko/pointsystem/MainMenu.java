package no.noko.pointsystem;

import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;

import util.Platform;
import util.Window;
import util.swing.NBButton;
import util.swing.gride.XStrip;
import util.swing.gride.YStrip;

public class MainMenu {
	
	private static final int INIT_WIDTH = 800;
	private static final int INIT_HEIGHT = 600;
	
	private static final int MIN_WIDTH = 800;
	private static final int MIN_HEIGHT = 600;
	
	private static Window window;

	private MainMenu(){}
	
	private static void create() {
		window = new Window("Background Editor", INIT_WIDTH, INIT_HEIGHT) {
			private static final long serialVersionUID = 1L;

			public void Init() {
				window = this;
					
//				setMaximumSize(new Dimension(MAX_WIDTH, MAX_HEIGHT));
				setMinimumSize(new Dimension(MIN_WIDTH, MIN_HEIGHT));
				
				textfeldSets.setFontSize(10);
				
				
				XStrip root = new XStrip();
				
				NBButton b1 = new NBButton(this);
				root.append(b1);
				
				NBButton b2 = new NBButton(this);
				root.append(b2);
				
				NBButton b3 = new NBButton(this);
				root.append(b3);
				
				this.getFrameBox().setBoxObject(root);
				
				
				setVisible(true);
				panel.setBackground(Color.DARK_GRAY);
			}

			public void ButtonAction(NBButton b) {

			}

			public void onCloseing() {
			}
		};

	}
	
	@SuppressWarnings("unused")
	private class LivePanel extends NBPanel {
		
		public LivePanel(Window window) {
			super();
			
			new NBButton(window);
			
			
		}
		
	}
	

	public static void Open(boolean viwe) {
		if (window == null)
			create();
		else
			window.setVisible(viwe);
	}
	
	public static void main(String[] args) {
		new Platform();
		MainMenu.Open(true);
	}

}
