package scene;

import openglObjects.Vao;

public class Model {
	
	private final Vao vao;
	
	public Model(Vao vao){
		this.vao = vao;
	}
	
	public Vao getVao(){
		return vao;
	}
	
	public void delete(){
		vao.delete();
	}

	public static Model polygon(float[] data, int vecDims) {
//		int[] indices = new int[data.length / vecDims];
//		
//		for (int i = 0; i < indices.length; i++)
//			indices[i] = i;
		
		int[] indices = {0, 1, 2, 3};
		
		Vao vao = Vao.create();
		vao.storeData(indices, data.length / vecDims, data);
		
		return new Model(vao);
	}
	
}
