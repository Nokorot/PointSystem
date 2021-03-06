package engineTest;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import engine.Display;
import fontRendering.TextMaster;
import guis.GuiRenderer;
import guis.GuiTexture;
import utils.Vector2f;

public class Seane {
	
	protected Display display;
	
	private LiveImage background;

	public static List<GuiTexture> guis = new ArrayList<GuiTexture>();
	
	public void setBackground(String imagePath) {
		if (background != null)
			guis.remove(background);
		background = new LiveImage(imagePath);
		background.posiotion = new Vector2f();
		background.scaleType = GuiTexture.S_FILL;
		guis.add(background);
	}
	
	public void setBackground(BufferedImage image) {
		background = new LiveImage(image);
		background.posiotion = new Vector2f();
		background.scaleType = GuiTexture.S_FILL;
		guis.add(background);
		if (background != null)
			guis.remove(background);
	}
	
	public void render(){
		GuiRenderer.render(guis);
		TextMaster.render();
	}

	public void update() {
		// TODO Auto-generated method stub
	}

	public void setDisplay(Display display) {
		this.display = display;
	}
	
}
