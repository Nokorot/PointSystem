package textures;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class TextureUtils {
	
	private static final Map<String, Texture> pathTextures = new HashMap<>();

	private static final Map<BufferedImage, Texture> imageTextures = new HashMap<>();
	
	private static final int BYTES_PER_PIXEL = 4;

	public static Texture loadTexture(final String path) {
		if (pathTextures.containsKey(path))
			return pathTextures.get(path);
		
		try {
			InputStream IS = new FileInputStream(path);
			Texture texture = loadTexture(ImageIO.read(IS));
			pathTextures.put(path, texture);
			return texture;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Texture loadTexture(BufferedImage image) {
		if (imageTextures.containsKey(image))
			return imageTextures.get(image);
		
		int[] pixels = new int[image.getWidth() * image.getHeight()];
		image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());
		ByteBuffer buffer = BufferUtils .createByteBuffer(image.getWidth() * image.getHeight() * BYTES_PER_PIXEL); 
					// 4 for RGBA, 3 for RGB
		for (int y = 0; y < image.getHeight(); y++) {
			for (int x = 0; x < image.getWidth(); x++) {
				int pixel = pixels[y * image.getWidth() + x];
				buffer.put((byte) ((pixel >> 16) & 0xFF)); // Red component
				buffer.put((byte) ((pixel >> 8) & 0xFF)); // Green component
				buffer.put((byte) (pixel & 0xFF)); // Blue component
				buffer.put((byte) ((pixel >> 24) & 0xFF)); // Alpha component.
															// Only for RGBA
			}
		}
		buffer.flip(); // FOR THE LOVE OF GOD DO NOT FORGET THIS
		// You now have a ByteBuffer filled with the color data of each pixel.
		// Now just create a texture ID and bind it. Then you can newTexture it
		// using
		// whatever OpenGL method you want, for example:
		int textureID = GL11.glGenTextures(); // Generate texture ID
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID); // Bind texture ID
		// Setup wrap mode
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
		// Setup texture scaling filtering
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		// Send texel data to OpenGL
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, image.getWidth(), image.getHeight(), 0,
				GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
		// Return the texture ID so we can bind it later again
		
		Texture texture = new Texture(textureID, image.getWidth(), image.getHeight()); 
		imageTextures.put(image, texture);
		return texture;
	}

}
