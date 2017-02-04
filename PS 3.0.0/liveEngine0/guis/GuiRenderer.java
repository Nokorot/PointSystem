package guis;

import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import scene.Entity;
import scene.Model;
import utils.Maths;
import utils.Matrix4f;
import utils.Vector2f;

public class GuiRenderer {

	private static Model quad;
	private static GuiShader shader;

	public static void init() {
		shader = new GuiShader();

		float[] positions = { -1, 1, -1, -1, 1, 1, 1, -1 };
		quad = Model.polygon(positions, 2);
	}

	public static void render(List<GuiTexture> guis) {
		if (shader == null)
			System.err.println("You have to initielize the GuiRenderer.");
		shader.start();
		quad.getVao().bind(0);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		for (GuiTexture gui : guis) {
			if (!gui.visible) continue;
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, gui.getTexture());
			Matrix4f matrix = Maths.createTransformationMatrix(gui.getPosiotion(), gui.getScale(), gui.getRotation());
			shader.projectionViewMatrix.loadMatrix(matrix);
			gui.load(shader);
			GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, quad.getVao().getIndexCount());
		}
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_BLEND);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		shader.stop();
	}

	public static void render(Map<Model, List<Entity>> entities) {
		shader.start();

		for (Model model : entities.keySet()) {

//			GL11.glOrtho(l, r, b, t, n, f);
			
			model.getVao().bind(0);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glEnable(GL11.GL_DEPTH_TEST);

			for (Entity entity : entities.get(model)) {
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

	public static void cleanUp() {
		shader.cleanUp();
	}

}
