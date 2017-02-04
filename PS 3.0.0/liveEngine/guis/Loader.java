package guis;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class Loader {

	private static final String TEXTURE_RES_LOC = "res/texture/";
	private static final String FONT_ATLAS_RES_LOC = "res/font/";

	private static List<Integer> vaos = new ArrayList<Integer>();
	private static List<Integer> vbos = new ArrayList<Integer>();
	private static List<Integer> textures = new ArrayList<Integer>();

	public RawModel loadToVAO(float[] positions, float[] textureCoords, float[] normals, int[] indices) {
		int vaoID = createVAO();
		bindIndicesBuffer(indices);
		storeDatainAttributeList(0, 3, positions);
		storeDatainAttributeList(1, 2, textureCoords);
		storeDatainAttributeList(2, 3, normals);
		unbindVAO();
		return new RawModel(vaoID, indices.length);
	}

	public int loadToVAO(float[] positions, float[] textureCoords) {
		int vaoID = createVAO();
		storeDatainAttributeList(0, 2, positions);
		storeDatainAttributeList(1, 2, textureCoords);
		unbindVAO();
		return vaoID;
	}

	public RawModel loadToVAO(float[] positions, float[] textureCoords, float[] normals, float[] tangents,
			int[] indices) {
		int vaoID = createVAO();
		bindIndicesBuffer(indices);
		storeDatainAttributeList(0, 3, positions);
		storeDatainAttributeList(1, 2, textureCoords);
		storeDatainAttributeList(2, 3, normals);
		storeDatainAttributeList(3, 3, tangents);
		unbindVAO();
		return new RawModel(vaoID, indices.length);
	}

	public static RawModel loadToVAO(float[] positions, int dimentions, float[] normals) {
		int vaoID = createVAO();
		storeDatainAttributeList(0, dimentions, positions);
		unbindVAO();
		return new RawModel(vaoID, positions.length / dimentions);
	}

	public static RawModel loadToVAO(float[] positions, int dimentions) {
		int vaoID = createVAO();
		storeDatainAttributeList(0, dimentions, positions);
		unbindVAO();
		return new RawModel(vaoID, positions.length / dimentions);
	}

	private static final int BYTES_PER_PIXEL = 4;

	@SuppressWarnings("resource")
	public static int newTexture(final String path) {
		int texture = 0;
		try {
			InputStream IS = new FileInputStream(path);//Loader.class.getResourceAsStream("../../" + path);
			ByteArrayOutputStream BAOS = new ByteArrayOutputStream();
			int read1 = IS.read();
			while (read1 != -1) {
				BAOS.write(read1);
				read1 = IS.read();
			}
			byte[] textureBA = BAOS.toByteArray();
			BAOS.close();
			BufferedImage textureBI = ImageIO.read(new ByteArrayInputStream(textureBA));
			texture = loadTexture(textureBI);
			// Loader.log("Texture load > Buffered image: " +
			// textureBI.getWidth() + "x" + textureBI.getHeight() + " / Texture
			// ID: " + texture);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return texture;
	}

	private static int loadTexture(BufferedImage image) {
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
		return textureID;
	}

	
	 * public int loadTexture(String fileName) { Texture texture = null; try {
	 * texture = TextureLoader.getTexture("PNG", new
	 * FileInputStream(TEXTURE_RES_LOC + fileName + ".png"));
	 * GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
	 * GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER,
	 * GL11.GL_LINEAR_MIPMAP_LINEAR); GL11.glTexParameterf(GL11.GL_TEXTURE_2D,
	 * GL14.GL_TEXTURE_LOD_BIAS, -.4f); } catch (FileNotFoundException e) {
	 * e.printStackTrace(); } catch (IOException e) { e.printStackTrace(); } int
	 * id = texture.getTextureID(); textures.add(id); return id; }
	 * 
	 * 
	 * private TextureData decodeTextureFile(String fileName) { int width = 0;
	 * int height = 0; ByteBuffer buffer = null; try { FileInputStream in = new
	 * FileInputStream(fileName); PNGDecoder decoder = new PNGDecoder(in); width
	 * = decoder.getWidth(); height = decoder.getHeight(); buffer =
	 * ByteBuffer.allocateDirect(4 * width * height); decoder.decode(buffer,
	 * width * 4, Format.RGBA); buffer.flip(); in.close(); } catch (Exception e)
	 * { e.printStackTrace(); System.err.println("Tried to load texture " +
	 * fileName + ", didn't work"); System.exit(-1); } return new
	 * TextureData(buffer, width, height); }
	 

	private static int createVAO() {
		int vaoID = GL30.glGenVertexArrays();
		vaos.add(vaoID);
		GL30.glBindVertexArray(vaoID);
		return vaoID;
	}

	public void cleanUp() {
		for (Integer vao : vaos) {
			GL30.glDeleteVertexArrays(vao);
		}
		for (Integer vbo : vbos) {
			GL15.glDeleteBuffers(vbo);
		}
		for (Integer texture : textures) {
			GL11.glDeleteTextures(texture);
		}
	}

	private static void storeDatainAttributeList(int attributeNumber, int coordinataSize, float[] data) {
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		FloatBuffer buffer = storeDataInFloatBuffer(data);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(attributeNumber, coordinataSize, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}

	private static void unbindVAO() {
		GL30.glBindVertexArray(0);
	}

	private void bindIndicesBuffer(int[] indices) {
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
		IntBuffer buffer = storeDataInIntBuffer(indices);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
	}

	private IntBuffer storeDataInIntBuffer(int[] data) {
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}

	private static FloatBuffer storeDataInFloatBuffer(float[] data) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}

}
