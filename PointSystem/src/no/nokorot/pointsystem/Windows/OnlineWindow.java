package no.nokorot.pointsystem.Windows;

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
import java.util.Arrays;
import java.util.Random;
import java.util.StringJoiner;

import javax.swing.plaf.synth.SynthSpinnerUI;

import no.nokorot.pointsystem.PSData;
import no.nokorot.pointsystem.Element.Team;
import util.Window;
import util.handelers.FileHandler;
import util.handelers.ImageHandeler;
import util.handelers.ImageHandeler.ScaleType;
import util.swing.Label;
import util.swing.NBButton;
import util.swing.NBTextField;
import util.swing.SwitchButton;
import util.swing.TextArea;
import util.swing.gride.XStrip;
import util.swing.gride.YStrip;

public class OnlineWindow {
	
	private static final String url = "https://pointsystemo.herokuapp.com/";
//	private static final String url = "http://127.0.0.1:5000/";
	private static long minTime = 1000;
	
	private static final int MIN_WIDTH = 600, MIN_HEIGHT = 400;
	
	private static Window window;

	private static NBTextField codeField;

	private static SwitchButton advanced;
	private static SwitchButton slides;
	
	public static boolean isOnline = false, inprosess = false;
	public static String psCode;
	
	private static Thread thread;
	private static boolean running = false;
	
	private OnlineWindow() {
	}

	public static void Open(boolean viwe) {
		if (window == null)
			create();

		window.setVisible(viwe);
	}
	
	public static void create() {
		window = new Window("Online Manager", 500, 120) {
			private static final long serialVersionUID = 1L;

			public void Init() {
				window = this;
				setResizable(false);
				
//				setMaximumSize(new Dimension(MAX_WIDTH, MAX_HEIGHT));
//				setMinimumSize(new Dimension(MIN_WIDTH, MIN_HEIGHT));

//				panel2.textfeldSets.setFontSize(10);
				panel2.labelSets.setTColor(Color.WHITE);
				panel2.labelSets.setFontSize(15);

				XStrip root = new XStrip();

				
				
				YStrip y;
				XStrip x = new XStrip();
//				NBButton online = new NBButton(window, "", "online");
//				online.setBackground(Color.CYAN);
//				online.setIcon(MainMenu.icons[1], ScaleType.TILLPASS);
//				x.append(online, 1/2.);
				
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
				
//				advanced = new SwitchButton(window, SwitchButton.COLOR, window.panel2.buttonSets.background, Color.CYAN);
//				advanced.setText("Advanced");
//				advanced.addActionListener((ActionEvent e) -> {
//					if (running)
//						enableAdvancedMode(advanced.activated);
//				});
//				x.append(advanced);
				
//				slides = new SwitchButton(window, SwitchButton.COLOR, window.panel2.buttonSets.background, Color.CYAN){
//					protected void onAction() {
//						enableSlideMode(slides.getActiveIndex() == 1);
//					}
//				};
//				slides.setText("Slides");
//				x.append(slides);
				
//				y = new YStrip();
//				y.append(new Label(window, "Team Amount"));
//				y.append(teamAmount = new NBTextField(window, "2", "teams"), 1.8);
//				x.append(y);
				
				y = new YStrip();
				y.append(new Label(window, "Code (4-10 sym.)"));
				y.append(codeField = new NBTextField(window, "", "online-code"), 1.8);
				x.append(y);
				
				SwitchButton GoOnline = new SwitchButton(window, SwitchButton.COLOR, window.panel2.buttonSets.background, Color.CYAN);
				GoOnline.setText("Go Online");
//						window, "", "Go Online");
//				online.setIcon(MainMenu.icons[1], ScaleType.TILLPASS);
				GoOnline.addActionListener((ActionEvent e) -> GoOnline());
				x.append(GoOnline, 1);
				
				root.append(x);
				
				
				this.getFrameBox().setBoxObject(root);

				panel2.setBackground(Color.DARK_GRAY);
			}

			public void ButtonAction(NBButton button) {
			}
			
			public void onCloseing() {
				MainMenu.online.actionPerformed(null);
//				save();
			}
		};

//		load();
	}
	
	private static void GoOnline(){
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
	
	private static void openOnlineConnection() {
//		for(NamedTeamMenu team : Teams){
//			if (team != null)
//				team.setEditable(false);
//		}
//		teamsBox.update();
//		SetUp(MainMenu.currentAmountOfTeams);

		LocalTeamPanel.teamAmount.setEditable(false);
		codeField.setEditable(false);
		
		
		running = true;
		thread = new Thread(()-> onlineConnection(), "Online Connection");
		thread.start();
	}
	
	private static void endOnlineConnection() {
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
//		for(NamedTeamMenu team : Teams){
//			if (team != null)
//				team.setEditable(true);
//		}

		LocalTeamPanel.teamAmount.setEditable(true);
		codeField.setEditable(true);
	}
	
	private static void onlineConnection(){
		psCode = codeField.getText();
		if (psCode.length() < 4){
			Random random = new Random();
			psCode = String.valueOf(random.nextInt(99999) + 1000);
			codeField.setText(psCode);
		}
		
		
		String[] slist = new String[MainMenu.currentAmountOfTeams];
		for (int i = 0; i < MainMenu.currentAmountOfTeams; i++) 
			slist[i] = ""+Team.TEAMS[i].points;
		String currentPoints = String.join(",", slist);
		
		psRead("make", psCode, currentPoints);

//		enableAdvancedMode(advanced.getActiveIndex() == 1);
		if (PSData.SlideShowEnaled)
			enableSlideMode(true);
		
		double lu_time = 0;
		long last = System.currentTimeMillis();
		try {
		while (running){
			String responce  = psRead("get", psCode, lu_time);
			if (responce == null){
				System.err.println("Responce, null!!!");
				continue;
			}
			
			String[] lines = responce.split("\\|\\|");
			String[][] data = new String[lines.length][]; int count = 0;
			for (String l : lines){
				data[count] = l.split("\\|");
				if (count > 0){
					lu_time = Double.parseDouble(data[count][0]);
					if (SlideModeEnabled)
						SlideShowWindow.setSelectedPage(Integer.parseInt(data[count][2]));
					System.out.println(Arrays.toString(data[count]));
				}
				count++;
			}
			
//			String[] data0 = responce.split("\\|");
			String[] pointsS = data[0][0].split(",");
			int[] points = new int[pointsS.length];
			for (int i = 0; i < pointsS.length; i++){
				try {
					points[i] = Integer.parseInt(pointsS[i]);
				}catch (NumberFormatException e) {
					e.printStackTrace();
				}
			}
			for (int i = 0; i < Math.min(points.length, MainMenu.currentAmountOfTeams); i++){
				Team.TEAMS[i].setPoints(points[i]);
			}
//			if (SlideModeEnabled){
//				
//				SlideShowWindow.setSelectedPage(Integer.parseInt(data[0][2]));
//			}
			
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
	private static void enableAdvancedMode(boolean enable) {
		if (enable == AdvancedModeEnabled)
			return;
		psRead("info", psCode, "advancedMode", enable);
		if (enable){
		}
	}
	
	public static boolean SlideModeEnabled = false; 
	public static void enableSlideMode(boolean enable) {
		if (!isOnline) return;
		SlideModeEnabled = enable;
		psRead("info", psCode, "slideMode", enable ? 1 : 0);
		if (enable){
			SlideShowWindow.syncSlides();
		}
	}
	
	public static String psRead(String requestType, Object... data){
		String[] slist = new String[data.length];
		for (int i = 0; i < data.length; i++) 
			slist[i] = data[i].toString();
		return read(url, "pointsystem/"+requestType+"/" + String.join("|", slist));
	}
	
	public static String read(String url, String extention){
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
		}
		return null;
	}
	
	private static final String base = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ-.";
	public static String toMyBase(byte[] data) {
		
		StringJoiner sj = new StringJoiner("");
		for (int i = 0; i < Math.ceil(data.length / 3.); i++){
			int index = 0;
			index |= ((int) data[i*3]) << 16;
			if (data.length > i*3+1)
				index |= ((int) data[i*3+1]) << 8;
			if (data.length > i*3+2)
				index |= ((int) data[i*3+2]) << 0;
//			int index = ((int) data[i*4+1]) << 16 | ((int) data[i*4+2]) << 8 | (int) data[i*4+3];
		
			System.out.println((index >> 0) & 0x3f);
			sj.add(""+base.charAt((index >> 18) & 0x3f));
			sj.add(""+base.charAt((index >> 12) & 0x3f));
			sj.add(""+base.charAt((index >> 6) & 0x3f));
			sj.add(""+base.charAt((index >> 0) & 0x3f));
		}
		
		return sj.toString();
	}
	
//	public static void main(String[] args) {
//		
//		String[] g = "k,hfgg |l".split("\\|");
//		System.out.println(Arrays.toString(g));
//		
//		
//		/*byte[] data = {1, 0, 0, 0};
//		
//		System.out.println(Arrays.toString(data));
//		
//		System.out.println(toMyBase(data));*/
//	}
	
}
