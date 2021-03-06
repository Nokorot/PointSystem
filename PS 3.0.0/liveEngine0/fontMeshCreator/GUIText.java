package fontMeshCreator;

import engine.Display;
import fontRendering.FontMaterial;
import openglObjects.Vao;
import utils.Vector2f;

/**
 * Represents a piece of text in the game.
 * 
 * @author Karl
 *
 */
public class GUIText {

	private Display display;

	private String textString;
	private float fontSize;
	private boolean visible = true;

	private int textMeshVao;
	private int vertexCount;

	private Vector2f position = new Vector2f();
	private float lineMaxSize;
	private int numberOfLines;

	private FontMaterial fontMaterial;

	private boolean centerText = false;

	/**
	 * Creates a new text, loads the text's quads into a VAO, and adds the text
	 * to the screen.
	 * 
	 * @param text
	 *            - the text.
	 * @param fontSize
	 *            - the font size of the text, where a font size of 1 is the
	 *            default size.
	 * @param material
	 *            - the font that this text should use.
	 * @param position
	 *            - the position on the screen where the top left corner of the
	 *            text should be rendered. The top left corner of the screen is
	 *            (0, 0) and the bottom right is (1, 1).
	 * @param maxLineLength
	 *            - basically the width of the virtual page in terms of screen
	 *            width (1 is full screen width, 0.5 is half the width of the
	 *            screen, etc.) Text cannot go off the edge of the page, so if
	 *            the text is longer than this length it will go onto the next
	 *            line. When text is centered it is centered into the middle of
	 *            the line, based on this line length value.
	 * @param centered
	 *            - whether the text should be centered or not.
	 */
	public GUIText(Display display, String text, float fontSize, FontMaterial material, Vector2f position,
			float maxLineLength, boolean centered) {
		this.display = display;
		this.textString = text;
		this.fontSize = fontSize;
		this.fontMaterial = material;
		this.position = position;
		this.lineMaxSize = maxLineLength;
		this.centerText = centered;
		this.updateMesh();
	}
	
	public GUIText(Display display){
		this.display = display;
	}
	
	public GUIText(){
	}

	private void updateMesh() {
		if (this.display == null) return;
		this.display.addOperation(this, "MeshUpdate", () -> {
			TextMesh data = fontMaterial.font.createTextMesh(this);

			Vao vao = Vao.create();
			vao.storeData(new int[] { 2, 2 }, data.getVertexPositions(), data.getTextureCoords());

			this.textMeshVao = vao.id;
			this.vertexCount = data.getVertexCount();
		});
	}
	
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	public void setText(String text) {
		if (text == null)
			return;
		if (text.length() <= 0)
			return;

		this.textString = text;
		this.updateMesh();
	}
	
	public void setFontMaterial(FontMaterial fontMaterial) {
		this.fontMaterial = fontMaterial;
		this.updateMesh();
	}

	public void setDisplay(Display display) {
		this.display = display;
		updateMesh();
	}

	public void setFontSize(float fontSize) {
		this.fontSize = fontSize;
		updateMesh();
	}

	public void setPosition(Vector2f position) {
		this.position = position;
	}

	public void setLineMaxSize(float lineMaxSize) {
		this.lineMaxSize = lineMaxSize;
		updateMesh();
	}

	public void setCenterText(boolean centerText) {
		this.centerText = centerText;
		updateMesh();
	}

	public boolean isVisible() {
		return visible;
	}
	
	/**
	 * @return The font used by this text.
	 */
	public FontType getFont() {
		return fontMaterial.font;
	}

	/**
	 * @return The font mateial used by this text.
	 */
	public FontMaterial getFontMaterial() {
		return fontMaterial;
	}

	/**
	 * @return The number of lines of text. This is determined when the text is
	 *         loaded, based on the length of the text and the max line length
	 *         that is set.
	 */
	public int getNumberOfLines() {
		return numberOfLines;
	}

	/**
	 * @return The position of the top-left corner of the text in screen-space.
	 *         (0, 0) is the top left corner of the screen, (1, 1) is the bottom
	 *         right.
	 */
	public Vector2f getPosition() {
		return position;
	}

	/**
	 * @return the ID of the text's VAO, which contains all the vertex data for
	 *         the quads on which the text will be rendered.
	 */
	public int getMesh() {
		return textMeshVao;
	}

	/**
	 * @return The total number of vertices of all the text's quads.
	 */
	public int getVertexCount() {
		return this.vertexCount;
	}

	/**
	 * @return the font size of the text (a font size of 1 is normal).
	 */
	protected float getFontSize() {
		return fontSize;
	}

	/**
	 * Sets the number of lines that this text covers (method used only in
	 * loading).
	 * 
	 * @param number
	 */
	protected void setNumberOfLines(int number) {
		this.numberOfLines = number;
	}

	/**
	 * @return {@code true} if the text should be centered.
	 */
	protected boolean isCentered() {
		return centerText;
	}

	/**
	 * @return The maximum length of a line of this text.
	 */
	protected float getMaxLineSize() {
		return lineMaxSize;
	}

	/**
	 * @return The string of text.
	 */
	protected String getTextString() {
		return textString;
	}

}
