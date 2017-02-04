package 
utils;

public class Vector3f extends Vector {
	
	public float x, y, z;

	public Vector3f(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vector3f() {
		this(0, 0, 0);
	}

	public void set(Vector3f other) {
		this.x = other.x;
		this.y = other.y;
		this.z = other.z;
	}
	
	public float lengthSquerd(){
		return x * x + y * y + z * z;
	}
	
	public float length() {
		return (float) Math.sqrt(lengthSquerd());
	}

	public void normalize() {
		float length = length();
		this.x /= length;
		this.y /= length;
		this.z /= length;
	}
	
	public Vector3f normalized() {
		float length = length();
		float x = this.x / length;
		float y = this.y / length;
		float z = this.z / length;
		return new Vector3f(x, y, z);
	}

}
