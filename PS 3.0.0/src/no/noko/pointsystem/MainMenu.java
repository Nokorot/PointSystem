package no.noko.pointsystem;

import java.awt.Color;
import java.awt.Dimension;

import util.Platform;
import util.Window;
import util.swing.NBButton;
import util.swing.gride.BoxObject;
import util.swing.gride.DXStrip;
import util.swing.gride.YStrip;
import util.swing.tabLabel.TabLayout;
import util.swing.tabLabel.TabLayout.RoundRecTabLayout;;

public class MainMenu {

	public static Color A = Color.BLACK;
	public static Color B = new Color(50, 50, 50);//Color.DARK_GRAY;
	public static Color C = new Color(70, 70, 70);//Color.GRAY;
	public static Color D = Color.LIGHT_GRAY;
	public static Color E = Color.WHITE;
	
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
				//this.panel2.buttonSets.tColor = Color.BLACK;
					
//				setMaximumSize(new Dimension(MAX_WIDTH, MAX_HEIGHT));
				setMinimumSize(new Dimension(MIN_WIDTH, MIN_HEIGHT));
				
				panel2.buttonSets.setBColor(MainMenu.B);
				panel2.buttonSets.setTColor(MainMenu.E);
				panel2.textfeldSets.setFontSize(10);
				
				DXStrip root = new DXStrip(this);
				
				YStrip y = new YStrip();
				y.append(new ToolBar(this), 1, 0).setInsets(2, 2, 0, 0);;
				
				DXStrip x = new DXStrip(this);
				x.append(new ContentPanel(this), 1, 0).setInsets(0, 2, 2, 0);
				x.append(new MainPanel(this), 2).setInsets(0, 0, 2, 0);
				y.append(x, 3);
				
				root.append(y, 3);
				root.append(new LivePanel(this), 1, 0).setLeftInset(0);
				
				this.getFrameBox().setBoxObject(root);
					
				setVisible(true);
				panel2.setBackground(MainMenu.C);
			}
			public void ButtonAction(NBButton b) {
			}
			public void onCloseing() {
			}
		};

	}
	
	public static TabLayout getTabLabelLayout() {
		RoundRecTabLayout l = new RoundRecTabLayout(Color.CYAN, Color.BLACK, 2, 2);
		l.selectedButtonColor = MainMenu.B;
		l.unselectedButtonColor = MainMenu.C;
		l.background = MainMenu.B;
		return l;
	}
	
	static class MainPanel extends NBButton implements BoxObject {
		private static final long serialVersionUID = 1L;

		public MainPanel(Window window) {
			super(window);
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
