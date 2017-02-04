package guis;

import engine.Display;
import textures.Texture;
import utils.Matrix4f;
import utils.Vector2f;

public class GuiTexture {

	public static final int S_STRETCHE	= 0;
	public static final int S_FILL 		= 1;
	public static final int S_FITT 		= 2;
	
	public boolean visible = true;
	
	protected final Texture texture;
	public Vector2f posiotion;
	public Vector2f scale;
	public float rotation;
	public int scaleType = S_STRETCHE;
	
	public GuiTexture(Texture texture, Vector2f posiotion, Vector2f scale, float rotation) {
		this.texture = texture;
		this.posiotion = posiotion;
		this.scale = scale;
		this.rotation = rotation;
	}
	
	public GuiTexture(Texture texture){
		this.texture = texture;
		this.posiotion = new Vector2f(.5f, .5f);
		this.scale = new Vector2f(1, 1);
		this.rotation = 0;
	}

	public int getTexture() {
		return texture.ID;
	}

	public Vector2f getPosiotion() {
		return posiotion;
	}

	public Vector2f getScale() {
		return scale;
	}

	public float getRotation() {
		return rotation;
	}
	
	public void load(GuiShader shader){
		float asprD = Display.aspect * scale.x / scale.y;
		float aspr = (float) texture.width / texture.height;
		
		float x=1,y=1;
		if (scaleType > 1)
			if (asprD > aspr)
				x = asprD / aspr;
			else
				y = aspr / asprD;
		else if (scaleType > 0)
			if (asprD < aspr)
				x = asprD / aspr;
			else
				y = aspr / asprD;
		Matrix4f scaleMatrix = Matrix4f.scale(x, y, 1);
		
		shader.scaleMatrix.loadMatrix(scaleMatrix);
		
	}

}
