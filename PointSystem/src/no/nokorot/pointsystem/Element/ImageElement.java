package no.nokorot.pointsystem.Element;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.event.ListSelectionEvent;

import com.thecherno.raincloud.serialization.RCObject;

import no.nokorot.pointsystem.PointSystem;
import util.handelers.FileHandler;
import util.handelers.ImageHandeler;
import util.swing.ImageList;

public class ImageElement {
	
	private static final Random random = new Random();
	
	// ExstractImageLocations
	private static String eel = PointSystem.filedLoc + "/ImageElements/";

	private static final int iconSize = 50;

	public static interface SelectedListaner {
		public void onSelction(ImageElement selectedElement);
	}
	
	private static SelectedListaner lListener;
	
	private static ImageList list;
	private static List<ImageElement> imageElements = new ArrayList<ImageElement>();

	private String name;
	private File location;
	private BufferedImage imageIcon;
	private BufferedImage image;

	public static void init(ImageList imageList, SelectedListaner listener) {
		FileHandler.createPath(eel);

		lListener = listener;
		
		list = imageList;
		list.addListSelectionListener((ListSelectionEvent e) -> {
				if (e.getValueIsAdjusting()) 
					for (ImageElement imageElement : imageElements) 
						if(imageElement.imageIcon == list.getSelectedValue().getImage()){
							listener.onSelction(imageElement);
			}
		});
	}
	
	private ImageElement() {
		imageElements.add(this);
	}

	private void createIcon(BufferedImage image) {
		try {
			imageIcon = ImageHandeler.getScaledImage(image, iconSize, iconSize);
		} catch (IOException e) {
			e.printStackTrace();
		}

		name = "IE" + random.nextLong();

		ImageHandeler.save(eel + name, imageIcon);
	}

	public BufferedImage getImage() {
		if (image != null)
			return image;
		else
			return image = ImageHandeler.loadGloab(location);
	}

	public String getPath() {
		if(location == null)
			return null;
		return location.getPath();
	}

	public static ImageElement addImage(BufferedImage image, boolean first, boolean select) {
		if (!imageElements.contains(image)) {
			if (image == null)
				return null;
			ImageElement element = new ImageElement();

			element.location = null;
			element.image = image;
			element.createIcon(image);

			if (first)
				list.addImage(0, element.imageIcon, false);
			else
				list.addImage(element.imageIcon, false);
			list.repaint();

			if (select){
				list.setSelectedIndex(first ? 0 : list.getElementCount() - 1);
				lListener.onSelction(element);
			}
			return element;
		}
		return null;
		
	}
	
	public static void addImage(File file, boolean first, boolean select) {
		if (file == null || !file.exists())
			return;
		if (!imageElements.contains(file)) {
			BufferedImage image = ImageHandeler.loadGloab(file.getPath());
			if (image == null)
				return;
			ImageElement element = new ImageElement();
			
			element.location = file;
			element.image = image;
			element.createIcon(image);

			if (first)
				list.addImage(0, element.imageIcon, false);
			else
				list.addImage(element.imageIcon, false);
			list.repaint();

			if (select){
				list.setSelectedIndex(first ? 0 : list.getElementCount() - 1);
				lListener.onSelction(element);
			}
		}
	}

	public static void addExtract(File file, String name, boolean first) {
		if (file == null || !file.exists())
			return;
		if (!imageElements.contains(file)) {
			BufferedImage icon = ImageHandeler.loadGloab(eel + name);
			if(icon == null)
				return;
			ImageElement element = new ImageElement();

			element.name = name;
			element.location = file;
			element.imageIcon = icon;

			if (first)
				list.addImage(0, element.imageIcon, false);
			else
				list.addImage(element.imageIcon, false);
			list.repaint();
		}
	}

	public static void save(RCObject parent, String key) {
		RCObject out = new RCObject(key);
		int i = 0;
		for (ImageElement element : imageElements) {
			System.out.println(element.getPath());
			if (element.getPath() == null || element.name == null)
				continue;
			out.addString("location " + i, element.getPath());
			out.addString("name " + i, element.name);
			i++;
		}
		parent.addSubObject(out);
	}

	public static void load(RCObject parent, String key) {
		String path;
		int i = 0;
		RCObject in = parent.getSubObject(key);
		
		while ((path = in.getString("location " + i)) != null) {
			addExtract(new File(path), in.getString("name " + i), false);
			i++;
		}
	}

	public boolean equals(Object obj) {
		if (obj instanceof ImageElement)
			return obj == this;
		else if (obj instanceof String)
			return ((String) obj).equals(location.getPath());
		else if (obj instanceof Image)
			return ((Image) obj).equals(image);
		return false;
	}

}