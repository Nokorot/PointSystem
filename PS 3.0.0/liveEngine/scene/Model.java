package scene;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import openglObjects.Vao;
import openglObjects.Vbo;

public class Model {
	
	private final Vao vao;
	
	private Model(Vao vao) {
		this.vao = vao;
	}
	
//	public Model(String modelFile){
//		ModelData data = OBJFileLoader.loadOBJ(modelFile);
//		vao = Vao.create();
//		data.storeInVao(vao);
//		vao.storeData(data.getIndices(), data.getVertexCount(), data.getVertices(), data.getTextureCoords(),
//				data.getNormals());
//	}
	
	
	
	
}
