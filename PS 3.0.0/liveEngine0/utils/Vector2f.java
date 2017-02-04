package utils;

public class Vector2f extends Vector {
	
	public float x, y;

	public static Vector2f parse(String s) throws NumberFormatException {
		float x, y;
	
		String[] ss = s.split(",");

//		if (ss.length != 2)
//			throw new NumberFormatException("Wrong amount of parameters.");
		
		x = Float.parseFloat(ss[0]);
		y = Float.parseFloat(ss[1]);
		
		return new Vector2f(x, y);
	}
	
	public Vector2f(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public Vector2f(Vector2f other){
		this.x = other.x;
		this.y = other.y;
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

	public Vector2f add(Vector2f other) {
		return new Vector2f(this.x + other.x, this.y + other.y);
	}

	public Vector2f mult(Vector2f other) {
		return new Vector2f(this.x * other.x, this.y * other.y);
	}

	public Vector2f scale(float s) {
		return new Vector2f(this.x * s, this.y * s);
	}

	public Vector2f mult(float x, float y) {
		return new Vector2f(this.x * x, this.y * y);
	}

}
