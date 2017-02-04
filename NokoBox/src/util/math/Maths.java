package util.math;

import java.util.Vector;

public class Maths {
	
	public static int clamp(int x, int min, int max){
		return x < min ? min : x > max ? max : x;
	}

	public static double sum(double[] x) {
		double sum = 0.0D;
		for (double d : x)
			sum += d;
		return sum;
	}

	public static double sum(Vector<Double> x) {
		double sum = 0.0D;
		for (double d : x)
			sum += d;
		return sum;
	}

}
