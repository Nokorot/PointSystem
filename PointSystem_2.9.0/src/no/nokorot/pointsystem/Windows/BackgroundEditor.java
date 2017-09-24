package no.nokorot.pointsystem.Windows;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JColorChooser;
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
import util.swing.gride.BoxGrid;
import util.swing.gride.XStrip;

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
				
				setVisible(true);
				BoxGrid bg = getGrid(new double[] { 2, 1.3 },
						new double[] { TOPP_BAR_HEIGHT, getPlaneHeight() - TOPP_BAR_HEIGHT });
				// bg.setInsets(3);
				bg.setExpX(new double[] { 1, 0 });
				bg.setExpY(new double[] { 0, 1 });

				bg.getBox(0, 1).setComponent(img = new Label(this));

				BoxGrid topGrid = bg.getBox(0, 0).getInsideGrid(new double[] { 1, 1, 1, 2 },
						new double[] { 1, 2 });
				topGrid.setExp(new double[]{0, 0, 0, 1}, new double[]{0, 1});
				// topGrid.setAllInsets(0, 3, 0, 3);

				panel2.labelSets.setTColor(Color.WHITE);
				panel2.labelSets.setFontSize(15);

				topGrid.setComponent(new Label(this, "Bacground"), 0, 0);
				bacground = new NBButton(this);
				bacground.setIcon(PSData.backgroundImage, PSData.backgroundScaleType);
				Box backBox = topGrid.getBox(0, 1);
				backBox.setRightInset(0);
				backBox.setComponent(bacground);

				topGrid.setComponent(new Label(this, "Logo"), 1, 0);
				logo = new NBButton(this);
				logo.setIcon(PSData.logo, PSData.logoScaleType);
				Box logoBox = topGrid.getBox(1, 1);
				logoBox.setRightInset(0);
				logoBox.setComponent(logo);

				topGrid.setComponent(new Label(this, "Scale Type"), 2, 0);
				scale = new PopDownTextField(this);
				for (ScaleType type : ScaleType.values())
					scale.addElements(type.name());
				Font f = scale.getFont();
				scale.setFont(new Font(f.getFontName(), Font.BOLD, 13));
				scale.addActionListener((ActionEvent e) -> {
					setScale(ScaleType.valueOf(scale.getSelectedItem()));
				});
				topGrid.setComponent(scale, 2, 1);

				BoxGrid rTopGrid = bg.getBox(1, 0).getInsideGrid(2, 1);

				typeSwitch = new SwitchButton(this, SwitchButton.STRING, "Images", "Colors");
				typeSwitch.setActiveIndex(1);
				rTopGrid.setComponent(typeSwitch, 1);

				brows = new NBButton(this, "Browse");
				findColor = new NBButton(this, "Find");
				pickColor = new NBButton(this, "Pick");
				
				colorType = new XStrip();
				colorType.append(findColor).setInsets(0, 0, 0, 3);
				colorType.append(pickColor).setInsets(0, 3, 0, -1);

				addBox = rTopGrid.getBox(0);
				addBox.setComponent(brows);

				imageList = new ImageList(this, "imageList");
				imageList.setBackground(Color.LIGHT_GRAY);
				imageList.addListSelectionListener((ListSelectionEvent e) -> {
					if (e.getValueIsAdjusting()) {
						colorList.clearSelection();
					}
				});

				colorList = new ImageList(this, "colorList");
				colorList.setBackground(Color.LIGHT_GRAY);
				colorList.addListSelectionListener((ListSelectionEvent e) -> {
					if (e.getValueIsAdjusting()) {
						int i = colorList.getSelectedIndex();
						setImage(colorList.getImage(i), null);
						imageList.clearSelection();
					}
				});

				listBox = bg.getBox(1, 1);
				listBox.setComponent(imageList);

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
//					new java.awt.FileDialog((java.awt.Frame) null).setVisible(true);
					
					ImagePreviewFileChooser fileChooser = new ImagePreviewFileChooser(lastLocation);
					fileChooser.setPreferredSize(new Dimension(800, 500));
					fileChooser.setPreviewPanelWidth(500);
					fileChooser.showOpenDialog(this);
					lastLocation = fileChooser.getCurrentDirectory().getPath();
					File file = fileChooser.getSelectedFile();
					if (file != null && file.exists()){
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
		ImageElement.addImage(PSData.logo, true, false);

		img.setImageIcon(PSData.backgroundImage);
		setScale(PSData.backgroundScaleType);
		load();
	}
	

	public static void Open(boolean viwe) {
		if (window == null)
			create();
		else
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
		if (in == null)
			return;
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
			System.out.println(colorList.getElementCount());
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
}
