package engineTest;
import java.awt.image.BufferedImage;

import guis.GuiTexture;
import textures.Texture;
import textures.TextureUtils;

public class LiveImage extends GuiTexture {
	
	
//	public static enum ScaleType {
//		FILL, STRETSCH, FITT;
//	}
	
//	public ScaleType scaleType;

	public LiveImage(String path) {
		super(load(path));
	}

	public LiveImage(BufferedImage image) {
		super(load(image));
	}
	
	private static Texture load(String path){
		return TextureUtils.loadTexture(path);
	}
	
	private static Texture load(BufferedImage image){
		return TextureUtils.loadTexture(image);
	}

//	public Vector2f getScale() {
//		float aspr = (float) this.texture.width / this.texture.height;
//		float daspr = Display.aspect;
//		if (scaleType == ScaleType.STRETSCH)
//			return scale;
//		else if (scaleType == ScaleType.FILL)
//			if (aspr > 1)
//				return scale.mult(  )
//		
//		return scale;
//	}
	
}
