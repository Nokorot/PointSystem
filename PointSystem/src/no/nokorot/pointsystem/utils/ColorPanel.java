package no.nokorot.pointsystem.utils;
import java.awt.Color;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JColorChooser;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;

import com.thecherno.raincloud.serialization.RCArray;
import com.thecherno.raincloud.serialization.RCObject;

import util.Window;
import util.swing.ImageList;
import util.swing.NBButton;
import util.swing.gride.BoxObject;
import util.swing.gride.XStrip;
import util.swing.gride.YStrip;

public class ColorPanel implements BoxObject, ActionListener {

	public static interface ColorPanalHandaler {
		public void elementSelected(ColorPanel panal);
	}

	private Window window;
	
	private YStrip root;
	private static ImageList colorList;

	private List<Color> colors = new ArrayList<>();
	private List<ColorPanalHandaler> handalers = new ArrayList<>();

	public ColorPanel(Window window) {
		this.window = window;

		root = new YStrip();

		XStrip x = new XStrip();

		NBButton newColor = new NBButton(window, "new", "newColor");
		newColor.addActionListener(this);
		x.append(newColor);

		NBButton pickColor = new NBButton(window, "pick", "pickColor");
		pickColor.addActionListener(this);
		x.append(pickColor);
		
		NBButton removeColor = new NBButton(window, "track", "removeColor");
		removeColor.addActionListener(this);
		x.append(removeColor);

		root.append(x);
		
		colorList = new ImageList(window, "colorList");
		colorList.setBackground(Color.LIGHT_GRAY);
		colorList.addListSelectionListener((ListSelectionEvent e) -> {
			if (e.getValueIsAdjusting()) {
				for (ColorPanalHandaler handaler : handalers) {
					handaler.elementSelected(this);
				}
			}
		});
		root.append(colorList, 6);
		
	}

	public void actionPerformed(ActionEvent e) {
		switch (((NBButton) e.getSource()).code) {
		case "newColor": 
			addColor(JColorChooser.showDialog(null, "Find a color", Color.white));
			break;
		case "pickColor": 
			List<Color> c = new ColorPicker(window).pickColors();
			System.out.println(c);
			addColors( c );
			break;
		case "removeColor":
			removeIndex(colorList.getSelectedIndex());
			return;
		}
	}
	
	private void addColors(List<Color> colors) {
		for (Color color : colors)
			addColor(color);
	}

	public void addColor(Color color) {
		System.out.println(color);
		if (color == null)
			return;
		if (!colors.contains(color)) {
			BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
			image.setRGB(0, 0, color.getRGB());
			colors.add(color);
			colorList.addImage(image, true);
			colorList.repaint();
		}
	}
	
	public void removeIndex(int i){
		if (i < 0 || i > colors.size()) return;
		colors.remove(i);
		colorList.removeImage(i);
	}

	public Color getSelectedColor() {
		int i = colorList.getSelectedIndex();
		if (i < 0) return null;
		Color c = new Color(colorList.getImage(i).getRGB(0, 0));
		return c;
	}

	public Image getSelectedColorImage() {
		int i = colorList.getSelectedIndex();
		if (i < 0) return null;
		return colorList.getImage(i);
	}

	public void addColorPanalHandaler(ColorPanalHandaler handaler) {
		handalers.add(handaler);
	}

	public void setBounds(Rectangle rectangle) {
		root.setBounds(rectangle);
	}

	public void setVisible(boolean visable) {
		root.setVisible(visable);
	}

	public RCObject asRCObject() {
		RCObject object = new RCObject("ColorPanal");
		
		int[] data = new int[colors.size()];
		for (int i = 0; i < colors.size(); i++)
			data[i] = colors.get(i).getRGB();
		
		object.addArray( RCArray.Integer("colors", data) );
		
		return object;
	}

	public void loadRCObject(RCObject subObject) {
		if (subObject == null)
			return;

		RCArray array = subObject.getArray("colors");
		if (array == null)
			return;
		
		int[] data = (int[]) array.getData();
		
		for (int i : data)
			addColor(new Color(i));
		
		colorList.repaint();
	}

	public void setPane(JPanel pane) {
		root.setPane(pane);
	}
	
}