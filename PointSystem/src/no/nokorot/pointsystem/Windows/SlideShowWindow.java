package no.nokorot.pointsystem.Windows;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;

import com.sun.javafx.webkit.ThemeClientImpl;

import apachePOI.BufferedImageSlides;
import no.nokorot.pointsystem.PSData;
import no.nokorot.pointsystem.PointSystem;
import no.nokorot.pointsystem.Element.ImageElement;
import util.Window;
import util.handelers.ImageHandeler;
import util.handelers.ImageHandeler.ScaleType;
import util.swing.Label;
import util.swing.NBButton;
import util.swing.NBTextField;
import util.swing.PopDownTextField;
import util.swing.SwitchButton;
import util.swing.gride.BoxObject;
import util.swing.gride.XStrip;
import util.swing.gride.YStrip;

@SuppressWarnings("serial")
public class SlideShowWindow {
	
	private static final int MAX_WIDTH = 1000;
	private static final int MAX_HEIGHT = 600;
	private static final int MIN_WIDTH = 822;
	private static final int MIN_HEIGHT = 410;

	private static Window window;

	private static NBButton enable, previus, next;
	private static PopDownTextField scale;
	private static Label img;

	private static BufferedImageSlides bis;

	private static int selectedPage = -1;
	
	private static PageList pageList;

	private SlideShowWindow() {
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
				
				XStrip x;
				YStrip y;
				
				y = new YStrip();
				x = new XStrip();
				Label l = new Label(this, "PowerPoint files here.");
				l.setFontSize(14);
				x.append(l, 6);
				Label infoB = new Label(this);
				infoB.setIcon(MainMenu.icons[2], ScaleType.TILLPASS);
				
//				infoB.setBackground(null);
				x.append(infoB).setInsets(0);
				y.append(x, 1, 0);
				
				pageList = new PageList(this, "imageList");
				pageList.setBackground(Color.LIGHT_GRAY);
				pageList.setDropTarget(new DropTarget(pageList, new ImageDropTargetListener()));
//				pageList.addMouseListener(new PopClickListener());

				pageList.addListSelectionListener((ListSelectionEvent e) -> {
					if (e.getValueIsAdjusting()) {
//						colorList.clearSelection();
					}
				});
				y.append(pageList, 15);
				root.append(y, 2);
				
				
				x = new XStrip();
				y = new YStrip();
//				y.append(new Label(this, "Enable"));
				enable = new NBButton(this, "Enable"){
					protected void onAction() {
						if (selectedPage != -1){
							PSData.SlideShowEnaled = !PSData.SlideShowEnaled;
							if (OnlineWindow.isOnline)
								OnlineWindow.enableSlideMode(PSData.SlideShowEnaled);
							enable.setBackground(PSData.SlideShowEnaled ? Color.cyan : Color.LIGHT_GRAY);
							LiveWindow.render();
						}
					}
				};
//				bacground.setIcon(PSData.backgroundImage, PSData.backgroundScaleType);
				y.append(enable, 3);
				x.append(y, 1, 0);
				
				y = new YStrip();
//				y.append(new Label(this, "Previus"));
				previus = new NBButton(this, "Previus"){
					protected void onAction() {
						setSelectedPage(selectedPage - 1);
					}
				};
//				bacground.setIcon(PSData.backgroundImage, PSData.backgroundScaleType);
				y.append(previus, 3);
				x.append(y, 1, 0);
				
				y = new YStrip();
//				y.append(new Label(this, "Next"));
				next = new NBButton(this, "Next"){
					protected void onAction() {
						setSelectedPage(selectedPage + 1);
					}
				};
//				logo.setIcon(PSData.logo, PSData.logoScaleType);
				y.append(next, 3);
				x.append(y, 1, 0);

				y = new YStrip();
				y.append(new Label(this, "Scale Type"));
				scale = new PopDownTextField(this);
				for (ScaleType type : ScaleType.values())
					scale.addElements(type.name());
				Font f = scale.getFont();
				scale.setFont(new Font(f.getFontName(), Font.BOLD, 13));
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
				
				this.getFrameBox().setBoxObject(root);

				panel2.setBackground(Color.DARK_GRAY);
			}

			public void onCloseing() {
				MainMenu.slideShow.actionPerformed(null);
//				save();
			}
		};

//		ImageElement.init(pageList, (ImageElement e) -> setImage(e.getImage(), e.getPath()));
		

//		img.setImageIcon(PSData.backgroundImage);
//		setScale(PSData.backgroundScaleType);
//		load();
	}

	public static void Open(boolean viwe) {
		if (window == null)
			create();
		window.setVisible(viwe);
	}

	public static double lats_pageChange = 0; 
	public static void setSelectedPage(int index) {
		if (bis == null)
			return;
		if (index == selectedPage || index < 0 || index >= bis.numberOfPages())
			return;
		PSData.slidePage = bis.getBufferedImage(index);
		pageList.setSelectedIndex(index);
		img.setIcon(bis.getBufferedImage(index), ScaleType.TILLPASS);
		selectedPage = index;
		LiveWindow.render();
	}
	
	
	/*private static final String url = "https://pointsystemo.herokuapp.com/";
	private static boolean connected = false;
	private static boolean running = false;
	private static void ConectToServer() {
		if (connected)
			return;
		connected = true;
		new Thread(() -> {
			running = true;
			while (running) {
				
			}
			connected = false;
		}).run();
	}
	
	private static void DisconectFromServer() {
		running = false;
	}
	
	public static String psRead(String requestType, Object... data){
		String[] slist = new String[data.length];
		for (int i = 0; i < data.length; i++) 
			slist[i] = data[i].toString();
		return read(url, "pointsystem/"+requestType+"/" + String.join("|", slist));
	}
	
	public static String read(String url, String extention){
		URL oracle;
		try {
			oracle = new URL(url + extention);
			URLConnection yc = oracle.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
			StringBuilder builder = new StringBuilder();
			String line;
			while ((line = in.readLine()) != null)
				builder.append(line);
			in.close();
			return builder.toString();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
		}
		return null;
	}*/
	
/*	private static void save() {
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
	}*/

	static class PageList extends JList<ImageIcon> implements BoxObject {
		private static final long serialVersionUID = 1L;

		JScrollPane sp = new JScrollPane();
		
		String code = "";
		
		private DefaultListModel<ImageIcon> listModel;
		
		
		private List<BufferedImage> images = new ArrayList<BufferedImage>();
		private Dimension imageSize = new Dimension(100, 100);

		
		public PageList(Window window, String code) {
			setModel(listModel = new DefaultListModel<ImageIcon>());
			
			this.code = code;
			
//			setFixedCellHeight(60);
//			setFixedCellWidth(60);
			
			DefaultListCellRenderer renderer = (DefaultListCellRenderer) getCellRenderer();
			renderer.setHorizontalAlignment(JLabel.CENTER);
			
			addListSelectionListener((ListSelectionEvent e) -> {
				if (e.getValueIsAdjusting())
					setSelectedPage(getSelectedIndex());
			});
			
			
			setVisibleRowCount(-1);
			setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			setLayoutOrientation( JList.VERTICAL);
			
			sp.setViewportView(this);
			window.panel2.add(sp);
		}

		/*public void removeImage(int index){
			listModel.removeElementAt(index);
		}
		
		public void updateImage(int index, BufferedImage image, boolean scale) {
			try {
				if(image == null)
					return;
				if(scale)
					image = ImageHandeler.getScaledImage(image, imageSize.width, imageSize.height);
				listModel.getElementAt(index).setImage(image);
				this.repaint();
			} catch (IOException e) {
			}
		}*/

		public void addImage(BufferedImage image, boolean scale) {
			try {
				if(image == null)
					return;
				if(scale)
					image = ImageHandeler.getScaledImage(image, imageSize.width, imageSize.height, ScaleType.TILLPASS);
				listModel.addElement(new ImageIcon(image));
				images.add(image);
			} catch (IOException e) {
			}
		}

		public void addImage(int index, BufferedImage image, boolean scale) {
			try {
				if(scale)
					image = ImageHandeler.getScaledImage(image, imageSize.width, imageSize.height);
				if(image == null)
					return;
				listModel.add(index, new ImageIcon(image));
				images.add(image);
			} catch (IOException e) {
			}
		}
		
		public void addImages(int index, BufferedImage[] im, boolean scale) {
			for (BufferedImage image1 : im) {
				addImage(index, image1, scale);
			}
		}
		
		public BufferedImage getImage(int i) {
			return images.get(i);
		}
		/*
		public void addAllFromAFile(int index, String path) throws IOException {
			File folder = new File(path);
			File[] listOfFiles = folder.listFiles();

			for (int i = 0; i < listOfFiles.length; i++) {
				System.out.println("check path" + listOfFiles[i]);
				addImage(index, ImageIO.read(listOfFiles[i]), true);
			}
		}*/
		
		public int getElementCount(){
			return listModel.getSize();
		}
		
		public void setVisible(boolean aFlag) {
			sp.setVisible(aFlag);
//			super.setVisible(aFlag);
		}
		
		public void setBounds(Rectangle r) {
			sp.setBounds(r);
		}

		@Override
		public void setPane(JPanel pane) {
			pane.add(this);
		}


	}
	/*
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
	}*/

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
				
				img.setIcon(ImageHandeler.createImage_Cercel(200, 200, Color.RED, null), ScaleType.TILLPASS);
				new Thread(() -> {
					@SuppressWarnings("deprecation")
					String d = URLDecoder.decode(data);
					
					for (String s : d.split("\n")) {
						if (s.startsWith("file://")) {
							File f = new File(s.substring("file://".length(), s.length() - 1));
							
							bis = new BufferedImageSlides(f.getAbsolutePath());
							setSelectedPage(0);
							
							for (int i = 0; i < bis.numberOfPages(); i++) {
								pageList.addImage(bis.getBufferedImage(i), true);
							}
							
							if (OnlineWindow.SlideModeEnabled)
								syncSlides();
							
							ImageElement.addImage(f, true, false);
						}
					}
				}).start();

			} catch (IOException e2) {
			} catch (UnsupportedFlavorException e2) {
			}

		}
	}

	public static void syncSlides() {
		if (bis == null)
			return;
//		int i = bis.hashCode();
//		OnlineWindow.psRead("DataPacket", OnlineWindow.psCode, ""+i, ""+bis.numberOfPages());
		
		OnlineWindow.psRead("info", OnlineWindow.psCode, "slidesData", bis.numberOfPages(), selectedPage);
		
		// TODO: Send Data !!!!!
		
	}
	
}
