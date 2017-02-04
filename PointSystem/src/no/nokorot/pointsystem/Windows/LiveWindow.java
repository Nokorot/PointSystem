package no.nokorot.pointsystem.Windows;

import static no.nokorot.pointsystem.Element.Team.MAX_TEAMS;
import static no.nokorot.pointsystem.Element.Team.TEAMS;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JWindow;

import no.nokorot.pointsystem.PSData;
import no.nokorot.pointsystem.Element.NamedTeam;
import util.Window;
import util.handelers.ImageHandeler;
import util.handelers.ImageHandeler.ScaleType;
import util.math.Maths;
import util.swing.Label;
import util.swing.gride.Box;
import util.swing.gride.BoxGrid;

public class LiveWindow {

	// public static final int X_GRID_SIZE = 5;
	// public static final int Y_GRID_SIZE = 5;

	public static final int LogoMode = 1;
	public static final int BlackMode = 2;

	private static JWindow window;
	private static Label label;
	private static BufferedImage image;

	private static int Mode = 0;
	private static boolean cleared = false;

	private static List<Element> elements = new ArrayList<Element>();

	private static BufferedImage Logo;
	private static ScaleType LogoScaling = ScaleType.TILLPASS;

	private static BufferedImage BackgroundImage;
	private static ScaleType BacgroundScaling = ScaleType.FYLL;
	private static Color backgroundColor = Color.BLACK;

	public static void create(Window parent, BufferedImage logo) {
		window = new JWindow();
		label = new Label(parent);
		window.getContentPane().add(label, BorderLayout.CENTER);
		setBounds(1, 1, 100, 100);
		Logo = logo;

		label.getGraphics();

		for (int i = 0; i < TEAMS.length; i++)
			Teams[i] = new NamedTeam(label, TEAMS[i], new Rectangle());
		add(Teams);
	}

	public static void setBounds(Rectangle rec) {
		setBounds(rec.x, rec.y, rec.width, rec.height);
	}

	public static void setBounds(int x, int y, int width, int height) {
		window.setBounds(x, y, width, height);
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		render();
	}

	public static void setVisible(boolean b) {
		window.setVisible(b);
	}

	public static void setMode(int mode) {
		if (Mode == mode) {
			Mode = 0;
			setCleared(cleared);
		} else {
			Mode = mode;
			for (Element element : elements)
				element.setVisible(false);
		}
		render();
	}

	public static void setCleared(boolean clear) {
		cleared = clear;

		if(Mode != 0)
			return;
		for (Element element : elements)
			if(element.Active)
				element.setVisible(!clear);
	}
	
	public static boolean isCleared() {
		return cleared;
	}

	public static void setBackground(BufferedImage background, ScaleType scaleType) {
		if (BackgroundImage == background && BacgroundScaling == scaleType)
			return;
		BackgroundImage = background;
		BacgroundScaling = scaleType;
		render();
	}

	public static void setBackgroundColor(Color c) {
		backgroundColor = c;
	}

	public static void setLogo(BufferedImage logo, ScaleType scaleType) {
		if (Logo == logo && LogoScaling == scaleType)
			return;
		Logo = logo;
		LogoScaling = scaleType;
		render();
	}

	public static int getWidth() {
		return window.getWidth();
	}

	public static int getHeight() {
		return window.getHeight();
	}

	public static Label getLabel() {
		return label;
	}

	public static void update() {
		if (!PSData.getLiveWindowBounds().equals(window.getBounds()))
			setBounds(PSData.getLiveWindowBounds());

		if (!cleared)
			for (Element element : elements) {
				element.update();
			}
	}

	public static void render() {
		Graphics g = image.getGraphics();

		if (Mode == BlackMode)
			g.setColor(Color.BLACK);
		else
			g.setColor(backgroundColor);
		
		g.fillRect(0, 0, getWidth(), getHeight());

		if (Mode == LogoMode) {
			renderScaledImage(g, Logo, LogoScaling);

		} else if (Mode != BlackMode) {
			renderScaledImage(g, BackgroundImage, BacgroundScaling);
			if (!cleared) {
				for (Element element : elements) {
					element.render(g);
				}
			}
		}

		g.dispose();

		label.setIcon(new ImageIcon(image));
	}

	private static void renderScaledImage(Graphics g, BufferedImage image, ScaleType scaleType) {
		if (image == null)
			return;
		BufferedImage scaledLogo = null;
		try {
			scaledLogo = ImageHandeler.getScaledImage(image, getWidth(), getHeight(), scaleType);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
			return;
		}
		g.drawImage(scaledLogo, getWidth() / 2 - scaledLogo.getWidth() / 2,//
				getHeight() / 2 - scaledLogo.getHeight() / 2, scaledLogo.getWidth(), //
				scaledLogo.getHeight(), null);
	}

	public static abstract class Element {
		public abstract void render(Graphics g);
		
		private boolean Active = true;
		
		public void setActive(boolean active) {
			Active = active;
			setVisible(active && !cleared);
		}

		protected abstract void setVisible(boolean visible);

		public void update() {
		}
	}

	public static void add(Element... es) {
		for (Element element : es)
			elements.add(element);
		render();
	}

	public static void remove(Element... es) {
		for (Element element : es) {
			element.setVisible(false);
			elements.remove(element);
		}
		render();
	}

	// *********** Set Up **************

	private static NamedTeam[] Teams = new NamedTeam[MAX_TEAMS];

	private static BoxGrid grid;

	public static void SetUp(int teams) {
		for (int i = 0; i < Teams.length; i++) {
			if (Teams[i] == null)
				break;
			Teams[i].setActive(false);
		}

		teams = Maths.clamp(teams, 1, Teams.length);

		int team = 0;

		grid = new BoxGrid(new Rectangle(0, 100, 1000, 800), 1, (teams + 2) / 3);
		if (teams == 1) {
			addI(grid.getBox(0), team++);
		} else {
			if (teams % 3 == 1) {
				team = addIII(grid, team, (teams / 3) - 1);
				team = addII(grid.getBox(team / 3), team, team / 3);
				team = addII(grid.getBox(team / 3 + 1), team, (team + 1) / 3);
			} else {
				team = addIII(grid, team, teams / 3);
				if (teams % 3 == 2) {
					team = addII(grid.getBox(teams / 3), team, team / 3);
				}
			}
		}
		update();
	}

	private static void addTeam(Box box, int team) {
		if (team >= Teams.length)
			return;

		Rectangle r = box.getRectangle();

		Teams[team].setRealatieBounds(r.x / 1000f, r.y / 1000f, r.width / 1000f, r.height / 1000f);
		Teams[team].setActive(true);
	}

	private static void addI(Box box, int team) {
		BoxGrid g = box.getInsideGrid(new double[] { 1, 2, 1 }, 1);
		addTeam(g.getBox(1), team++);
	}

	private static int addII(Box box, int team, int h) {
		BoxGrid g = box.getInsideGrid(new double[] { 1, 4, 1, 4, 1 }, 1);
		addTeam(g.getBox(1), team++);
		addTeam(g.getBox(3), team++);
		return team;
	}

	private static int addIII(BoxGrid grid, int team, int a) {
		for (int i = 0; i < a; i++) {
			BoxGrid g = grid.getBox(i).getInsideGrid(new double[]{1, 6, 1, 6, 1, 6, 1}, 1);
			addTeam(g.getBox(1), team++);
			addTeam(g.getBox(3), team++);
			addTeam(g.getBox(5), team++);
		}
		return team;
	}

	

}
