package no.nokorot.pointsystem.Windows;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.event.ListSelectionEvent;
import javax.swing.plaf.basic.BasicTabbedPaneUI;

import com.thecherno.raincloud.serialization.RCObject;

import no.nokorot.pointsystem.PSData;
import no.nokorot.pointsystem.PointSystem;
import no.nokorot.pointsystem.Element.Team;
import no.nokorot.pointsystem.utils.FontObject;
import util.Window;
import util.adds.UpdateAdd;
import util.handelers.ImageHandeler.ScaleType;
import util.swing.ImageList;
import util.swing.Label;
import util.swing.NBButton;
import util.swing.NBSlider;
import util.swing.NBTabbedPane;
import util.swing.PopDownTextField;
import util.swing.SwitchButton;
import util.swing.NBTextField;
import util.swing.gride.BoxGrid;
import util.swing.gride.BoxObject;
import util.swing.gride.XStrip;
import util.swing.gride.YStrip;

public class FontEditor2 {

	private static Window window;

	private static FontObject currentTheme;

	private static PopDownTextField Fontname;
	private static SwitchButton Bold, Italic;

	private static NBButton MColor, BColor;
	private static ColorSelector mainColorSelector;
	private static ColorSelector borderColorSelector;

	private static NBSlider BThiknes;
//	private static TextField BThiknes;
	
	private static boolean[] namesA = new boolean[9];
	private static boolean[] pointsA = new boolean[9];

	private static PreviewPanel previwe;
	private static FontPanel fontPanel;

	private static void create() {
		currentTheme = new FontObject("Def");

		window = new Window("Font Editor", 700, 500) {
			private static final long serialVersionUID = 1L;

			protected void onOpened() {
				setSize(600, 400);
			}

			public void Init() {
				setMinimumSize(new Dimension(700, 450));

				setResizable(true);
				panel2.labelSets.setFontSize(12);

				XStrip root = new XStrip();
				YStrip y = new YStrip();

				NBTabbedPane tabbedPane = new NBTabbedPane(this);
				tabbedPane.setPaneBackground(Color.DARK_GRAY);
				tabbedPane.setUI(new BasicTabbedPaneUI() {
					protected void installDefaults() {
						super.installDefaults();
						highlight = Color.pink;
						lightHighlight = Color.green;
						shadow = Color.red;
						darkShadow = Color.cyan;
						focus = Color.yellow;
					}
				});


				tabbedPane.addTab("Home", HomeTab(this));
				tabbedPane.addTab("Teams", TeamsTab(this));
				
				y.append(tabbedPane, 1, 0.3);
				y.getBox(-1).setInsets(5, 5, 5, 5);

//				Label line = new Label(this);
//				line.setOpaque(true);
//				line.setBackground(Color.LIGHT_GRAY);
//				y.append(line, .1);

//				y.append(previwe = new PreviewPanel(this, currentTheme), 2.5);
				y.getBox(-1).setInsets(10, 20, 0, 20);

				root.append(y);

				root.append(fontPanel = new FontPanel(this) {
					public void onAction() {
						setTheme(this.getSelectedFont());
						fontPanel.setSaveState(true);
					}
				}, .5);

				this.getFrameBox().setBoxObject(root);
			}

			public void ButtonAction(NBButton b) {
				if (b.code != null) {
					switch (b.code) {
					case "SaveFont":
						fontPanel.saveFont(currentTheme);
						fontPanel.setSaveState(true);
						break;
					case "ResetFont":
						currentTheme.copy(fontPanel.getSelectedFont());
						fontPanel.setSaveState(true);
						break;
					case "NewFont":
						if (fontPanel.getSaveState())
							return;
						fontPanel.addFont(currentTheme);
						fontPanel.setSaveState(true);
						break;
					}
				}
			}

			protected void onCloseing() {
				MainMenu.font.actionPerformed(null);
			}
		};
		// MainMenu.docker.registerDockee(window, Docker.EAST_DOCKED);

		// setTheme(fontPanel.getSelectedFont());
		load();
		if (Fontname.getElementCount() > 0)
			Fontname.setSelectedIndex(-1);
		fontPanel.select(-1);
		fontPanel.setSaveState(true);
	}

	private static BoxObject HomeTab(Window window){
		YStrip root = new YStrip();
		
		
		XStrip home = new XStrip();

		YStrip fontS = new YStrip();
		Fontname = new PopDownTextField(window) {
			private static final long serialVersionUID = 1L;

			protected void onAction() {
				currentTheme.setFontname(this.getSelectedItem());
				fontPanel.setSaveState(false);
				previwe.update();
			}
		};
		for (Font f : PSData.environment.getAllFonts())
			Fontname.addItem(f.getFontName());
		fontS.append(Fontname);


		XStrip stile = new XStrip();
		Bold = new SwitchButton(window, SwitchButton.COLOR) {
			private static final long serialVersionUID = 1L;

			protected void onAction() {
				currentTheme.setBold(!currentTheme.isBold());
				fontPanel.setSaveState(false);
				previwe.update();
			}
		};
		Bold.setText("Bold");
		stile.append(Bold);

		Italic = new SwitchButton(window, SwitchButton.COLOR) {
			private static final long serialVersionUID = 1L;

			protected void onAction() {
				currentTheme.setItalic(!currentTheme.isItalic());
				fontPanel.setSaveState(false);
				previwe.update();
			}
		};
		Italic.setText("Italic");
		stile.append(Italic);
		fontS.append(stile);

		home.append(fontS);

		YStrip borderS = new YStrip();

		XStrip borderX = new XStrip();
		borderX.append(new Label(window, "Main color"));
		borderX.append(new Label(window, "Border"));
		borderS.append(borderX, .7);

		borderX = new XStrip();
		borderX.append(MColor = new NBButton(window, "Main") {
			private static final long serialVersionUID = 1L;

			protected void onAction() {
				if (mainColorSelector.isOpen())
					mainColorSelector.Close();
				else
					mainColorSelector.Open();
			}
		});
		mainColorSelector = new ColorSelector("Main Color Selector") {
			protected void onColorChange(Color color) {
				MColor.setBackground(color);
				currentTheme.setColor(color);
				fontPanel.setSaveState(false);
				previwe.update();
			}
		};

		borderX.append(BColor = new NBButton(window, "Border") {
			private static final long serialVersionUID = 1L;

			protected void onAction() {
				if (borderColorSelector.isOpen())
					borderColorSelector.Close();
				else
					borderColorSelector.Open();
			}
		});
		borderColorSelector = new ColorSelector("Main Color Selector") {
			protected void onColorChange(Color color) {
				BColor.setBackground(color);
				currentTheme.setBorderColor(color);
				fontPanel.setSaveState(false);
				previwe.update();
			}
		};
		borderS.append(borderX);

		// borderS.append(new Slider());
		borderS.append(new Label(window, "Border thiknes"), .7);
		borderS.append(BThiknes = new NBSlider(window, 0, 1, 0.2f) {
			private static final long serialVersionUID = 1L;

			protected void onAction() {
				try {
					float thiknes = this.getFloatValue();//Float.parseFloat(this.getText());
					currentTheme.setBorderWidth(thiknes);
					fontPanel.setSaveState(false);
					previwe.update();
				} catch (Exception e) {
				}
			}
		});
		BThiknes.setBackground(Color.DARK_GRAY);

		home.append(borderS);
		home.append((BoxObject) null, .1);
		
//		return home;
		
		root.append(home);
		root.append(previwe = new PreviewPanel(window, currentTheme), 2.5);
		
		return root;
	}
	
	private static BoxObject TeamsTab(Window window){
		YStrip root = new YStrip();

		NBButton[] names = new NBButton[9];
		NBButton[] points = new NBButton[9];
		
		
//		TextField teams = new TextField(window);
//		root.append(teams);

//		XStrip y = new XStrip();
//		y.append(new NBButton(window, "Names"){
//			boolean active;
//			protected void onAction() {
//				active = !active;
//				this.setBackground(active ? Color.CYAN : Color.LIGHT_GRAY);	
//			}
//		});
//		y.append(new NBButton(window, "Teams"){
//			boolean active;
//			protected void onAction() {
//				active = !active;
//				this.setBackground(active ? Color.CYAN : Color.LIGHT_GRAY);	
//			}
//		});
		
		XStrip y = new XStrip();
		y.append(new NBButton(window, "Names"){
			protected void onAction() {
				for (int i = 0; i < 9; i++){
					namesA[i] = !namesA[i];
					Team.TEAMS[i].fontName.copy(currentTheme);
//					names[i].setBackground(namesA[i] ? Color.cyan : Color.lightGray);
				}
				MainMenu.updateLiveWindow();
			}
		});
		y.append(new NBButton(window, "Points"){
			protected void onAction() {
				for (int i = 0; i < 9; i++){
					pointsA[i] = !pointsA[i];
					Team.TEAMS[i].fontPoints.copy(currentTheme);
//					points[i].setBackground(pointsA[i] ? Color.cyan : Color.lightGray);
				}
				MainMenu.updateLiveWindow();
			}
		});
//		y.append(new NBButton(window, "All"){
//			protected void onAction() {
//				for (int i = 0; i < 9; i++){
//					namesA[i] = !namesA[i];
//					names[i].setBackground(namesA[i] ? Color.cyan : Color.lightGray);
//					pointsA[i] = !pointsA[i];
//					points[i].setBackground(pointsA[i] ? Color.cyan : Color.lightGray);
//				}
//			}
//		});
		root.append(y);
		

//		y = new XStrip();
//		y.append(new NBButton(window, "Clear"){
//			protected void onAction() {
//				for (int i = 0; i < 9; i++)
//					activateTeam(i, true);
//			}
//		});
//		y.append(new NBButton(window, "All"){
//			protected void onAction() {
//				for (int i = 0; i < 9; i++)
//					activateTeam(i, false);
//			}
//		});
//		root.append(y);
		
		YStrip teams = new YStrip();
		for (int i = 0; i < 3; i++){
			XStrip x = new XStrip();
			for (int j = 0; j < 3; j++){
				int index = j+i*3;
				YStrip t = new YStrip();
				t.append(names[index] = new NBButton(window, "N" + index){
					protected void onAction() {
						namesA[index] = !namesA[index];
						Team.TEAMS[index].fontName.copy(currentTheme);
						MainMenu.updateLiveWindow();
//						this.setBackground(namesA[index] ? Color.cyan : Color.lightGray);
					}
				});
				t.append(points[index] = new NBButton(window, "P" + index){
					protected void onAction() {
						pointsA[index] = !pointsA[index];
						Team.TEAMS[index].fontPoints.copy(currentTheme);
						MainMenu.updateLiveWindow();
//						this.setBackground(pointsA[index] ? Color.cyan : Color.lightGray);
					}
				});
				
				x.append(t).setInsets(5);;
			}
			teams.append(x);
		}
		root.append(teams, 3);
		
		return root;
	}
	
	public static void setTheme(FontObject fontObject) {
		currentTheme.copy(fontObject);
		previwe.setCurrent(currentTheme);
		Bold.setActiveIndex(fontObject.isBold() ? 1 : 0);
		Italic.setActiveIndex(fontObject.isItalic() ? 1 : 0);
		int i = Fontname.getIndexOf(currentTheme.getFontname());
		Fontname.setSelectedIndex(i > -1 ? i : 0);

//		for (int j = 0; j < 9; j++){
//			if (namesA[j])
//				Team.TEAMS[j].fontName = fontObject;
//			if (pointsA[j])
//				Team.TEAMS[j].fontPoints = fontObject;	
//		}
//		MainMenu.updateLiveWindow();
		
//		for (Team team : Team.TEAMS)
//			team.fontName = fontObject;
	}

	public static void Open(boolean view) {
		if (window == null)
			create();
		window.setVisible(view);
	}

	public static void addFont(String fontname) {
		if (Fontname.getIndexOf(fontname) == -1)
			Fontname.addItem(fontname);
	}

	public static void removeFont(String fontname) {
		if (Fontname.getIndexOf(fontname) != -1)
			Fontname.removeItem(fontname);
	}

	public static void setChoosenFont(String fontname) {
		if (Fontname.getIndexOf(fontname) == -1)
			return;
		Fontname.setSelectedItem(fontname);
	}

	public static Color loadColor(String key, RCObject in) {
		try {
			return new Color(in.getInteger(key));
		} catch (Exception e) {
			return new Color(0xffffff);
		}
	}

	private static void loadDefault(){
		fontPanel.addFont(new FontObject("Default"));
	}
	
	private static void load() {
		 RCObject in = PointSystem.database.getObject("Font Editor");
		 if (in == null) {
			 loadDefault();
			 return;
		 }
		 
		 int i = in.getField("AmountOfFontObjects").intData();
		 
		 while (i > 0){
			 FontObject fo = new FontObject("FontObject:" + i);
			 fo.load(in, "FontObject:" + i--);
			 System.out.println(fo);
			 fontPanel.addFont(fo);
		 }
		 
//		 for (int i = 0; i < in.getInteger("FontNameAmount"); i++) {
//		 String s = in.getString("FontName_" + i);
//		 if (s == null)
//		 return;
//		 addFont(s);
//		 }
		
//		 System.out.println(in.getSubObject("Font Color Panel"));
//		 colorPanel.loadRCObject(in.getSubObject("Font Color Panel"));

	}

	public static void save() {
		 if(window == null)
			 return;
		 RCObject out = new RCObject("Font Editor");
		
//		 out.addInteger("FontNameAmount", Fontname.getItemCount());
//		 for (int i = 0; i < Fontname.getItemCount(); i++)
//			 out.addString("FontName_" + i, Fontname.getItemAt(i));
		
//		 RCObject colors = colorPanel.asRCObject();
//		 out.addObject("Font Color Panel", colors);

		 out.addInteger("AmountOfFontObjects", fontPanel.fontObjects.size());
		 
		 int i = 0;
		 for (FontObject font : fontPanel.fontObjects){
			 out.addSubObject(font.save("FontObject:" + i++));
			 System.out.println(font);
		 }
		
		 PointSystem.database.addObject(out);
	}
	
	private static class PreviewPanel extends YStrip {

		private Label preview;

		private Image background;

		private FontObject _current;
		
		public PreviewPanel(Window window, FontObject currentTheme) {
			super();

			_current = currentTheme;

			XStrip x = new XStrip();
			x.append(new NBButton(window, "All"), .7);
			x.append(new NBButton(window, "Names"), 1);
			x.append(new NBButton(window, "Points"), 1);
			x.append((BoxObject) null, 2);
//			this.append(x, .1);

			preview = new Label(window) {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				protected void paintComponent(Graphics g) {
					// super.paintComponent(g);
					Rectangle r = preview.getBounds();// g.getClipBounds();
					r.x = r.y = 0;

					g.setColor(Color.GREEN);

					g.drawRect(0, 0, r.width - 1, r.height - 1);

					Image img = background;

					g.drawImage(img, 0, 0, r.width, r.height, null);

					r.x += 5;
					r.y += 5;
					r.width -= 10;
					r.height -= 10;

					_current.drawString(g, "Preview", r);
				}
			};
			// PSData.onUpdate("BackgroundImage", () -> preview.repaint());

			preview.setOpaque(true);
			preview.setBackground(Color.RED);
			this.append(preview);

			window.addUpdate(new UpdateAdd() {
				public void mUpdate() {
					if (background != PSData.backgroundImage) {
						background = PSData.backgroundImage;
						preview.repaint();
					}
				};
			});
		}

		public void update() {
			preview.repaint();
		}

		public void setCurrent(FontObject current) {
			_current = current;
			preview.repaint();
		}
	}

	private static class FontPanel extends YStrip {

		private ImageList fontList;
		private List<FontObject> fontObjects = new ArrayList<>();

		private NBButton Theams;
		private NBButton New, save, reset, Trash;;
		private NBButton load, export;

		public FontPanel(Window window) {
			XStrip x = new XStrip();

			BufferedImage[] icons = PointSystem.icons;

			x = new XStrip();
			save = new NBButton(window, "", "SaveFont");
			save.setBackground(null);
			setSaveState(true);
			x.append(save).setInsets(0);
			reset = new NBButton(window, "", "ResetFont");
			reset.setIcon(icons[5], ScaleType.TILLPASS);
			reset.setBackground(null);
			x.append(reset).setInsets(0);
			New = new NBButton(window, "", "NewFont") {
				// protected void paintComponent(Graphics g) {
				// Rectangle r = g.getClipBounds();
				// g.drawImage(((ImageIcon) New.getIcon()).getImage(), r.x, r.y,
				// null);
				//// super.paintComponent(g);
				// }
			};
			New.setIcon(icons[10], ScaleType.TILLPASS);
			New.setBackground(null);
			x.append(New).setInsets(0);
			Trash = new NBButton(window, "", "TrashFont");
			Trash.setIcon(icons[11], ScaleType.TILLPASS);
			Trash.setBackground(null);
			x.append(Trash).setInsets(0);
			x.append(Theams = new NBButton(window, "Theams", "theams"), 3);
			this.append(x);

			fontList = new ImageList(window, "colorList");
			fontList.setBackground(Color.LIGHT_GRAY);
			fontList.addListSelectionListener((ListSelectionEvent e) -> {
				if (e.getValueIsAdjusting()) {
					onAction();
				}
			});
			this.append(fontList, 6);

			// TODO: import/export - fontPanel
//			x = new XStrip();
//			x.append(load = new NBButton(window, "Load", "load"));
//			load.setIcon(icons[4], ScaleType.TILLPASS);
//			x.append(export = new NBButton(window, "Export", "export"));
//			export.setIcon(icons[3], ScaleType.TILLPASS);
//			this.append(x);
		}

		public void saveFont(FontObject currentTheme) {
			FontObject sf = getSelectedFont();
			if (sf == null)
				return;
			sf.copy(currentTheme);
			fontList.updateImage(fontList.getSelectedIndex(), sf.getIcon(), true);
		}

		public void addFont(FontObject fontObject) {
			fontObjects.add(fontObject);
			fontList.addImage(fontObject.getIcon(), true);
		}

		public void select(int i) {
			fontList.setSelectedIndex(i);
		}

		public FontObject getSelectedFont() {
			int i = fontList.getSelectedIndex();
			if (i > -1) 
				return fontObjects.get(i);
			else 
				return null;
		}

		public void onAction() {
		}

		private boolean saveState = true;

		private void setSaveState(boolean state) {
			this.saveState = state;
			if (state) {
				save.setIcon(PointSystem.icons[6], ScaleType.TILLPASS);
			} else {
				save.setIcon(PointSystem.icons[7], ScaleType.TILLPASS);
			}
		}

		public boolean getSaveState() {
			return saveState;
		}

	}

	private static class FontAdder {

		public static FontAdder instance;

		public static void Open(boolean view) {
			if (instance == null)
				;
			instance = new FontAdder();
			instance.window.setVisible(view);
		}

		private Window window;

		private NBButton Add;

		private PopDownTextField remove, fonts;

		public FontAdder() {
			window = new Window("FontAdder", 200, 200) {
				private static final long serialVersionUID = 1L;

				public void Init() {
					setResizable(false);

					BoxGrid grid = getGrid(1, new double[] { 1, 1, 0.4, 1 });

					fonts = new PopDownTextField(this);
					grid.setComponent(fonts, 0);

					Add = new NBButton(this, "Add");
					grid.setComponent(Add, 1);

					grid.getBox(2).setBottomInset(-6);
					grid.setComponent(new Label(this, "Remove"), 2);

					remove = new PopDownTextField(this);
					grid.setComponent(remove, 3);
				}

				public void ButtonAction(NBButton button) {
					if (button == Add) {
						addFont(fonts.getSelectedItem());
					}
				}

				public void PopDownTextFieldAction(PopDownTextField source) {
					if (source == remove) {
						removeFont(source.getSelectedItem());
						source.setSelectedIndex(-1);
					}
				}

				protected void onCloseing() {
				}

			};

			for (Font f : PSData.environment.getAllFonts())
				fonts.addItem(f.getFontName());

			remove.setData(Fontname.getData());

		}
	}

}
