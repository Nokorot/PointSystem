package no.nokorot.pointsystem.Windows;

import static no.nokorot.pointsystem.Element.Team.TEAMS;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
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
import util.swing.SwitchButton;
import util.swing.TextArea;
import util.swing.TextField;
import util.swing.gride.Box;
import util.swing.gride.BoxObject;
import util.swing.gride.Strip;
import util.swing.gride.XStrip;
import util.swing.gride.YStrip;

public class OnlineTeamPanel {
	
	private static final String url = "https://pointsystemo.herokuapp.com/";
//	private static final String url = "http://127.0.0.1:5000/";
	private static long minTime = 250;
	
	private static int Height = MainMenu.Height;
	private static int AdvanceHeight = 150;
	private static int HeightFix = 0;
	
	public BoxObject toolBox;
	public Box teamsBox = new Box();
	
	private Window window;
	private NamedTeamMenu[] Teams;

	private TextField teamAmount;
	private TextField codeField;
	
	private boolean isOnline = false, inprosess = false;
	
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
		String code = codeField.getText();
		if (code.length() < 4){
			Random random = new Random();
			code = String.valueOf(random.nextInt(99999) + 1000);
			codeField.setText(code);
		}
		
		String currentPoints = "" + Teams[0].getPoints();
		for (int i = 1; i < MainMenu.currentAmountOfTeams; i++){
			currentPoints += "-" + Teams[i].getPoints();
		}
		
		
		read(url, "make-pointsystem/"+code+"/"+currentPoints);
		
		long last = System.currentTimeMillis();
		try {
		while (running){
			String data = read(url, "get-pointsystem/"+code);
			String[] pointsS = data.split(", ");
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
		x.append(online, 3/8.);
		
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
						try {
							ta.setText(FileHandler.readLocalFile("info.txt"));
						} catch (IOException e) {
							e.printStackTrace();
						}
						root.append(ta, 2);
						
						Label qr = new Label(this);
						qr.setIcon(ImageHandeler.load("/onlineQR.png"), ScaleType.TILLPASS);
						root.append(qr);
						
						this.getFrameBox().setBoxObject(root);
						
						setVisible(true);
					}
				};
			}
		});
		x.append(info, 3/8.);
		
		y = new YStrip();
		y.append(new Label(window, "Team Amount"));
		y.append(teamAmount = new TextField(window, "2", "teams"), 1.8);
		x.append(y);
		
		y = new YStrip();
		y.append(new Label(window, "Code (4-10 sym.)"));
		y.append(codeField = new TextField(window, "", "online-code"), 1.8);
		x.append(y);

		SwitchButton GoOnline = new SwitchButton(window, SwitchButton.COLOR, window.buttonSets.bColor, Color.CYAN);
		GoOnline.setText("Go Online");
//				window, "", "Go Online");
//		online.setIcon(MainMenu.icons[1], ScaleType.TILLPASS);
		GoOnline.addActionListener((ActionEvent e) -> GoOnline());
		x.append(GoOnline, 1);
		
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
