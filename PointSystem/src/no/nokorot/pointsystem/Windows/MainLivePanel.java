package no.nokorot.pointsystem.Windows;

import static no.nokorot.pointsystem.Element.Team.MAX_TEAMS;
import static no.nokorot.pointsystem.Element.Team.TEAMS;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import no.nokorot.pointsystem.Element.NamedTeamMenu;
import util.Window;
import util.swing.NBButton;
import util.swing.gride.Box;
import util.swing.gride.BoxObject;
import util.swing.gride.XStrip;
import util.swing.gride.YStrip;

public class MainLivePanel extends YStrip {

	public static NamedTeamMenu[] Teams = new NamedTeamMenu[MAX_TEAMS];
	
	private Window window;
	private Box contentBox;
	
	private NBButton hideB, logoB, blackB, clearB;
	
	public MainLivePanel(Window window) {
		this.window = window;
		
		
		
		XStrip x = new XStrip();
		hideB = new MySwitch(window, "Hide", "hide");
//		hideB.actionPerformed(null);
		x.append(hideB);
		hideB.addActionListener((ActionEvent e) -> {
			LiveWindow.setVisible(!((MySwitch) e.getSource()).Active) ;
//			window.toFront(); 
		});
		x.append(logoB = new MySwitch(window, "Logo", "logo"));
		logoB.addActionListener((ActionEvent e) -> 
				LiveWindow.setMode(LiveWindow.LogoMode, !((MySwitch) e.getSource()).Active));
		x.append(blackB = new MySwitch(window, "Black", "black"));
		blackB.addActionListener((ActionEvent e) -> 
				LiveWindow.setMode(LiveWindow.BlackMode, !((MySwitch) e.getSource()).Active));
		x.append(clearB = new MySwitch(window, "Clear", "clear"));
		clearB.addActionListener((ActionEvent e) -> 
				LiveWindow.setMode(LiveWindow.ClearMode, !((MySwitch) e.getSource()).Active));

//		LiveWindow.setMode(LiveWindow.LogoMode, swich.Active); break;
//		
//	case "black":
//		LiveWindow.setMode(LiveWindow.BlackMode, swich.Active); break;
//		
//	case "clear":
//		LiveWindow.setMode(LiveWindow.ClearMode, swich.Active); break;
		
		this.append(x, 1, 0);
		this.append((BoxObject) null, 0.1, 0);
		
		x = new XStrip();
		x.append(new NBButton(window, "Reset P"));
		x.append(new NBButton(window, "Reset N"));
		this.append(x, 0.75, 0);
		
		contentBox = new Box();
		this.append(contentBox, 9);
		
		setTeamsAmount(2);
	}
	
	public void setTeamsAmount(int i) {
		
		YStrip root = new YStrip();
		
		root.append((BoxObject) null, (6-i)/4.);
		
		for (int j = 0; j < i; j++){
			if (Teams[j] == null)
				Teams[j] = new NamedTeamMenu(window, TEAMS[j]);
			Teams[j].setVisible(true);
			root.append(Teams[j], 1, 0);
		}
		
		root.append((BoxObject) null, (6-i)/4.);
		
		contentBox.setBoxObject(root);
		
	}
	
	/*public void SetUp(int teams) {
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
	}*/
	
	public static class MySwitch extends NBButton implements ActionListener {
		private static final long serialVersionUID = 1L;

		public boolean Active = false;
		
		public Color activeColor = Color.CYAN;
		public Color deactiveColor = Color.LIGHT_GRAY;
		
		public MySwitch(Window window, String text, String code) {
			super(window, text, code);
			this.addActionListener(this);
		}
		
		public MySwitch(Window window, String text) {
			super(window, text, text);
			this.addActionListener(this);
		}

		public void actionPerformed(ActionEvent e) {
			Active = !Active;
			if (Active) this.setBackground(activeColor);
			else this.setBackground(deactiveColor);
		}
		
	}
	
}
