package util.swing;

import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;

import util.Window;
import util.swing.gride.BoxObject;

public class NBSlider extends JSlider implements BoxObject {
	private static final long serialVersionUID = 1L;
	
	private int mult = 1;
	
	public NBSlider(Window window, int min, int max, int init){
		super(min, max, init);
		addChangeListener((ChangeEvent e) -> onAction());
	}
	
	public NBSlider(Window window, float min, float max, float init, int mult) {
		this(window, (int) (min*mult), (int) (max * mult), (int) (init*mult));
		this.mult = mult;
	}
	
	public NBSlider(Window window, float min, float max, float init) {
		this(window, min, max, init, 100);
	}
	
	public float getFloatValue() {
		return (float) super.getValue() / mult;
	}
	
	public void setFloatValue(float f) {
		super.setValue((int) (f * mult));
	}
	
	protected void onAction() {
	}
	
	public void setPane(JPanel pane) {
		pane.add(this);
	}

}
