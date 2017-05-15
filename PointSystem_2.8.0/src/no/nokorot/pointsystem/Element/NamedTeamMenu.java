package no.nokorot.pointsystem.Element;

import java.awt.Rectangle;

import no.nokorot.pointsystem.Windows.MainMenu;
import util.Window;
import util.swing.Button;
import util.swing.TextField;
import util.swing.gride.Box;
import util.swing.gride.BoxGrid;
import util.swing.gride.BoxObject;
import util.swing.gride.XStrip;
import util.swing.gride.YStrip;

public class NamedTeamMenu implements BoxObject {

	// private static float nameHeight = 0.15f;

	private Team team;

	private YStrip root;
	private Window menu;
	private TextField nameLabel, pointsLabel;

	public NamedTeamMenu(Window menu, Team team) {
		this.team = team;
		this.menu = menu;

		Objects();
	}

	/*public NamedTeamMenu(Window menu, Box box, Team team) {
		this.team = team;
		this.menu = menu;

		bG = box.getInsideGrid(1, 2);
		Objects();
	}

	public void setBox(Box box) {
		box.setBoxObject(bG);
	}*/

	public void setBounds(Rectangle bounds) {
		root.setBounds(bounds);
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	public void setVisible(boolean visible) {
		root.setVisible(visible);
	}

	public void setPoints(int i) {
		team.points = i;
		pointsLabel.setText(team.stringedP());
		MainMenu.updateLiveWindow();
	}

	public void setName(String name) {
		team.name = name;
		nameLabel.setText(name);
		MainMenu.updateLiveWindow();
	}

	@SuppressWarnings("serial")
	private void Objects() {
		
		root = new YStrip();
		
		nameLabel = new TextField(menu, team.name) {
			protected void onAction() {
				team.name = nameLabel.getText();
				MainMenu.updateLiveWindow();
			}
		};
		
		root.append(nameLabel);//.setInsets(3);
		
		
		XStrip x = new XStrip();
		
		x.append(new Button(menu, "-") {
			public void onAction() {
				team.points -= Integer.parseInt(MainMenu.GivenPoints.getText());
				pointsLabel.setText(team.stringedP());
				MainMenu.updateLiveWindow();
			}
		});
		
		x.append(pointsLabel = new TextField(menu, team.stringedP()) {
			protected void onAction() {
				team.setP(pointsLabel.getText());
				pointsLabel.setText(team.stringedP());
				MainMenu.updateLiveWindow();
			}
		});
		
		x.append(new Button(menu, "+") {
			public void onAction() {
				team.points += Integer.parseInt(MainMenu.GivenPoints.getText());
				pointsLabel.setText(team.stringedP());
				MainMenu.updateLiveWindow();
			}
		});
		
		root.append(x);
		
	}
}
