package no.nokorot.pointsystem.Windows;

import static no.nokorot.pointsystem.Element.Team.MAX_TEAMS;
import static no.nokorot.pointsystem.Element.Team.TEAMS;
import static no.nokorot.pointsystem.PSData.backgroundImage;
import static no.nokorot.pointsystem.PSData.backgroundScaleType;
import static no.nokorot.pointsystem.PSData.logo;
import static no.nokorot.pointsystem.PSData.logoScaleType;

import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import no.nokorot.pointsystem.PointSystem;
import no.nokorot.pointsystem.Element.NamedTeamMenu;
import util.Window;
import util.handelers.ImageHandeler.ScaleType;
import util.math.Maths;
import util.swing.Button;
import util.swing.Docker;
import util.swing.Label;
import util.swing.TextField;
import util.swing.gride.Box;
import util.swing.gride.BoxObject;
import util.swing.gride.Grid;
import util.swing.gride.Strip;
import util.swing.gride.XStrip;
import util.swing.gride.YStrip;

public class MainMenu {

	private static int Width = 600, Height = 200, AdvanceHeight = 150;
	private static int HeightFix;

	public static MySwitch font, backgornd, size; // ,nullButton;
//	private static Button Hide, Logo, Black, Clear;
//	private static Button ResetPoints, ResetNames;
	
	public static TextField GivenPoints;

	public static Window window;
	public static Docker docker;
	
	private static Box TeamBox;
	
	private static boolean viwe = false;

	private MainMenu() {
	}

	public static void create() {
		window = localWindow();
		
		// window.gridInsets.set(10, 10, 10, 10);
		LiveWindow.create(window, PointSystem.Logo);
		SetUp(2);

		LiveWindow.setLogo(PointSystem.Logo, ScaleType.TILLPASS);
		
		docker = new Docker();
		docker.registerDock(window);
		docker.setLayout(Docker.STICKY_LAYOUT);
//		docker.setComponentMovedReactTime(50);
	}

	private static void close() {
		window.setVisible(false);
		LiveWindow.setVisible(false);
		PointSystem.close();
	}

	public static void Open() {
		if (window == null)
			create();

		window.setVisible(true);
		
		if (backgroundImage != null)
			LiveWindow.setBackground(backgroundImage, backgroundScaleType);
		if (logo != null)
			LiveWindow.setLogo(logo, logoScaleType);

		LiveWindow.setVisible(viwe);
		window.setVisible(true);
		updateLiveWindow();
		LiveWindow.render();
	}

	public static void updateLiveWindow() {
		if (window == null)
			return;
		LiveWindow.setBackground(backgroundImage, backgroundScaleType);
		LiveWindow.setLogo(logo, logoScaleType);
		LiveWindow.update();
	}

	// *********** Set Up **************
	private static NamedTeamMenu[] Teams = new NamedTeamMenu[MAX_TEAMS];
	
	private static void SetUp(int teams) {
		LiveWindow.SetUp(teams);		
		
		YStrip root = new YStrip();
		
		int A = teams, i = 0;
		
		while (A - i > 4)
			i = add(root, i, 3);
		if (A - i > 3)
			i = add(root, i, 2);
		if (A - i > 2)
			i = add(root, i, 3);
		if (A - i > 1)
			i = add(root, i, 2);
		if (A - i > 0)
			add(root, i, 1);
		
		for (int k = A; k < Teams.length; k++)
			if (Teams[k] != null)
				Teams[k].setVisible(false);
		
		int height = root.size();
		
		TeamBox.setBoxObject(root);

		System.out.println(Height + height * AdvanceHeight + HeightFix);
		window.setHeight(Height + height * AdvanceHeight + HeightFix);
//		window.setLocationRelativeTo(null);
		window.repaint();
	}

	private static int add(Strip root, int i, int l){
		XStrip x = new XStrip();
		
		for (int k = 0; k < l; k++){
			x.append((BoxObject) null, 1);
			if (Teams[i+k] == null)
				Teams[i+k] = new NamedTeamMenu(window, TEAMS[i+k]);
			Teams[i+k].setVisible(true);
			x.append(Teams[i+k], l*l*2);
		}
		x.append((BoxObject) null, 1);
		root.append(x);
		
		return i + l;
	}


	static class MySwitch extends Button implements ActionListener {
		private static final long serialVersionUID = 1L;

		public boolean Active = false;
		
		public Color activeColor = Color.CYAN;
		public Color deactiveColor = Color.LIGHT_GRAY;
		
		public MySwitch(Window window, String text, String code) {
			super(window, text, code);
			this.addActionListener(this);
			
		}

		public void actionPerformed(ActionEvent e) {
			Active = !Active;
			if (Active) this.setBackground(activeColor);
			else this.setBackground(deactiveColor);
		}
		
	}
	
	private static Window localWindow() {
		return new Window("PointSystem", Width, Height) {
			private static final long serialVersionUID = 1L;

			public void Init() {
				setResizable(false);

				textfeldSets.setFont(new Font("Arial", Font.BOLD, 32));
				labelSets.setFontSize(14);
				
				YStrip root = new YStrip();
				XStrip x;
				
				x = new XStrip();
				x.append(size = new MySwitch(this, "Size", "size"));
				x.append(backgornd = new MySwitch(this, "Background", "bg"));
				x.append(font = new MySwitch(this, "Font", "font"));
				root.append(x, 1, 0);
				
				x = new XStrip();
				MySwitch hide = new MySwitch(this, "Hide", "hide");
				hide.actionPerformed(null);
				x.append(hide);
				x.append(new MySwitch(this, "Logo", "logo"));
				x.append(new MySwitch(this, "Black", "black"));
				x.append(new MySwitch(this, "Clear", "clear"));
				root.append(x, 1, 0);
				
				Label l = new Label(this);
				l.setOpaque(true);
				l.setBackground(Color.GRAY);
				root.append(l, 0.2, 0);
				
				YStrip y;
				x = new XStrip();
				y = new YStrip();
				y.append(new Label(this, "Reset"));
				y.append(new Button(this, "Points", "resp"), 1.8);
				x.append(y);
				y = new YStrip();
				y.append(new Label(this, "Reset"));
				y.append(new Button(this, "Names", "resn"), 1.8);
				x.append(y);
				y = new YStrip();
				y.append(new Label(this, "Given Points"));
				y.append(GivenPoints = new TextField(this, "1", "points"), 1.8);
				x.append(y);
				y = new YStrip();
				y.append(new Label(this, "Team Amount"));
				y.append(new TextField(this, "2", "teams"), 1.8);
				x.append(y);
				root.append(x, 1.2, 0).setInsets(3, 5, 3, 5);
				
				TeamBox = new Box();
				root.append(TeamBox, 0, 1);
				
				setVisible(true);
				this.getFrameBox().setBoxObject(root);
				
				panel.setBackground(Color.DARK_GRAY);
			}
			
			public void ButtonAction(Button b) {
				if (b == null)
					return;

				if ( b instanceof MySwitch){
					MySwitch swich = (MySwitch) b;
					switch (b.code) { 
					case "size":
						EditSize.Open(swich.Active); break;
					case "bg":
						BackgroundEditor.Open(swich.Active); break;
					case "font":
						FontEditor2.Open(swich.Active); break;
						
					case "hide":
						LiveWindow.setVisible(!swich.Active);
						window.toFront(); break;
						
					case "logo":
						LiveWindow.setMode(LiveWindow.LogoMode); break;
						
					case "black":
						LiveWindow.setMode(LiveWindow.BlackMode); break;
						
					case "clear":
						LiveWindow.setCleared(swich.Active); break;
					}
				}
				else { switch (b.code) {
					case "resp":
						for (NamedTeamMenu team : Teams) 
							if(team != null)
								team.setPoints(0);
						break;
					case "resn":
						int i = 0;
						for (NamedTeamMenu team : Teams) 
							if(team != null)
								team.setName("Team " + ++i);
						break;
					}
				}
			


				updateLiveWindow();
			}

			@Override
			public void TextFieldAction(TextField tf) {
				switch (tf.code) {
				case "points":
					break;
				case "teams":
					int t = 0;
					try { t = Integer.parseInt(tf.getText());
					} catch (Exception e) { return; }
					SetUp(t);
					tf.setText(Maths.clamp(t, 1, Teams.length) + "");
					break;
				}
			}
			
			public void onCloseing() {
				close();
			}
		};
	}

}