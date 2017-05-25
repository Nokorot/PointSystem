package no.nokorot.pointsystem.Windows;

import static no.nokorot.pointsystem.Element.Team.MAX_TEAMS;
import static no.nokorot.pointsystem.PSData.backgroundImage;
import static no.nokorot.pointsystem.PSData.backgroundScaleType;
import static no.nokorot.pointsystem.PSData.logo;
import static no.nokorot.pointsystem.PSData.logoScaleType;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import com.thecherno.raincloud.serialization.RCObject;
import com.thecherno.raincloud.serialization.RCString;

import no.nokorot.pointsystem.PointSystem;
import no.nokorot.pointsystem.Element.NamedTeamMenu;
import util.Window;
import util.handelers.ImageHandeler.ScaleType;
import util.math.Maths;
import util.swing.Docker;
import util.swing.Label;
import util.swing.NBButton;
import util.swing.TextField;
import util.swing.gride.Box;
import util.swing.gride.BoxObject;
import util.swing.gride.XStrip;
import util.swing.gride.YStrip;

public class MainMenu {

	public final static int Width = 600, Height = 200;
	public final static int HeightFix = 0;

	public static MySwitch font, backgornd, size; // ,nullNBButton;
//	private static NBButton Hide, Logo, Black, Clear;
//	private static NBButton ResetPoints, ResetNames;
	
	private static String defaultName = "Team %i";
	
	public static TextField GivenPoints;

	public static Window window;
	public static Docker docker;
	
	public static NamedTeamMenu[] Teams = new NamedTeamMenu[MAX_TEAMS];
	
	public static Box TeamToolBox = new Box();
	public static Box TeamBox = new Box();
	private static LocalTeamPanel localTeamPanel;
	private static OnlineTeamPanel onlineTeamPanel;
	
	private static boolean viwe = false;
	
	public static BufferedImage[] icons = PointSystem.icons;

	private MainMenu() {
	}

	public static void create() {
		load();
		window = localWindow();
		
		// window.gridInsets.set(10, 10, 10, 10);
		LiveWindow.create(window, PointSystem.Logo);
		localTeamPanel = new LocalTeamPanel(window, Teams);
		onlineTeamPanel = new OnlineTeamPanel(window, Teams);
		currentAmountOfTeams = 2;
		setOnline(false);

		LiveWindow.setLogo(PointSystem.Logo, ScaleType.TILLPASS);
		
		docker = new Docker();
		docker.registerDock(window);
		docker.setLayout(Docker.STICKY_LAYOUT);
//		docker.setComponentMovedReactTime(50);
	}

	private static void close() {	
		save();
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

	private static boolean Online = false;
	public static void setOnline(boolean online){
		Online = online;
		if (Online){
			localTeamPanel.toolBox.setVisible(false);
			onlineTeamPanel.setOnline();
			onlineTeamPanel.toolBox.setVisible(true);
		}else{
			onlineTeamPanel.toolBox.setVisible(false);
			localTeamPanel.setLocal();
			localTeamPanel.toolBox.setVisible(true);
		}
		setTeams(currentAmountOfTeams);
	}
	
	public static int currentAmountOfTeams;
	public static void setTeams(int teams){
		currentAmountOfTeams = teams;
		if (Online) 
			onlineTeamPanel.SetUp(teams);
		else
			localTeamPanel.SetUp(teams);
	}
	
	static class MySwitch extends NBButton implements ActionListener {
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
				setDefaultCloseOperation(3);

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
				
				root.append(TeamToolBox, 1.2, 0);
				TeamToolBox.setInsets(3, 5, 3, 5);
				
				TeamBox = new Box();
				root.append(TeamBox, 0, 1);
				
				setVisible(true);
				this.getFrameBox().setBoxObject(root);
				
				panel.setBackground(Color.DARK_GRAY);
			}
			
			public void ButtonAction(NBButton b) {
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
				else if (b.code != null){
					switch (b.code) {
					case "setD": SetDefaultName.Open();
					case "resp":
						for (NamedTeamMenu team : Teams) 
							if(team != null)
								team.setPoints(0);
						break;
					case "resn":
						int i = 1;
						for (NamedTeamMenu team : Teams)
							if(team != null){
								team.setName(defaultName.replace("%i", "" + i++));
//								team.setName("Team " + ++i);
							}
						break;
					case "online":
						setOnline(!Online);
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
					try { t = Maths.clamp(Integer.parseInt(tf.getText()), 1, Teams.length);
					} catch (Exception e) { return; }
					setTeams(t);
					tf.setText(t + "");
					break;
				}
			}
			
			public void onCloseing() {
				close();
			}
		};
	}
	
	private static void save() {
		RCObject out = new RCObject("Main menu");
		out.addString(new RCString("dName", defaultName));
		PointSystem.database.addObject(out);
	}

	private static void load() {
		RCObject in = PointSystem.database.getObject("Main menu");
		if (in == null)
			return;
		
		defaultName = in.getString("dName");
		
	}
	
	private static class SetDefaultName extends Window {
		private static final long serialVersionUID = 1L;
		
		private static SetDefaultName object;
		
		public static void Open() {
			if (object == null)
				object = new SetDefaultName();
			object.setVisible(true);
		}

		private TextField dName;
		
		public SetDefaultName() {
			super("Set Default Names", 260, 140);
			
			setResizable(false);
			
			YStrip root = new YStrip();
			
			XStrip addons = new XStrip();
			addons.append(new NBButton(this, "index", "index"));
			addons.append((BoxObject) null, 3);
			root.append(addons);
			
			root.append(dName = new TextField(this, defaultName, "dName"));
			
			XStrip x = new XStrip();
			x.append(new NBButton(this, "cansel", "cansel"));
			x.append(new NBButton(this, "done", "done"));
			root.append(x);
			
			setVisible(true);
			getFrameBox().setInsets(10);
			getFrameBox().setBoxObject(root);
		}
		
		@Override
		public void ButtonAction(NBButton button) {
			
			switch (button.code) {
			case "index":
				dName.setText(dName.getText() + "%i");
				break;
			case "cansel":
				this.setVisible(false);
				break;
			case "done":
				// TODO: Check if the text is valid
				defaultName = dName.getText();
				this.setVisible(false);
				break;
			}
		}
		
		
	}
	
	

}