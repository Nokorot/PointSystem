package guis;

import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import scene.Entity;
import scene.Model;
import scene.Skin;
import utils.Maths;
import utils.Matrix4f;
import utils.Vector2f;
import utils.Vector3f;

public class GuiRenderer {
	
	private static GuiShader shader;
	
	public static void init() {
		shader = new GuiShader();
	}
	
	public static void render(Map<Model, List<Entity>> entities){
		shader.start();
		
		for (Model model : entities.keySet()){ 
			
			model.getVao().bind(0);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			
			for (Entity entity : entities.get(model)){
				entity.getSkin().getDiffuseTexture().bindToUnit(0);
				
				Matrix4f matrix = Maths.createTransformationMatrix(new Vector2f(), new Vector2f(1, 1), 0);
				shader.projectionViewMatrix.loadMatrix(matrix);
				
				
				GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, model.getVao().id);
			}
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			GL11.glDisable(GL11.GL_BLEND);
			GL20.glDisableVertexAttribArray(0);
			GL30.glBindVertexArray(0);
		}
		
		
		shader.stop();
	}
	
	public static void cleanUp(){
		shader.cleanUp();
	}

}
