package no.nokorot.pointsystem.Windows;

import static no.nokorot.pointsystem.Element.Team.TEAMS;

import no.nokorot.pointsystem.Element.NamedTeamMenu;
import util.Window;
import util.handelers.ImageHandeler.ScaleType;
import util.swing.NBButton;
import util.swing.Label;
import util.swing.TextField;
import util.swing.gride.Box;
import util.swing.gride.BoxObject;
import util.swing.gride.Strip;
import util.swing.gride.XStrip;
import util.swing.gride.YStrip;

public class LocalTeamPanel {

	private static int Height = MainMenu.Height;
	private static int AdvanceHeight = 150;
	private static int HeightFix = 0;

	public BoxObject toolBox;
	public Box teamsBox = new Box();

	private Window window;
	private NamedTeamMenu[] Teams;

	private TextField teamAmount;

	public LocalTeamPanel(Window window, NamedTeamMenu[] teamMenues) {
		this.window = window;
		this.Teams = teamMenues;
		initToolBox();
	}

	public void setLocal() {
		MainMenu.TeamBox.setBoxObject(teamsBox);
		MainMenu.TeamToolBox.setBoxObject(toolBox);
		MainMenu.TeamToolBox.setInsets(3, 5, 3, 5);

		teamAmount.setText("" + MainMenu.currentAmountOfTeams);
	}

	private void initToolBox() {
		YStrip y;
		XStrip x = new XStrip();
		NBButton online = new NBButton(window, "", "online");
		online.setIcon(MainMenu.icons[1], ScaleType.TILLPASS);
		x.append(online, .5);

		NBButton setD = new NBButton(window, "", "setD");
		setD.setIcon(MainMenu.icons[0], ScaleType.TILLPASS);
		x.append(setD, .5);

		y = new YStrip();
		y.append(new Label(window, "Reset"));
		y.append(new NBButton(window, "Points", "resp"), 1.8);
		x.append(y);
		y = new YStrip();
		y.append(new Label(window, "Reset"));
		y.append(new NBButton(window, "Names", "resn"), 1.8);
		x.append(y);
		y = new YStrip();
		y.append(new Label(window, "Given Points"));
		y.append(MainMenu.GivenPoints = new TextField(window, "1", "points"), 1.8);
		x.append(y);
		y = new YStrip();
		y.append(new Label(window, "Team Amount"));
		y.append(teamAmount = new TextField(window, "2", "teams"), 1.8);
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

	private int add(Strip root, int i, int l) {
		XStrip x = new XStrip();

		for (int k = 0; k < l; k++) {
			x.append((BoxObject) null, 1);
			if (Teams[i + k] == null)
				Teams[i + k] = new NamedTeamMenu(window, TEAMS[i + k]);
			Teams[i + k].setVisible(true);
			x.append(Teams[i + k], l * l * 2);
		}
		x.append((BoxObject) null, 1);
		root.append(x);

		return i + l;
	}

}
