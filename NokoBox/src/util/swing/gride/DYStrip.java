package util.swing.gride;

public class DYStrip extends YStrip {

	public DYStrip() {
	}

	@Override
	public void setBounds(double x, double y, double width, double height){
		if (ATot == 0) return;
		
		height /= ATot;
		for (int i = 0; i < this.size(); i++) {
			double h = height * A.get(i);
			boxes.get(i).setBounds(x, y, width, h);
			y += h;
		}
	}

	@Override
	public void increaseBounds(double dX, double dY, double dWidth, double dHeight) {
		if (ETot != 0)
			dHeight /= ETot;
		double dy = dY;
		for (int i = 0; i < this.size(); i++) {
			double dh = dHeight * E.get(i);
			boxes.get(i).increaseBounds(dX, dy, dWidth, dh);
			dy += dh;
		}
	}

}
