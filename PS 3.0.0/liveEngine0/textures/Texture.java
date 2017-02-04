package textures;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

public class Texture {

	public final int ID;
	public final int width, height;
	private final int type;

	protected Texture(int id, int width, int height) {
		this.ID = id; 
		this.width = width;
		this.height = height;
		this.type = GL11.GL_TEXTURE_2D;
	}
	
	protected Texture(int id, int width, int height, int type) {
		this.ID = id; 
		this.width = width;
		this.height = height;
		this.type = type;
	}

	public void bindToUnit(int unit) {
		GL13.glActiveTexture(GL13.GL_TEXTURE0 + unit);
		GL11.glBindTexture(type, ID);
	}

	public void delete() {
		GL11.glDeleteTextures(ID);
	}

	/* public static TextureBuilder newTexture(String path) {
		return new TextureBuilder(textureFile);
	}

	public static Texture newCubeMap(MyFile[] textureFiles) {
		int cubeMapId = TextureUtils.loadCubeMap(textureFiles);
		//TODO needs to know size!
		return new Texture(cubeMapId, GL13.GL_TEXTURE_CUBE_MAP, 0);
	}
	
	public static Texture newEmptyCubeMap(int size) {
		int cubeMapId = TextureUtils.createEmptyCubeMap(size);
		return new Texture(cubeMapId, GL13.GL_TEXTURE_CUBE_MAP, size);
	}*/

}
