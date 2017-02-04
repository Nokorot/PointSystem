package fontRendering;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import fontMeshCreator.FontType;
import utils.Vector2f;
import utils.Vector4f;

public class FontMaterial {
	
	public class Glow extends FontMaterial {
		public Glow(){
			borderWidth = 0.4f;
		}
	}

	public FontType font;
	public Vector4f color; 
	public Vector4f borderColor;
	public float width = 0.5f;
	public float borderWidth = 0.0f;
	public float decayRate = 5;
	public float borderDecayRate = 5;
	public Vector2f borderOffset;

	public FontMaterial(){
	}
	
	public FontMaterial(FontType font){
		this.font = font;
	}
	
	@SuppressWarnings("resource")
	public static FontMaterial loadFile(String filename) throws FileNotFoundException {
		Map<String, String> data = new HashMap<>();
		
		try {
			Scanner scan = new Scanner(new File(filename));
			
			while (scan.hasNextLine()){
				String[] line = scan.nextLine().replaceAll(" ", "").split(":");
				data.put(line[0], line[1]);
			}
			
			FontMaterial fm = new FontMaterial();
			
			for (String key : data.keySet()){
				String value = data.get(key);
				switch (key) {
				case "Font": 			fm.font 			= TextMaster.getFontType(value);	break;
				case "Color":			fm.color 			= Vector4f.parse(value);   			break;
				case "Width": 			fm.width 			= Float.parseFloat(value); 			break;
				case "DecayRate": 		fm.decayRate 		= Float.parseFloat(value); 			break;
				case "BorderWidth": 	fm.borderWidth		= Float.parseFloat(value); 			break;
				case "BorderColor": 	fm.borderColor 		= Vector4f.parse(value);   			break;
				case "BorderOffset": 	fm.borderOffset 	= Vector2f.parse(value);   			break;
				case "BorderDecayRate": fm.borderDecayRate 	= Float.parseFloat(value); 			break;
				}
			}
			return fm;
			
		} catch (FileNotFoundException e) {
			System.err.printf("Cluld not load the file \"%s\"\n", filename);
			e.printStackTrace();
		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public FontMaterial(FontMaterial material){
		this.font = material.font;
		this.color = new Vector4f(material.color);
		this.borderColor = new Vector4f(material.borderColor);
		this.width = material.width;
		this.borderWidth = material.borderWidth;
	}
	
	public void use(FontShader shader){
		// TODO:
	}

	public void load(FontShader shader) {
		shader.loadMaterial(this);
	}
	
}
