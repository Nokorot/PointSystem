package util.math;

import java.awt.Color;
import util.adds.Logable;

public class Vec4i implements Logable {
	public static Vec4i empty = new Vec4i(0, 0, 0, 0);

	public static final Vec4i RED = new Vec4i(255, 0, 0, 255);
	public static final Vec4i GREEN = new Vec4i(0, 255, 0, 255);
	public static final Vec4i BLUE = new Vec4i(0, 0, 255, 255);
	public static final Vec4i BLACK = new Vec4i(0, 0, 0, 255);
	public static final Vec4i WHITE = new Vec4i(255, 255, 255, 255);
	public int x;
	public int y;

	public Vec4i(int x, int y, int z, int w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}

	public int z;
	public int w;

	public boolean equals(Vec4i v) {
		return (this.x == v.x) && (this.y == v.y) && (this.z == v.z) && (this.w == v.w);
	}

	public String toLogString() {
		return this.x + ", " + this.y + ", " + this.z + ", " + this.w;
	}

	public void fromLogString(String s) {
		String[] v = s.split(", ");
		this.x = Integer.parseInt(v[0]);
		this.y = Integer.parseInt(v[1]);
		this.z = Integer.parseInt(v[2]);
		this.w = Integer.parseInt(v[3]);
	}

	public int getIntColor() {
		return this.w << 24 + this.y << 16 + this.z << 8 + this.w;
	}

	public static Vec4i fromColor(Color col) {
		return new Vec4i(col.getRed(), col.getGreen(), col.getBlue(), col.getBlue());
	}
}