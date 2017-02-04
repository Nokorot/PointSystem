package engineTest;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import engine.Display;
import fontRendering.TextMaster;
import guis.GuiRenderer;
import guis.GuiTexture;
import util.swing.gride.XStrip;
import util.swing.gride.YStrip;
import utils.Vector2f;

public class LiveWindow extends Display {
	
	private LiveImage background;

	public static List<GuiTexture> guis = new ArrayList<GuiTexture>();

//	public static List<LiveContent> content = new ArrayList<LiveContent>();

	private PSTeam[] teams = new PSTeam[9];
	
	public static float getAspectration() {
		return 1;
	}

	public LiveWindow() {
		super(500, 500);
		visible(true);
	}

	protected void init() {
		GuiRenderer.init();
		TextMaster.init();

		setBackground("res/bg.jpg");
		
		orgenizeTeams(2);
	}
	
	public void setBackground(Color color) {
		super.setBackground(color);
		if (background != null)
			background.visible = false;
	}
	
	public void setBackground(String imagePath) {
		background = new LiveImage("res/bg.jpg");
		background.posiotion = new Vector2f();
		background.scaleType = GuiTexture.S_FILL;
	}
	
	public void setBackground(BufferedImage image) {
		background = new LiveImage(image);
		background.posiotion = new Vector2f();
		background.scaleType = GuiTexture.S_FILL;
	}
	
	
	
	private PSTeam getTeam(int index){
		if (teams[index] == null)
			teams[index] = new PSTeam(this);
		teams[index].setVisible(true);
		return teams[index];
	}
	
	public PSTeam orgenizeTeams(int amount) {
		YStrip root = new YStrip();
		
		if (amount > 9 || amount < 0)
			return null;
		
		// width is 3
		int i = 0;
		while (amount > 2 && amount != 4){
			XStrip x = new XStrip();
			x.append(getTeam(i++));
			x.append(getTeam(i++));
			x.append(getTeam(i++));
			root.append(x);
			amount -=3;
		}
		while (amount > 0 && amount %2 == 0){
			XStrip x = new XStrip();
			x.append(getTeam(i++));
			x.append(getTeam(i++));
			root.append(x);
			amount -=2;
		}
		if (amount > 0)
			root.append(getTeam(i++));
		
		root.setBounds(new Rectangle(-1000, -1000, 2000, 2000));
		
		for (; i<9; i++){
			if (teams[i] == null)
				break;
			teams[i].setVisible(false);
		}
		
		return null;
	}
	
	@Override
	protected void windowHints() {
		super.windowHints();
		// glfwWindowHint(GLFW_DECORATED, GLFW_FALSE); // removes the window border
	}

	@Override
	protected void update() {
		// TODO Auto-generated method stub
	}

	@Override
	protected void render() {
		GuiRenderer.render(guis);
		TextMaster.render();
	}
}
