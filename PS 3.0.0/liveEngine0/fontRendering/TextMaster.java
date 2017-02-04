package fontRendering;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fontMeshCreator.FontType;
import fontMeshCreator.GUIText;
import textures.TextureUtils;

public class TextMaster {
	
	private static String FontResLocation = "res/font/";

	private static Map<String, FontType> fonts = new HashMap<String, FontType>();
	
	public static Map<FontType, List<GUIText>> texts = new HashMap<FontType, List<GUIText>>();
	
	private static FontRenderer renderer;
	
	public static void init(){
		
		// TODO: make the TextMaster independent on the screen;
		
		renderer = new FontRenderer();
	}
	
	public static void render(){
		renderer.render(texts);
	}

	public static void addText(GUIText text) {
		FontType font = text.getFont();
		
		List<GUIText> textBatch = TextMaster.texts.get(font);
		if (textBatch == null) 
			TextMaster.texts.put(font, textBatch = new ArrayList<GUIText>());
		
		textBatch.add(text);		
	}
	
	public static void removeText(GUIText text){
		List<GUIText> textBatch = texts.get(text.getFont());
		textBatch.remove(text);
		if (textBatch.isEmpty()) 
			texts.remove(textBatch);
	}
	
	public static void cleanUp(){
		renderer.cleanUp();
	}

	public static FontType getFontType(String fileName) {
		if (fonts.containsKey(fileName))
			return fonts.get(fileName);
		return newFontType(fileName, fileName, fileName);
	}
	
	public static FontType getFontType(String key, String fileName) {
		return newFontType(key, fileName, fileName);
	}

	private static FontType newFontType(String key, String atlasFileName, String disctipptionFileName) {
		FontType font = new FontType(loadFontAtlas(atlasFileName), disctipptionFileName);
		fonts.put(key, font);
		return font;
	}

	public static int loadFontAtlas(String fileName) {
		return TextureUtils.loadTexture(FontResLocation + fileName+ ".png").ID;
	}
	
}
