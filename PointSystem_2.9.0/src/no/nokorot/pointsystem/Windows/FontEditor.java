package no.nokorot.pointsystem.Windows;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.List;

import javax.swing.border.LineBorder;

import com.thecherno.raincloud.serialization.RCObject;

import no.nokorot.pointsystem.PSData;
import no.nokorot.pointsystem.PointSystem;
import no.nokorot.pointsystem.Element.Team;
import no.nokorot.pointsystem.utils.ColorPanel;
import util.Window;
import util.swing.Label;
import util.swing.NBButton;
import util.swing.PopDownTextField;
import util.swing.TextList;
import util.swing.gride.BoxGrid;
import util.swing.gride.BoxObject;
import util.swing.gride.XStrip;
import util.swing.gride.YStrip;

public class FontEditor {

//	private static Button[] colorButtons;
//	private static List<Color> colors;
	
	private static Window window;

	private static TextList teamList;
	private static NBButton Name, Points;
	private static NBButton Bold, Italic, AddFont;

	private static PopDownTextField Fontname;
	
	private static ColorPanel colorPanel;

	private static LineBorder selected = new LineBorder(Color.CYAN, 3);
	private static boolean bold, italic;
	
	private static void create() {
		window = new Window("Font Editor", 550, 300) {
			private static final long serialVersionUID = 1L;

			protected void onOpened() {
				setSize(600, 400);
			}
			
			public void Init() {
				setMinimumSize(new Dimension(550, 300));
				
				setResizable(true);
//				panel.setBackground(new Color(0x44));
				labelSets.setFontSize(18);
				
				
				XStrip root = new XStrip();
				
				teamList = new TextList(this);
				teamList.setFontSize(20f);
				teamList.setBackground(Color.LIGHT_GRAY);
				root.append(teamList);
				
				YStrip y = new YStrip();
				
				XStrip fontS = new XStrip();
				Fontname = new PopDownTextField(this);
				fontS.append(Fontname, 3);
				
				AddFont = new NBButton(this, "New");
				fontS.append(AddFont);
				
				y.append(fontS);
				
				fontS = new XStrip();
				Bold = new NBButton(this, "Bold");
				fontS.append(Bold);

				Italic = new NBButton(this, "Italic");
				fontS.append(Italic);
				
//				Fontsize = new TextField(this, "1");
//				fontS.append(Fontsize);
				fontS.append((BoxObject) null);
				
				y.append(fontS);
				
				
				YStrip main = new YStrip();
				Name = new NBButton(this, "Name");
				Name.setBackground(Color.WHITE);
				main.append(Name);
				Points = new NBButton(this, "Points");
				Points.setBackground(Color.WHITE);
				main.append(Points);
				y.append(main, 5).setInsets(20, 40, 20, 40);
				
				
				root.append(new NBButton(this), 2);
				
				colorPanel = new ColorPanel(this);
				root.append(colorPanel, 2).setInsets(0);
				
				this.getFrameBox().setBoxObject(root);
				
				
				teamList.addElement("All");
				for (int i = 0; i < Team.TEAMS.length;)
					teamList.addElement("Team " + ++i);

				ButtonAction(Bold);
				teamList.setSelectedIndex(0);
				teamList.repaint();
			}

			public void ButtonAction(NBButton b) {
				if(b == Bold)
					b.setBorder((bold = !bold) ? selected : null);
				if(b == Italic)
					b.setBorder((italic = !italic) ? selected : null);

				if(b == Name || b == Points){
					int style = (bold ? Font.BOLD: 0) + (italic ? Font.ITALIC : 0);
					b.setFont(new Font(Fontname.getSelectedItem(), style, 24));

					Color c = colorPanel.getSelectedColor();
					if (c == null)
						return;
					int ci = c.getRed() + c.getBlue() + c.getGreen();
					
					if(ci > 255 * 3 * 0.6)
						b.setBackground(new Color(0x222222));
					else
						b.setBackground(new Color(0xffffff));
					
					b.setForeground(c);

					List<String> selected = teamList.getSelectedValuesList();
					
					if(selected.contains("All")){
						for(Team team : Team.TEAMS){
							if(b == Name)
								team.fontName.set(Fontname.getSelectedItem(), style, c);
							if(b == Points)
								team.fontPoints.set(Fontname.getSelectedItem(), style, c);
						}
						MainMenu.updateLiveWindow();
					}
					else {
						for (String string : selected) {
							System.out.println(string);
							int team = Integer.parseInt(string.substring("Team ".length(), string.length())) - 1;
							
							if(b == Name)
								Team.TEAMS[team].fontName.set(Fontname.getSelectedItem(), style, c);
							if(b == Points)
								Team.TEAMS[team].fontPoints.set(Fontname.getSelectedItem(), style, c);
						}
						
						MainMenu.updateLiveWindow();
					}
				}
				
				if (b == AddFont)
					FontAdder.Open(true);
			}
			
			protected void onCloseing() {
				MainMenu.font.actionPerformed(null);
			}
		};
//		MainMenu.docker.registerDockee(window, Docker.EAST_DOCKED);
		load();
		if(Fontname.getElementCount() > 0)
			Fontname.setSelectedIndex(0);
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

	public static void removeFont(String fontname){
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

	private static void load() {
		RCObject in = PointSystem.database.getObject("Font Editor");
		if (in == null) {
			loadDefault();
			return;
		}
		for (int i = 0; i < in.getInteger("FontNameAmount"); i++) {
			String s = in.getString("FontName_" + i);
			if (s == null)
				return;
			addFont(s);
		}
		
		System.out.println(in.getSubObject("Font Color Panel"));
		colorPanel.loadRCObject(in.getSubObject("Font Color Panel"));
		
	}

	private static void loadDefault() {
		addFont("Areal");
	}

	public static void save() {
		if(window == null)
			return;
		RCObject out = new RCObject("Font Editor");

		out.addInteger("FontNameAmount", Fontname.getItemCount());
		for (int i = 0; i < Fontname.getItemCount(); i++)
			out.addString("FontName_" + i, Fontname.getItemAt(i));

		RCObject colors = colorPanel.asRCObject();
		out.addObject("Font Color Panel", colors);
		
		PointSystem.database.addObject(out);
	}

	private static class FontAdder {

		public static FontAdder instance;

		public static void Open(boolean view) {
			if (instance == null);
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
					if (source == remove){
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
