package util.math;

import util.adds.Logable;

public class Vec3f implements Logable {
	public float x;
	public float y;
	public float z;
	public static Vec3f empty = new Vec3f(0.0F, 0.0F, 0.0F);

	public Vec3f(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public String toLogString() {
		return this.x + ", " + this.y + ", " + this.z;
	}

	public void fromLogString(String s) {
		String[] v = s.split(", ");
		this.x = Float.parseFloat(v[0]);
		this.y = Float.parseFloat(v[1]);
		this.z = Float.parseFloat(v[2]);
	}
}