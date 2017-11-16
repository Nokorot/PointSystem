package no.nokorot.pointsystem.Element;

import java.awt.Rectangle;

import javax.swing.JPanel;

import com.sun.org.apache.xalan.internal.xsltc.compiler.sym;

import no.nokorot.pointsystem.Windows.LiveWindow;
import no.nokorot.pointsystem.Windows.MainMenu;
import util.Window;
import util.swing.NBButton;
import util.swing.NBTextField;
import util.swing.gride.BoxObject;
import util.swing.gride.XStrip;
import util.swing.gride.YStrip;

public class NamedTeamMenu implements BoxObject {

	// private static float nameHeight = 0.15f;

	private Team team;

	private BoxObject root;
	private YStrip root0, root1;
	
	private Window menu;
	private NBTextField nameLabel, pointsLabel;
	private NBButton add, sub;
	
	public NamedTeamMenu(Window menu, Team team) {
		System.out.println(team);
		this.team = team;
		team.menu = this;
		this.menu = menu;

		Objects();
	}

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
		team.setPoints(i);
		pointsLabel.setText(team.stringedP());
	}

	public int getPoints() {
		return team.points;
	}

	public void setName(String name) {
		team.setName(nameLabel.getText());
		nameLabel.setText(name);
	}

	@SuppressWarnings("serial")
	private void Objects() {
		
		nameLabel = new NBTextField(menu, team.name) {
			protected void onAction() {
				team.setName(nameLabel.getText());
			}
		};
		
		sub = new NBButton(menu, "-") {
			public void onAction() {
				team.addPoints(-Integer.parseInt(MainMenu.GivenPoints.getText()));
				pointsLabel.setText(team.stringedP());
			}
		};
		
		add = new NBButton(menu, "+") {
			public void onAction() {
				team.addPoints(Integer.parseInt(MainMenu.GivenPoints.getText()));
				pointsLabel.setText(team.stringedP());
			}
		};
		
		pointsLabel = new NBTextField(menu, team.stringedP()) {
			protected void onAction() {
				team.setPoints(pointsLabel.getText());
				pointsLabel.setText(team.stringedP());
			}
		};
		
		root0 = new YStrip();
		root0.append(nameLabel);//.setInsets(3);
		XStrip x = new XStrip();
		x.append(sub);
		x.append(pointsLabel);
		x.append(add);
		root0.append(x);
		
		root1 = new YStrip();
		root1.append(nameLabel);//.setInsets(3);
		root1.append(pointsLabel);
		
		root = root0;
	}

	public void setEditable(boolean editable) {
		if (editable){
			root = root0;
			pointsLabel.setEditable(true);
			add.setVisible(true);
			sub.setVisible(true);
		} else{
			root = root1;
			pointsLabel.setEditable(false);
			add.setVisible(false);
			sub.setVisible(false);
		}
	}

	public void setPane(JPanel pane) {
		root.setPane(pane);
	}
}
