package util.swing.gride;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import util.Window;
import util.swing.Label;

public class DXStrip extends XStrip {
	
	Window w;
	
	public DXStrip(Window w) {
		this.w = w;
	}

	public void append(Box box, double a, double e){
		if (this.size() > 0) { 
			// TODO: Make XGlider static size
			XGlider g = new XGlider(w, this, this.getBox( size()-1), box);
			g.box.setLeftInset(0);
			g.box.setRightInset(0);
			super.append( g.box, 0.03, 0);
		}
		super.append(box, a, e);
	}
	
	public static class XGlider extends Label {
		private static final long serialVersionUID = 1L;
		
		private boolean hold = false;
		private int xLoc;
		private Box box;
		
		public XGlider(Window w, XStrip strip, Box left, Box right) {
			super(w);
			this.box = new Box(this);
			
			this.setOpaque(true);
			
			w.addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent e) {
					if (XGlider.this.getBounds().contains(e.getPoint())) {
						xLoc = e.getX();
						hold = true;
					}
				}
				
				public void mouseReleased(MouseEvent e) {
					hold = false;
				}
			});
			
			w.addMouseMotionListener(new MouseMotionAdapter() {
				public void mouseDragged(MouseEvent e) {
					if (hold) {
						int d = (xLoc-e.getX());
						xLoc = e.getX();
						box.increaseBounds(-d, 0, 0, 0);
						left.increaseBounds(0, 0, -d, 0);
						right.increaseBounds(-d, 0, d, 0);
					}
				}
			});
		}
	}
}
