package utils;

public class Vector4f {
	
	public float x, y, z, w;

	public static Vector4f parse(String s) throws NumberFormatException {
		float x, y, z, w;
	
		String[] ss = s.split(",");

		x = Float.parseFloat(ss[0]);
		y = Float.parseFloat(ss[1]);
		z = Float.parseFloat(ss[2]);
		w = Float.parseFloat(ss[3]);
		
		return new Vector4f(x, y, z, w);
	}
	
	public Vector4f(){
		this(0, 0, 0, 0);
	}
	
	public Vector4f(Vector4f other){
		this(other.x, other.y, other.z, other.w);
	}
	
	public Vector4f(float x, float y, float z, float w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	
}
