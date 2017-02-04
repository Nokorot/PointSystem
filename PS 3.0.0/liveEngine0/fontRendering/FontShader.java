package fontRendering;

import engine.Display;
import shaders.ShaderProgram;
import shaders.UniformFloat;
import shaders.UniformVec2;
import shaders.UniformVec4;
import utils.Vector2f;

public class FontShader extends ShaderProgram {

	protected UniformFloat asp = new UniformFloat("aspr");
	
	protected UniformVec4 color = new UniformVec4("color");
	protected UniformFloat width = new UniformFloat("width");
	protected UniformFloat dpow = new UniformFloat("dpow");
	
	protected UniformVec4 borderColor = new UniformVec4("borderColor");
	protected UniformFloat borderWidth = new UniformFloat("borderWidth");
	protected UniformFloat bdpow = new UniformFloat("bdpow");
	protected UniformVec2 offset = new UniformVec2("offset");

	protected UniformVec2 translation = new UniformVec2("translation");

	private static final String VERTEX_FILE = "liveEngine0/fontRendering/fontVertex.txt";
	private static final String FRAGMENT_FILE = "liveEngine0/fontRendering/fontFragment.txt";

	public FontShader() {
		super(VERTEX_FILE, FRAGMENT_FILE, "position", "textureCoords");
		super.storeAllUniformLocations(color, borderColor, offset, width, borderWidth, translation, asp);
	}

	protected void loadTranslation(Vector2f translation) {
		this.translation.loadVec2(translation);
	}

	public void loadMaterial(FontMaterial m) {
		asp.loadFloat(Display.aspect);
		if (m.color != null)
			color.loadVec4(m.color);
		if (m.borderColor != null)
			borderColor.loadVec4(m.borderColor);
		if (m.borderOffset != null)
			offset.loadVec2(m.borderOffset);
		width.loadFloat(m.width);
		borderWidth.loadFloat(m.borderWidth);
		dpow.loadFloat(m.decayRate);
		bdpow.loadFloat(m.borderDecayRate);
	}

}
