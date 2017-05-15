package util.swing;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.ScrollPane;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

import util.Window;
import util.adds.UpdateAdd;
import util.handelers.ImageHandeler;

public class ImageList extends JList<ImageIcon> {
	private static final long serialVersionUID = 1L;

	private List<BufferedImage> images = new ArrayList<BufferedImage>();
	
	private DefaultListModel<ImageIcon> listModel;
	private JScrollPane sp;

	public ImageList(Window window, Rectangle rec) {
		setModel(listModel = new DefaultListModel<ImageIcon>());
		setBounds(rec);
		sp = new JScrollPane(this);
//		sp.set
		window.panel.add(this);
		
		addAncestorListener(new AncestorListener() {
			public void ancestorRemoved(AncestorEvent event) {
				System.out.println(event);
			}
			public void ancestorMoved(AncestorEvent event) {
//				System.out.println(event);
				
			}
			public void ancestorAdded(AncestorEvent event) {
				System.out.println(event);
				
			}
		});
		
		window.addUpdate(new UpdateAdd() {
			public void mUpdate1() {
			}
			
			private Dimension size = new Dimension();
			public void mUpdate() {
				if(getWidth() != size.width){
					size = getSize();
					int width = size.width;
					listModel.removeAllElements();
					int count = 0;
					for (BufferedImage image1 : images) {
						try {
							listModel.add(count++, new ImageIcon(ImageHandeler.getScaledImage(image1, width, width)));
						} catch (IOException e) {
							e.printStackTrace();
						};
					}
				}
			}
		});
	}

	public ImageList(Window window) {
		this(window, new Rectangle());
	}

	public void addImage(int index, BufferedImage image) {
		listModel.add(index, new ImageIcon(image));
		images.add(image);
	}
	
	public void addImages(int i, BufferedImage[] im) {
		for (BufferedImage image1 : im) {
			addImage(0, image1);
		}
	}

	public void addAllFromAFile(int index, String path) throws IOException {
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
			System.out.println("check path" + listOfFiles[i]);
			addImage(index, ImageHandeler.getScaledImage(ImageIO.read(listOfFiles[i]), 200, 200));
		}
	}
	
}
