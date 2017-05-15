package util.swing.gride;

import java.awt.Component;
import java.awt.Insets;
import java.awt.Rectangle;

public class Box {

	public double x, y, width, height;
	public Insets insets = new Insets(3, 3, 3, 3);

	private Component component;
	private BoxGrid gridBag;

	public Box(double x, double y, double width, double height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public void increaseBounds(double x, double y, double width, double height) {
		setBounds(this.x + x, this.y + y, this.width + width, this.height + height);
	}

	public void setBounds(double x, double y, double width, double height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;

		if (component != null)
			component.setBounds(getRectangle());

		if (gridBag != null)
			gridBag.updateBounds(getRectangle());
	}

	public void setInsets(Insets insets) {
		this.insets = insets;
	}

	public void setComponent(Component component) {
		component.setBounds(getRectangle());
		this.component = component;
		gridBag = null;
	}

	public BoxGrid getInsideGrid(double[] X, double[] Y) {
		gridBag = new BoxGrid(getRectangle(), X, Y);
		if (component != null)
			component.setVisible(false);
		component = null;
		return gridBag;
	}

	public BoxGrid getInsideGrid(int x, double[] Y) {
		return getInsideGrid(BoxGrid.newD(x), Y);
	}

	public BoxGrid getInsideGrid(double[] X, int y) {
		return getInsideGrid(X, BoxGrid.newD(y));
	}

	public BoxGrid getInsideGrid(int x, int y) {
		return getInsideGrid(BoxGrid.newD(x), BoxGrid.newD(y));
	}

	public Rectangle getRectangle() {
		return new Rectangle((int) x + insets.left, (int) y + insets.top, (int) width - insets.left
				- insets.right, (int) height - insets.top - insets.bottom);
	}

	public String toString() {
		return "[x = " + x + ", y = " + y + ", width = " + width + ", height = " + height + "]";
	}

	public void setInnerGrid(BoxGrid innerGrid) {
		gridBag = innerGrid;
		component = null;
		if (gridBag != null)
			gridBag.updateBounds(getRectangle());
	}

	public void setVisible(boolean visable) {
		if (component != null)
			component.setVisible(visable);

		if (gridBag != null)
			for (Box box : gridBag)
				box.setVisible(visable);
	}
}