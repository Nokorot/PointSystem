package utils;

public class Vector2f extends Vector {
	
	public float x, y;

	public Vector2f(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public Vector2f() {
		this(0, 0);
	}

	public float lengthSquerd(){
		return x * x + y * y;
	}
	
	public float length() {
		return (float) Math.sqrt(lengthSquerd());
	}

	public Vector2f normalize() {
		float length = length();
		float x = this.x / length;
		float y = this.y / length;
		return new Vector2f(x, y);
	}

}
