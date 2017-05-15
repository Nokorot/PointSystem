package util.swing.gride;

import java.awt.Insets;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import util.swing.gride.Box;

public class BoxGrid implements Iterable<Box> {

	private Rectangle lastBounds;

	private Box[] boxes;

	// boxes covering several other boxes.
	private List<Box> mBoxes = new ArrayList<Box>();

	private int koloms, rows;
	private double xETot, yETot;
	private double[] expX;
	private double[] expY;

	public BoxGrid(Rectangle bounds, double[] X, double[] Y) {
		koloms = X.length;
		rows = Y.length;

		boxes = new Box[X.length * Y.length];

		expX = newD(X.length);
		expY = newD(Y.length);
		xETot = X.length;
		yETot = Y.length;

		lastBounds = bounds;

		double xTot = getSum(X);
		double yTot = getSum(Y);

		double width1 = bounds.getWidth() / xTot;
		double height1 = bounds.getHeight() / yTot;

		double xp = bounds.getX();
		double yp = bounds.getY();

		for (int y = 0; y < Y.length; y++) {
			for (int x = 0; x < X.length; x++) {
				boxes[x + y * X.length] = new Box(xp, yp, X[x] * width1, Y[y] * height1);

				xp += X[x] * width1;
			}

			yp += Y[y] * height1;
			xp = bounds.getX();
		}
	}

	public BoxGrid(Rectangle bounds, int xSize, int ySize) {
		this(bounds, newD(xSize), newD(ySize));
	}

	public BoxGrid(Rectangle bounds, double[] x, int ySize) {
		this(bounds, x, newD(ySize));
	}

	public BoxGrid(Rectangle bounds, int xSize, double[] y) {
		this(bounds, newD(xSize), y);
	}

	public static double[] newD(int s) {
		double[] r = new double[s];
		for (int i = 0; i < s; i++)
			r[i] = 1.0D;
		return r;
	}

	private static double getSum(double[] ds) {
		double resut = 0.0d;
		for (int i = 0; i < ds.length; i++)
			resut += ds[i];
		return resut;
	}

	public void updateBounds(Rectangle bounds) {
		if (!lastBounds.equals(bounds)) {
			resize(bounds.x - lastBounds.x, bounds.y - lastBounds.y, bounds.getWidth() - lastBounds.width,
					bounds.getHeight() - lastBounds.height);
			lastBounds = bounds;
		}
		chekSize(bounds);
	}

	private void resize(double dX, double dY, double dWidth, double dHeight) {
		dWidth /= xETot;
		dHeight /= yETot;

		double xp = dX;
		double yp = dY;

		for (int y = 0; y < rows; y++) {
			double ay = dHeight * expY[y];
			for (int x = 0; x < koloms; x++) {
				double ax = dWidth * expX[x];

				boxes[x + y * koloms].increaseBounds(xp, yp, ax, ay);
				xp += ax;
			}
			yp += ay;
			xp = dX;
		}

	}

	private void chekSize(Rectangle bounds) {
		Box b = boxes[boxes.length - 1];
		if (!(b.x + b.width == bounds.getWidth() + bounds.getX()))
			resize(0, 0, (bounds.getWidth() + bounds.getX()) - (b.x + b.width), 0);

		if (!(b.y + b.height == bounds.getHeight() + bounds.getY()))
			resize(0, 0, 0, (bounds.getHeight() + bounds.getY()) - (b.y + b.height));
	}

	public void setExp(double[] x, double[] y) {
		this.expX = x;
		this.expY = y;

		xETot = 0.0D;
		for (int i = 0; i < koloms && i < x.length; i++) {
			this.xETot += this.expX[i];
		}
		yETot = 0.0D;
		for (int i = 0; i < rows && i < y.length; i++) {
			yETot += this.expY[i];
		}
	}

	public void setAllInsetsTo(Insets insets) {
		for (Box box : boxes) {
			box.setInsets(insets);
		}
	}
	
	public Box getBox(int index) {
		return boxes[index];
	}

	public Box getBox(int x, int y) {
		return boxes[x + y * koloms];
	}

	
	/**
	 * 
	 * foreach loop stuf
	 * 
	 **/

	public int size() {
		return boxes.length;
	}

	private Box get(int i) {
		return boxes[i];
	}

	@Override
	public Iterator<Box> iterator() {
		return new MyIterator();
	}

	private class MyIterator implements Iterator<Box> {

		private int index = 0;

		public boolean hasNext() {
			return index < size();
		}

		public Box next() {
			return get(index++);
		}

		public void remove() {
			throw new UnsupportedOperationException("not supported yet");

		}
	}

	public void setAllVisible(boolean b){
		for (Box box : boxes) {
			box.setVisible(b);
		}
	}
	
	/*
	 * public Box getBox(gCoord c) { Box retult = Grid[(c.x + c.y * koloms)];
	 * 
	 * int widtha = 0; for (int i = 0; i < c.width; i++) { widtha += Grid[(c.x +
	 * i + c.y* koloms)].width; } int heighta = 0; for (int i = 0; i < c.height;
	 * i++) { heighta += Grid[(c.x + (c.y + i) * koloms)].height; }
	 * 
	 * return new Box(rec1.x, rec1.y, widtha, heighta);
	 */
	// }
}