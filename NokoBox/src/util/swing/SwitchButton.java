package util.swing;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import util.Window;
import util.handelers.ImageHandeler.ScaleType;

public class SwitchButton extends Button {
	private static final long serialVersionUID = 1L;

	public static final int NON = 0;
	public static final int STRING = 1;
	public static final int COLOR = 2;
	public static final int IMAGE = 3;

	public static String SString0 = "Off";
	public static String SString1 = "On";
	public static Color SColor0 = Color.GRAY;
	public static Color SColor1 = Color.CYAN;
	
	private int switchType = STRING;
	private List<String> strings = new ArrayList<String>();
	private List<Color> colors = new ArrayList<Color>();
	private List<BufferedImage> images = new ArrayList<BufferedImage>();
	private ScaleType scaleType = ScaleType.NOTSCALE;

	private int size = 0;
	private int active = 0;

	public SwitchButton(Window window, int type, Object... objects) {
		super(window);
		this.switchType = type;
		
		addObjects(objects);
		
		addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setActive(active + 1);
			}
		});
		
		setActive(0);
	}
	
	public static SwitchButton DefaultDoubleString(Window window){
//		return new SwitchButton(window, STRING, window.defaultSwitch.doubleString);
		return new SwitchButton(window, STRING, SString0, SString1);
	}
	
	public static SwitchButton DefaultDoubleColor(Window window){
//		return new SwitchButton(window, STRING, window.defaultSwitch.doubleColor);
		return new SwitchButton(window, COLOR, SColor0, SColor1);
	}
	
	@Override
	public void setBounds(int x, int y, int width, int height) {
		super.setBounds(x, y, width, height);
		update();
	}
	
	public void setSwitchType(int switchType) {
		if(switchType > 3 || switchType < 0 || this.switchType == switchType)
			return;
		clearButton();
		this.switchType = switchType;
		
		updateSize();
		setActive(0);
	}

	public void setImageScaleing(ScaleType scaleType) {
		this.scaleType = scaleType;
		update();
	}

	public void setActive(int active){
		if(size == 0)
			return;
		this.active = active % size;
		update();
	}
	
	public int getActiveIndex() {
		return active;
	}

//	public String getSelectedObject() {
//		return null;
//	}

	private void addObjects(Object... objects) {
		for (Object object : objects) {
			if(object instanceof String)
				strings.add((String) object);
			if(object instanceof Color)
				colors.add((Color) object);
			if(object instanceof BufferedImage)
				images.add((BufferedImage) object);
		}
		updateSize();
		update();
	}
	
	private void updateSize() {
		switch (switchType) {
		case STRING:
			size = strings.size();
			break;
		case COLOR:
			size = colors.size();
			break;
		case IMAGE:
			size = images.size();
			break;
		default:
			size = 0;
			break;
		}
	}
	
	private void clearButton(){
		setText("");
		setBackground(window.buttonSets.bColor);
		setIcon(null);
	}
	
	private void update(){
		switch (switchType) {
		case STRING:
			setText(strings.get(active));
			break;
		case COLOR:
			setBackground(colors.get(active));
			break;
		case IMAGE:
			setIcon(images.get(active), scaleType);
			break;
		default:
			size = 0;
			break;
		}
	}
}
