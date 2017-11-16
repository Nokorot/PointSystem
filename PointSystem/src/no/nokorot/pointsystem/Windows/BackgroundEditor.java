package no.nokorot.pointsystem.Windows;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JColorChooser;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.event.ListSelectionEvent;

import com.thecherno.raincloud.serialization.RCObject;

import engineTest.ImagePreviewFileChooser;
import no.nokorot.pointsystem.PSData;
import no.nokorot.pointsystem.PointSystem;
import no.nokorot.pointsystem.Element.ImageElement;
import no.nokorot.pointsystem.utils.ColorPicker;
import util.Window;
import util.handelers.ImageHandeler.ScaleType;
import util.swing.ImageList;
import util.swing.Label;
import util.swing.NBButton;
import util.swing.PopDownTextField;
import util.swing.SwitchButton;
import util.swing.gride.Box;
import util.swing.gride.DXStrip;
import util.swing.gride.XStrip;
import util.swing.gride.YStrip;

public class BackgroundEditor {

	private static final int MAX_WIDTH = 1000;
	private static final int MAX_HEIGHT = 600;
	private static final int MIN_WIDTH = 822;
	private static final int MIN_HEIGHT = 410;

	private static final int TOPP_BAR_HEIGHT = 80;

	private static Window window;

	private static NBButton bacground, logo;
	private static PopDownTextField scale;
	private static Label img;

	private static final int BACKGROUND = 1;
	private static final int LOGO = 2;
	private static int selected = BACKGROUND;

	private static SwitchButton typeSwitch;
	private static Box listBox, addBox;

	// private static List<String> imagelocations = new ArrayList<String>();
	private static List<Color> colors = new ArrayList<Color>();

	private static ImageList imageList;
	private static ImageList colorList;
	private static XStrip colorType;
	private static NBButton brows, findColor, pickColor;

	private static ScaleType scaleType = ScaleType.TILLPASS;
	private static String lastLocation = System.getProperty("user.home");

	private BackgroundEditor() {
	}

	public static void create() {
		window = new Window("Background Editor", MIN_WIDTH, MIN_HEIGHT) {
			private static final long serialVersionUID = 1L;

			public void Init() {
				window = this;

				setMaximumSize(new Dimension(MAX_WIDTH, MAX_HEIGHT));
				setMinimumSize(new Dimension(MIN_WIDTH, MIN_HEIGHT));

				panel2.textfeldSets.setFontSize(10);
				panel2.labelSets.setTColor(Color.WHITE);
				panel2.labelSets.setFontSize(15);

				XStrip root = new XStrip();
//				DXStrip root = new DXStrip(this);
				
				XStrip x = new XStrip();
				YStrip y = new YStrip();
				
				y.append(new Label(this, "Bacground"));
				bacground = new NBButton(this);
				bacground.setIcon(PSData.backgroundImage, PSData.backgroundScaleType);
				y.append(bacground, 3);
				x.append(y, 1, 0);
				
				y = new YStrip();
				y.append(new Label(this, "Logo"));
				logo = new NBButton(this);
				logo.setIcon(PSData.logo, PSData.logoScaleType);
				y.append(logo, 3);
				x.append(y, 1, 0);

				y = new YStrip();
				y.append(new Label(this, "Scale Type"));
				scale = new PopDownTextField(this);
				for (ScaleType type : ScaleType.values())
					scale.addElements(type.name());
				Font f = scale.getFont();
				scale.setFont(new Font(f.getFontName(), Font.BOLD, 13));
				scale.addActionListener((ActionEvent e) -> {
					setScale(ScaleType.valueOf(scale.getSelectedItem()));
				});
				y.append(scale, 3);
				x.append(y, 1, 0);
				x.append(new YStrip(), 2);
				
				y = new YStrip();
				y.append(x, 1, 0);
				
				img = new Label(this);
				img.setOpaque(true);
				img.setDropTarget(new DropTarget(img, new ImageDropTargetListener()));
				y.append(img, 4);
				
				root.append(y, 5);
				
				imageList = new ImageList(this, "imageList");
				imageList.setBackground(Color.LIGHT_GRAY);
				imageList.setDropTarget(new DropTarget(imageList, new ImageDropTargetListener()));
				imageList.addMouseListener(new PopClickListener());

				imageList.addListSelectionListener((ListSelectionEvent e) -> {
					if (e.getValueIsAdjusting()) {
//						colorList.clearSelection();
					}
				});
				root.append(imageList, 2);
				
				this.getFrameBox().setBoxObject(root);

				panel2.setBackground(Color.DARK_GRAY);
			}

			public void ButtonAction(NBButton b) {
				if (b == bacground) {
					selectBackground();
				}

				if (b == logo) {
					selectLogo();
				}

				if (b == typeSwitch) {
					if (typeSwitch.getActiveIndex() == 1) {
						listBox.setComponent(imageList);
						addBox.setComponent(brows);
					}
					if (typeSwitch.getActiveIndex() == 0) {
						listBox.setComponent(colorList);
						addBox.setBoxObject(colorType);
					}
				}

				if (b == brows) {
					// new java.awt.FileDialog((java.awt.Frame)
					// null).setVisible(true);

					ImagePreviewFileChooser fileChooser = new ImagePreviewFileChooser(lastLocation);
					fileChooser.setPreferredSize(new Dimension(800, 500));
					fileChooser.setPreviewPanelWidth(500);
					fileChooser.showOpenDialog(this);
					lastLocation = fileChooser.getCurrentDirectory().getPath();
					File file = fileChooser.getSelectedFile();
					if (file != null && file.exists()) {
						ImageElement.addImage(file, true, true);
						lastLocation = file.getParent();
					}
				}

				if (b == findColor) {
					Color c = JColorChooser.showDialog(null, "Find a color", Color.white);
					if (c == null)
						return;
					addColor(c);
				}

				if (b == pickColor) {

					Color c = new ColorPicker(window).pickColor();
					if (c == null)
						return;
					addColor(c);
				}
			}

			public void onCloseing() {
				MainMenu.backgornd.actionPerformed(null);
				save();
			}
		};

		ImageElement.init(imageList, (ImageElement e) -> setImage(e.getImage(), e.getPath()));
		

		img.setImageIcon(PSData.backgroundImage);
		setScale(PSData.backgroundScaleType);
		load();
	}

	public static void Open(boolean viwe) {
		if (window == null)
			create();
		window.setVisible(viwe);
	}

	private static void selectBackground() {
		selected = BACKGROUND;
		BackgroundEditor.setScale(PSData.backgroundScaleType);
		img.setImageIcon(PSData.backgroundImage);
	}

	private static void selectLogo() {
		selected = LOGO;
		BackgroundEditor.setScale(PSData.logoScaleType);
		img.setImageIcon(PSData.logo);
	}

	private static void save() {
		RCObject out = new RCObject("Background Chooser");
		int i = 0;
		for (Color color : colors)
			out.addInteger("color " + i++, color.getRGB());
		ImageElement.save(out, "ImageElements");

		PointSystem.database.addObject(out);
	}

	private static void load() {
		RCObject in = PointSystem.database.getObject("Background Chooser");
		if (in == null){
			ImageElement.addImage(PSData.logo, true, false);
			return;
		}
		new Thread(() -> {
			int color;
			int i = 0;
			while ((color = in.getInteger("color " + i++)) != -1)
				addColor(new Color(color));
			ImageElement.load(in, "ImageElements");
		}).start();
	}

	// **** Color Setup *****
	@SuppressWarnings("unused")
	private static void setColor(Color color) {
		if (color == null)
			return;
		if (!colors.contains(color))
			addColor(color);
		BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
		image.setRGB(0, 0, color.getRGB());
		setImage(image, null);
	}

	private static void addColor(Color color) {
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

	// **** Image Setup *****
	private static void setScale(ScaleType s) {
		scale.setSelectedItem(s.name());

		if (selected == BACKGROUND) {
			bacground.setIconScaling(s);
			PSData.backgroundScaleType = s;
		}
		if (selected == LOGO) {
			logo.setIconScaling(s);
			PSData.logoScaleType = s;
		}
		scaleType = s;

		img.setIconScaling(scaleType);
		MainMenu.updateLiveWindow();
	}

	public static void setImage(BufferedImage image, String path) {
		if (selected == BACKGROUND) {
			bacground.setImageIcon(image);
			if (path != null)
				PSData.backgroundLocation = path;
			PSData.backgroundImage = image;
		}

		if (selected == LOGO) {
			logo.setImageIcon(image);
			if (path != null)
				PSData.logoLocation = path;
			PSData.logo = image;
		}

		img.setImageIcon(image);
		MainMenu.updateLiveWindow();
	}
	
	static class PopUpDemo extends JPopupMenu {
	    JMenuItem anItem;
	    public PopUpDemo(int index){
	        anItem = new JMenuItem("Remove?");
	        anItem.addActionListener((ActionEvent e) -> {
	        	ImageElement.removeImage(index);
	        });
	        add(anItem);
	    }
	}
	
	static class PopClickListener extends MouseAdapter {
	    public void mousePressed(MouseEvent e){
	        if (e.isPopupTrigger())
	            doPop(e);
	    }

	    public void mouseReleased(MouseEvent e){
	        if (e.isPopupTrigger())
	            doPop(e);
	    }

	    private void doPop(MouseEvent e){
	    	int i = imageList.locationToIndex(e.getPoint());
	    	imageList.setSelectedIndex(i);
	        PopUpDemo menu = new PopUpDemo(i);
	        menu.show(e.getComponent(), e.getX(), e.getY());
	    }
	}

	private static class ImageDropTargetListener extends DropTargetAdapter {
		public void drop(DropTargetDropEvent e) {

			try {
				String data;
				Transferable t = e.getTransferable();

				if (e.isDataFlavorSupported(DataFlavor.getTextPlainUnicodeFlavor())) {
					e.acceptDrop(e.getDropAction());

					StringBuffer sb = new StringBuffer();
					InputStream is = (InputStream) t.getTransferData(DataFlavor.getTextPlainUnicodeFlavor());
					int i;
					while ((i = is.read()) != -1)
						if (i != 0)
							sb.append((char) i);

					data = new String(sb);

					e.dropComplete(true);
				} else if (e.isDataFlavorSupported(DataFlavor.stringFlavor)) {
					e.acceptDrop(e.getDropAction());

					data = (String) t.getTransferData(DataFlavor.stringFlavor);

					e.dropComplete(true);
				} else {
					e.rejectDrop();
					return;
				}
				
//				String dat = data; int i;
//				while ((i = dat.indexOf('%')) != -1){
//					String s = dat.substring(i+1, i +3);
//					if (s.equals("C3"))
//						s += dat.substring(i+4, i + 6);
//					System.out.println("" + (char) Integer.parseInt(s, 16));
//					dat = dat.replaceFirst("%" + s, "" + (char) Integer.parseInt(s, 16));
//					i++;
//				}
				

				
				System.out.println(data);

				new Thread(() -> {
//					String d = data.replace("%20", " ");
					@SuppressWarnings("deprecation")
					String d = URLDecoder.decode(data);
					System.out.println(d);
					
					for (String s : d.split("\n")) {
						if (s.startsWith("file://")) {
							File f = new File(s.substring("file://".length(), s.length() - 1));
							ImageElement.addImage(f, true, false);
						}
					}
				}).start();

			} catch (IOException e2) {
			} catch (UnsupportedFlavorException e2) {
			}

		}
	}

}
