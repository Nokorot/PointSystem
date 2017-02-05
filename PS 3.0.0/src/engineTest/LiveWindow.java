package engineTest;
import java.awt.Color;

import engine.Display;
import fontRendering.TextMaster;
import guis.GuiRenderer;

public class LiveWindow extends Display {
	
	public Seane seane;
	
	public static float getAspectration() {
		return aspect;
	}

	public LiveWindow() {
		super(500, 500);
		visible(true);
	}

	protected void init() {
		GuiRenderer.init();
		TextMaster.init();
	}
	
	public void setBackground(Color color) {
		super.setBackground(color);
	}
	
	@Override
	protected void windowHints() {
		super.windowHints();
		// glfwWindowHint(GLFW_DECORATED, GLFW_FALSE); // removes the window border
	}

	@Override
	protected void update() {
		if (seane == null) return;
		seane.update();
	}

	@Override
	protected void render() {
		if (seane == null) return;
		seane.render();
	}
}
