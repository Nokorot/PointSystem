package guis;

import shaders.ShaderProgram;
import shaders.UniformMatrix;

public class GuiShader extends ShaderProgram {
	
	private static final String VERTEX_FILE = "src/liveEngine/guis/guiVertexShader.txt";
	private static final String FRAGMENT_FILE = "src/liveEngine/guis/guiFragmentShader.txt";
	
	protected UniformMatrix projectionViewMatrix = new UniformMatrix("transformationMatrix");

	public GuiShader() {
		super(VERTEX_FILE, FRAGMENT_FILE, "position");
		super.storeAllUniformLocations(projectionViewMatrix);
	}
	
}
