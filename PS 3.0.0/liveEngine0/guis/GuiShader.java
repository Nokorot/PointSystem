package guis;

import shaders.ShaderProgram;
import shaders.UniformMatrix;

public class GuiShader extends ShaderProgram {
	
	private static final String VERTEX_FILE = "liveEngine0/guis/guiVertexShader.txt";
	private static final String FRAGMENT_FILE = "liveEngine0/guis/guiFragmentShader.txt";

	protected UniformMatrix projectionViewMatrix = new UniformMatrix("transformationMatrix");
	
	protected UniformMatrix scaleMatrix = new UniformMatrix("scaleMatrix");

	public GuiShader() {
		super(VERTEX_FILE, FRAGMENT_FILE, "position");
		super.storeAllUniformLocations(projectionViewMatrix, scaleMatrix);
	}
	
}
