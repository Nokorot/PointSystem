package fontMeshCreator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

//import org.lwjgl.opengl.Display;
//import org.lwjgl.util.vector.Vector2f;
//
//import toolbox.Vector2d;

/**
 * Provides functionality for getting the values from a font file.
 * 
 * @author Karl
 *
 */
public class MetaFile {

	public static final int NORMAL = 0;
	public static final int BOLD 	= 1;
	public static final int ITALIC = 2;
	
	public static final int PAD_TOP = 0;
	public static final int PAD_LEFT = 1;
	public static final int PAD_BOTTOM = 2;
	public static final int PAD_RIGHT = 3;

	public static final int DESIRED_PADDING = 8;

	private static final String SPLITTERS = " =\n";
	private static final String STRSING_MARKER = "\"'";
	private static final String NUMBER_SEPARATOR = ",";

	private double[] perPixelSize = new double[2];
	private int[] imageSize = new int[2];
	private int[] padding;
	private int pixelLineHeight;
	
	private String face;
	private int type = 0;
	private int count;
	
	private double spaceWidth;

	private Map<Integer, Character> metaData = new HashMap<Integer, Character>();

	private BufferedReader reader;
	private String line, prefix;
	private int index;
	private Map<String, String> values = new HashMap<String, String>();

	/**
	 * Opens a font file in preparation for reading.
	 * 
	 * @param file
	 *            - the font file.
	 */
	protected MetaFile(File file) {
		openFile(file);

		while (processNextLine()) 
			if (prefix.equals("info"))
				loadInfo();
			else if (prefix.equals("common"))
				loadCommon();
			else if (prefix.equals("chars"))
				loadChars();
			else if (prefix.equals("char"))
				loadChar();
		
		close();
	}
	
	protected double getSpaceWidth() {
		return spaceWidth;
	}

	protected Character getCharacter(int ascii) {
		return metaData.get(ascii);
	}
	
	protected int getHeightPadding(){
		return padding[PAD_TOP] + padding[PAD_BOTTOM];
	}
	
	protected int getWidthPadding(){
		return padding[PAD_LEFT] + padding[PAD_RIGHT];
	}

	private void loadInfo(){
		this.face = getStringValue("face");
		this.padding = getIntValues("padding");
		this.type += BOLD 	* getIntValue("bold");
		this.type += ITALIC * getIntValue("italic");
	}
	
	private void loadCommon(){
		imageSize[0] = getIntValue("scaleW");
		imageSize[1] = getIntValue("scaleH");	
		
		pixelLineHeight = getIntValue("lineHeight") - getHeightPadding();
		
		double aspectRatio = 1; //TODO: LiveWindow.getAspectRatio();
		perPixelSize[0] = TextMeshCreator.LINE_HEIGHT / (double) pixelLineHeight;
		perPixelSize[1] = perPixelSize[0] / aspectRatio;
	}
	
	private void loadChars(){
		this.count = getIntValue("count");
	}
	
	private void loadChar(){
		int id = getIntValue("id");
		if (id == TextMeshCreator.SPACE_ASCII){
			this.spaceWidth = (getIntValue("xadvance") - getWidthPadding()) * perPixelSize[0];
			return;
		}
		
		double xTex = (getDoubleValue("x")/* + (padding[PAD_LEFT] - DESIRED_PADDING)*/) / imageSize[0];
		double yTex = (getDoubleValue("y")/* + (padding[PAD_TOP] - DESIRED_PADDING)*/) / imageSize[1];
		int width 	= getIntValue("width")/* - getWidthPadding() + (2 * DESIRED_PADDING)*/;
		int height 	= getIntValue("height")/* - getHeightPadding() + (2 * DESIRED_PADDING)*/;
		double quadWidth = width * perPixelSize[0];
		double quadHeight = height * perPixelSize[1];
		double xTexSize = (double) width / imageSize[0];
		double yTexSize = (double) height / imageSize[1];
		double xOff = (getIntValue("xoffset")/* + (padding[PAD_LEFT] - DESIRED_PADDING)*/) * perPixelSize[0];
		double yOff = (getIntValue("yoffset")/* + (padding[PAD_TOP] - DESIRED_PADDING)*/) * perPixelSize[1];
		double xAdvance = (getIntValue("xadvance") - getWidthPadding()) * perPixelSize[0];
		
		metaData.put(id, new Character(id, xTex, yTex, xTexSize, yTexSize, xOff, yOff, quadWidth, quadHeight, xAdvance));
	}
	
	/**
	 * Read in the next line and store the variable values.
	 * 
	 * @return {@code true} if the end of the file hasn't been reached.
	 */
	private boolean processNextLine() {
		values.clear();
		line = null;
		index = 0;
		prefix = null;

		try {
			line = reader.readLine();
		} catch (IOException e1) {
		}
		if (line == null)
			return false;
		
		prefix = nextWord();
		
		String key;
		while (index < line.length()) {
			key = nextWord();
			if(curent() == '=')
				values.put(key, nextWord());
			else 
				values.put(key, "");
		}

		return true;
	}
	
	private String nextWord(){
		while(" =".contains(curent() + ""))
			index++;
		
		if (STRSING_MARKER.contains(curent() + ""))
			return getString();
		
		int start = index;
		while(!SPLITTERS.contains(next() + ""))
			continue;
		return line.substring(start, index);
	}
	
	private String getString(){
		int start = ++index;
		while(!"\"\n".contains(curent() + ""))
			index++;
		return line.substring(start, index++);
	}

	private char curent() {
		if (index + 1 >= line.length()) 
			return '\n';
		return line.charAt(index);
	}
	
	private char next() {
		if (++index >= line.length()) 
			return '\n';
		return line.charAt(index);
	}

	/**
	 * Gets the {@code int} value of the variable with a certain name on the
	 * current line.
	 * 
	 * @param variable
	 *            - the name of the variable.
	 * @return The value of the variable.
	 */
	private int getIntValue(String variable) {
		return Integer.parseInt(values.get(variable));
	}

	/**
	 * Gets the {@code double} value of the variable with a certain name on the
	 * current line.
	 * 
	 * @param variable
	 *            - the name of the variable.
	 * @return The value of the variable.
	 */
	private double getDoubleValue(String variable) {
		return Double.parseDouble(values.get(variable));
	}

	/**
	 * Gets the {@code String} value of the variable with a certain name on the
	 * current line.
	 * 
	 * @param variable
	 *            - the name of the variable.
	 * @return The value of the variable.
	 */
	private String getStringValue(String variable) {
		return values.get(variable);
	}

	/**
	 * Gets the array of ints associated with a variable on the current line.
	 * 
	 * @param variable
	 *            - the name of the variable.
	 * @return The int array of values associated with the variable.
	 */
	private int[] getIntValues(String variable) {
		String[] numbers = values.get(variable).split(NUMBER_SEPARATOR);
		int[] actualValues = new int[numbers.length];
		for (int i = 0; i < actualValues.length; i++) {
			actualValues[i] = Integer.parseInt(numbers[i]);
		}
		return actualValues;
	}
	
	/**
	 * Closes the font file after finishing reading.
	 */
	private void close() {
		try {
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Opens the font file, ready for reading.
	 * 
	 * @param file
	 *            - the font file.
	 */
	private void openFile(File file) {
		try {
			reader = new BufferedReader(new FileReader(file));
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Couldn't read font meta file!");
		}
	}


}
