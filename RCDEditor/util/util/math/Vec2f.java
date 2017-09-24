package util.math;

import util.adds.Logable;

public class Vec2f implements Comparable<Vec2f>, Logable {
	private static boolean sortY = false;

	public float x;
	public float y;
	public static Vec2f empty = new Vec2f(0.0F, 0.0F);

	public Vec2f(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public Vec2f() {
		this(0.0F, 0.0F);
	}

	public String toString() {
		return "[X = " + this.x + ", Y = " + this.y + "]";
	}

	public int compareTo(Vec2f o) {
		if (sortY) {
			return (int) (o.y - this.y);
		}
		return (int) (o.x - this.x);
	}

	public static void sortX() {
		sortY = false;
	}

	public static void sortY() {
		sortY = true;
	}

	public Vec2f times(int k) {
		return new Vec2f(this.x * k, this.y * k);
	}

	public String toLogString() {
		return this.x + ", " + this.y;
	}

	public void fromLogString(String s) {
		String[] v = s.split(", ");
		this.x = Float.parseFloat(v[0]);
		this.y = Float.parseFloat(v[1]);
	}
}