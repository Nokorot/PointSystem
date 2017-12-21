package no.nokorot.pointsystem.Windows;

import static no.nokorot.pointsystem.Element.Team.TEAMS;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Random;

import no.nokorot.pointsystem.Element.NamedTeamMenu;
import util.Window;
import util.handelers.FileHandler;
import util.handelers.ImageHandeler;
import util.handelers.ImageHandeler.ScaleType;
import util.swing.Label;
import util.swing.NBButton;
import util.swing.NBTextField;
import util.swing.SwitchButton;
import util.swing.TextArea;
import util.swing.gride.Box;
import util.swing.gride.BoxObject;
import util.swing.gride.Strip;
import util.swing.gride.XStrip;
import util.swing.gride.YStrip;

public class OnlineTeamPanel {
	
//	private static final String url = "https://pointsystemo.herokuapp.com/";
	private static final String url = "http://127.0.0.1:5000/";
	private static long minTime = 1000;
	
	private static int Height = MainMenu.Height;
	private static int AdvanceHeight = 150;
	private static int HeightFix = 0;
	
	public BoxObject toolBox;
	public Box teamsBox = new Box();
	
	private Window window;
	private NamedTeamMenu[] Teams;

	private NBTextField teamAmount;
	private NBTextField codeField;
	
	private SwitchButton advanced;
	
	private boolean isOnline = false, inprosess = false;
	private static String psCode;
	
	public OnlineTeamPanel(Window window, NamedTeamMenu[] teamMenues) {
		this.window = window;
		this.Teams = teamMenues;
		initToolBox();
	}

	public void setOnline() {
		MainMenu.TeamBox.setBoxObject(teamsBox);
		MainMenu.TeamToolBox.setBoxObject(toolBox);
		MainMenu.TeamToolBox.setInsets(3, 5, 3, 5);
		
		teamAmount.setText("" + MainMenu.currentAmountOfTeams);
	}
	
	private Thread thread;
	private boolean running = false;
	
	private void GoOnline(){
		if (inprosess)
			return;
		inprosess = true;
		if (isOnline){
			endOnlineConnection();
		}else{
			openOnlineConnection();
		}
		inprosess = false;
		isOnline = !isOnline;
	}
	
	private void openOnlineConnection() {
		for(NamedTeamMenu team : Teams){
			if (team != null)
				team.setEditable(false);
		}
//		teamsBox.update();
		SetUp(MainMenu.currentAmountOfTeams);

		teamAmount.setEditable(false);
		codeField.setEditable(false);
		
		
		running = true;
		thread = new Thread(()-> onlineConnection(), "Online Connection");
		thread.start();
	}
	
	private void endOnlineConnection() {
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		for(NamedTeamMenu team : Teams){
			if (team != null)
				team.setEditable(true);
		}

		SetUp(MainMenu.currentAmountOfTeams);
		teamAmount.setEditable(true);
		codeField.setEditable(true);
	}
	
	private void onlineConnection(){
		psCode = codeField.getText();
		if (psCode.length() < 4){
			Random random = new Random();
			psCode = String.valueOf(random.nextInt(99999) + 1000);
			codeField.setText(psCode);
		}
		
		String currentPoints = "" + Teams[0].getPoints();
		for (int i = 1; i < MainMenu.currentAmountOfTeams; i++){
			currentPoints += "," + Teams[i].getPoints();
		}
		
		
		psRead("make", psCode, currentPoints);
		
		if (advanced.activated)
			enableAdvancedMode(true);
		
		long last = System.currentTimeMillis();
		try {
		while (running){
			String data = psRead("get", psCode);
			String[] pointsS = data.split(",");
			int[] points = new int[pointsS.length];
			for (int i = 0; i < pointsS.length; i++){
				try {
					points[i] = Integer.parseInt(pointsS[i]);
				}catch (NumberFormatException e) {
					e.printStackTrace();
				}
			}
			for (int i = 0; i < Math.min(points.length, MainMenu.currentAmountOfTeams); i++){
				Teams[i].setPoints(points[i]);
			}
			
			long during = System.currentTimeMillis() - last;
			if (during < minTime)
				Thread.sleep(minTime - during);
			last = System.currentTimeMillis();
		}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private static boolean AdvancedModeEnabled = false; 
	private void enableAdvancedMode(boolean enable) {
		if (enable == AdvancedModeEnabled)
			return;
		psRead("info", psCode, "advancedMode", enable);
		if (enable){
		}
	}
	
	public String psRead(String requestType, Object... data){
		String[] slist = new String[data.length];
		for (int i = 0; i < data.length; i++) 
			slist[i] = data[i].toString();
		return read(url, "pointsystem/"+requestType+"/" + String.join("|", slist));
	}
	
	public String read(String url, String extention){
		URL oracle;
		try {
			oracle = new URL(url + extention);
			URLConnection yc = oracle.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
			StringBuilder builder = new StringBuilder();
			String line;
			while ((line = in.readLine()) != null)
				builder.append(line);
			in.close();
			return builder.toString();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
//			e.printStackTrace();
		}
		return null;
	}
	
	private void initToolBox(){
		YStrip y;
		XStrip x = new XStrip();
		NBButton online = new NBButton(window, "", "online");
		online.setBackground(Color.CYAN);
		online.setIcon(MainMenu.icons[1], ScaleType.TILLPASS);
		x.append(online, 1/2.);
		
		boolean isInfoOpen = false; 
		NBButton info = new NBButton(window, "", "info");
		info.setIcon(MainMenu.icons[2], ScaleType.TILLPASS);
		info.addActionListener((ActionEvent e) -> {
			if (! isInfoOpen ){
				new Window("How to use PS Onlnie", 420, 550){
					private static final long serialVersionUID = 1L;
					public void Init() {
						YStrip root = new YStrip();
						
						TextArea ta;
						ta = new TextArea(this, true);
						ta.setEditable(false);
						String text;
						try {
							text = FileHandler.readLocalFile("info.txt");
							ta.setText(text);
						} catch (IOException e) {
							e.printStackTrace();
						}
						root.append(ta, 2);
						
						NBButton qr = new NBButton(this);
						qr.addActionListener((ActionEvent e) -> {
						    URI uri;
							try {
								uri = new URL(url).toURI();
							
						    
								Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
							    if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
							        try {
							            desktop.browse(uri);
							        } catch (Exception e1) {
							            e1.printStackTrace();
							        }
							    }
						    } catch (MalformedURLException | URISyntaxException e2) {
								e2.printStackTrace();
							}

							
							
						});
						qr.setBackground(Color.DARK_GRAY);
						qr.setIcon(ImageHandeler.load("/onlineQR.png"), ScaleType.TILLPASS);
						root.append(qr);
						
						this.getFrameBox().setBoxObject(root);
						
						setVisible(true);
					}
				};
			}
		});
		x.append(info, 1/2.);
		
		advanced = new SwitchButton(window, SwitchButton.COLOR, window.panel2.buttonSets.background, Color.CYAN);
		advanced.setText("Advanced");
		advanced.addActionListener((ActionEvent e) -> {
			if (running)
				enableAdvancedMode(advanced.activated);
		});
		x.append(advanced);
		
		SwitchButton GoOnline = new SwitchButton(window, SwitchButton.COLOR, window.panel2.buttonSets.background, Color.CYAN);
		GoOnline.setText("Go Online");
//				window, "", "Go Online");
//		online.setIcon(MainMenu.icons[1], ScaleType.TILLPASS);
		GoOnline.addActionListener((ActionEvent e) -> GoOnline());
		x.append(GoOnline, 1);
		
		y = new YStrip();
		y.append(new Label(window, "Team Amount"));
		y.append(teamAmount = new NBTextField(window, "2", "teams"), 1.8);
		x.append(y);
		
		y = new YStrip();
		y.append(new Label(window, "Code (4-10 sym.)"));
		y.append(codeField = new NBTextField(window, "", "online-code"), 1.8);
		x.append(y);

		
		
		toolBox = x;
	}
	
	public void SetUp(int teams) {
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
		
		teamsBox.setBoxObject(root);
		
		window.setHeight(Height + height * AdvanceHeight + HeightFix);
		window.repaint();
	}

	private int add(Strip root, int i, int l){
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
	
}
