package util.swing.gride;

import java.awt.Rectangle;

public class Grid extends Rectangle {
	private static final long serialVersionUID = 1L;
	public Rectangle[] Grid;
	private int width;
	private int height;
	private int space;
	private double totX;
	private double totY;
	private double eTotX;
	private double eTotY;
	private double[] X;
	private double[] Y;
	private double[] expendX;
	private double[] expendY;

	public Grid(Rectangle rec, int xSize, int ySize, int space, boolean Static) {
		if (Static) {
			this.width = xSize;
			this.height = ySize;

			this.Grid = new Rectangle[xSize * ySize];

			for (int i = 0; i < this.Grid.length; i++) {
				this.Grid[i] = new Rectangle(rec.x + i % xSize * (rec.width + space), rec.y + i / xSize * (rec.width + space), rec.width, rec.width);
			}
		} else {
			setBounds(rec);
			this.X = new double[xSize];
			this.Y = new double[ySize];
			for (int i = 0; i < this.X.length; i++)
				this.X[i] = 1.0D;
			for (int i = 0; i < this.Y.length; i++) {
				this.Y[i] = 1.0D;
			}
			this.width = this.X.length;
			this.height = this.Y.length;
			this.space = space;

			this.totX = this.width;
			this.totY = this.height;

			this.expendX = new double[this.X.length];
			this.expendY = new double[this.Y.length];

			this.Grid = new Rectangle[this.width * this.height];

			setRec();
		}
	}

	public Grid(Rectangle rec, double[] x, double[] y, int space) {
		setBounds(rec);
		this.X = x;
		this.Y = y;
		this.width = x.length;
		this.height = y.length;
		this.space = space;

		this.expendX = new double[x.length];
		this.expendY = new double[y.length];

		this.Grid = new Rectangle[this.width * this.height];

		this.totX = 0.0D;
		for (int i = 0; i < this.width; i++) {
			this.totX += x[i];
		}
		this.totY = 0.0D;
		for (int i = 0; i < this.height; i++) {
			this.totY += y[i];
		}

		setRec();
	}

	public static Grid split(String type, Rectangle rec, double[] d) {
		if (type.equals("X"))
			return new Grid(rec, d, new double[] { 1.0D }, 1);
		if (type.equals("Y")) {
			return new Grid(rec, new double[] { 1.0D }, d, 1);
		}
		return new Grid(rec, new double[] { 1.0D }, new double[] { 1.0D }, 0);
	}
	
	public static Grid split(String type, Rectangle rec, int i){
		double[] d = new double[i];
		for (double e : d)
			e = 1;
		return split(type, rec, d);
	}

	private void setRec() {
		double width1 = getWidth() / this.totX;
		double height1 = getHeight() / this.totY;

		double xp = 0.0D;
		double yp = 0.0D;
		for (int y1 = 0; y1 < this.height; y1++) {
			for (int x1 = 0; x1 < this.width; x1++) {
				this.Grid[(x1 + y1 * this.width)] = new Rectangle((int) (xp + getX()), (int) (yp + getY()), (int) (this.X[x1] * width1 - this.space),
						(int) (this.Y[y1] * height1 - this.space));
				xp += this.X[x1] * width1;

				if (x1 == this.width - 1) {
					this.Grid[(x1 + y1 * this.width)].width += this.space;
				}
				if (y1 == this.height - 1) {
					this.Grid[(x1 + y1 * this.width)].height += this.space;
				}
			}
			yp += this.Y[y1] * height1;
			xp = 0.0D;
		}
	}

	public void setExpending(double[] x, double[] y) {
		for (int i = 0; i < x.length; i++) {
			this.expendX[i] = x[i];
		}
		for (int i = 0; i < y.length; i++) {
			this.expendY[i] = y[i];
		}

		this.eTotX = 0.0D;
		for (int i = 0; i < this.width; i++) {
			this.eTotX += this.expendX[i];
		}
		this.eTotY = 0.0D;
		for (int i = 0; i < this.height; i++) {
			this.eTotY += this.expendY[i];
		}
	}

	public Rectangle getRec(int x, int y) {
		return this.Grid[(x + y * this.width)];
	}

	public Rectangle getRec(int index) {
		return this.Grid[index];
	}

	public Rectangle getRec(int x1, int y1, int x2, int y2) {
		Rectangle rec1 = this.Grid[(x1 + y1 * this.width)];

		int xDif = x2 - x1;
		int yDif = y2 - y1;

		int widtha = this.space * xDif;
		for (int i = 0; i <= xDif; i++) {
			widtha += this.Grid[(x1 + i + y1 * this.width)].width;
		}
		int heighta = this.space * yDif;
		for (int i = 0; i <= yDif; i++) {
			heighta += this.Grid[(x1 + (y1 + i) * this.width)].height;
		}

		return new Rectangle(rec1.x, rec1.y, widtha, heighta);
	}

	public void reSizeWidth(int width) {
		double c1 = width;
		c1 /= getWidth();
		c1 *= this.totX;
		c1 /= this.eTotX;

		for (int i = 0; i < this.X.length; i++) {
			this.X[i] += this.expendX[i] * width;
		}

		setRec();
	}

	public int size() {
		return this.Grid.length;
	}
}