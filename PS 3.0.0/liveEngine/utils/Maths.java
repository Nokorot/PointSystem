package utils;


public class Maths {
	
	public static float barryCentric(Vector3f p1, Vector3f p2, Vector3f p3, Vector2f pos) {
		float det = (p2.z - p3.z) * (p1.x - p3.x) + (p3.x - p2.x) * (p1.z - p3.z);
		
		float l1 = ((p2.z - p3.z) * (pos.x - p3.x) + (p3.x - p2.x) * (pos.y - p3.z)) / det;
		float l2 = ((p3.z - p1.z) * (pos.x - p3.x) + (p1.x - p3.x) * (pos.y - p3.z)) / det;
		float l3 = 1.0f - l1 - l2;
		return l1 * p1.y + l2 * p2.y + l3 * p3.y;
	}

	public static Matrix4f createTransformationMatrix(Vector2f transformation, Vector2f scale, float rot){
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		matrix = matrix.multiply(Matrix4f.translate(transformation.x, transformation.y, 0));
		matrix = matrix.multiply(Matrix4f.rotate(rot, 0, 0, 1));
		matrix = matrix.multiply(Matrix4f.scale(scale.x, scale.y, 0));
		return matrix;
	}
	
	public static Matrix4f createTransformationMatrix(Vector3f transformation, Vector3f scale, float rotX, float rotY, float rotZ){
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		matrix = matrix.multiply(Matrix4f.translate(transformation.x, transformation.y, 0));
		matrix = matrix.multiply(Matrix4f.rotate(rotX, 1, 0, 0));
		matrix = matrix.multiply(Matrix4f.rotate(rotY, 0, 1, 0));
		matrix = matrix.multiply(Matrix4f.rotate(rotZ, 0, 0, 1));
		matrix = matrix.multiply(Matrix4f.scale(scale.x, scale.y, scale.z));
		return matrix;
	}
	
	public static Matrix4f createTransformationMatrix(Vector3f transformation, float scale, float rotX, float rotY, float rotZ){
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		matrix = matrix.multiply(Matrix4f.translate(transformation.x, transformation.y, 0));
		matrix = matrix.multiply(Matrix4f.rotate(rotX, 1, 0, 0));
		matrix = matrix.multiply(Matrix4f.rotate(rotY, 0, 1, 0));
		matrix = matrix.multiply(Matrix4f.rotate(rotZ, 0, 0, 1));
		matrix = matrix.multiply(Matrix4f.scale(scale, scale, scale));
		return matrix;
	}

	public static float clamp(float f, float minPich, float maxPich) {
		return f < minPich ? minPich : f > maxPich ? maxPich : f;
	}
	
}
