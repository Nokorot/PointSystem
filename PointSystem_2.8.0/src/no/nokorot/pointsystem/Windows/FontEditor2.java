package no.nokorot.pointsystem.Windows;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JColorChooser;
import javax.swing.border.LineBorder;

import no.nokorot.pointsystem.PSData;
import no.nokorot.pointsystem.PointSystem;
import no.nokorot.pointsystem.Element.Team;
import util.Window;
import util.swing.Button;
import util.swing.Docker;
import util.swing.Label;
import util.swing.PopDownTextField;
import util.swing.TextList;
import util.swing.gride.BoxGrid;

import com.thecherno.raincloud.serialization.RCObject;

public class FontEditor2 {

	private static Button[] colorButtons;
	private static List<Color> colors;

	private static Window window;

	private static TextList teamList;
	private static Button Name, Points, Default;
	private static Button Bold, Italic, AddFont, AddColour;

	private static PopDownTextField Fontname;

	private static LineBorder selected = new LineBorder(Color.CYAN, 3);
	private static Button selectedColour;
	private static boolean bold, italic;
	
	private static void create() {
		window = new Window("Font Editor", 400, 300) {
			private static final long serialVersionUID = 1L;

			public void Init() {
				setResizable(false);
				panel.setBackground(new Color(0x44));

				setVisible(true);
				BoxGrid grid = getGrid(new double[] { 2, 6 }, 1);

				teamList = new TextList(this);
				teamList.setFontSize(20f);
				teamList.setBackground(Color.LIGHT_GRAY);
//				teamList.setBorder(new LineBorder(Color.black, 2));
				grid.getBox(0).setComponent(teamList);

				BoxGrid g0 = grid.getBox(1).getInsideGrid(1, new double[]{1, 4});
				
				BoxGrid g1 = g0.getBox(0).getInsideGrid(2, 1);
				
				Name = new Button(this, "Name");
				Name.setBackground(Color.WHITE);
				g1.getBox(0).setComponent(Name);

				Points = new Button(this, "Points");
				Points.setBackground(Color.WHITE);
				g1.getBox(1).setComponent(Points);

				BoxGrid g2 = g0.getBox(1).getInsideGrid(1, new double[] { 1, 1, 2.5 });

				Fontname = new PopDownTextField(this);
				g2.getBox(0).setComponent(Fontname);

				BoxGrid g3 = g2.getBox(1).getInsideGrid(new double[]{1, 1, 2}, 1);
//				g3.setAllInsets(2, 3, 2, 3);

				Bold = new Button(this, "Bold");
				g3.getBox(0).setComponent(Bold);

				Italic = new Button(this, "Italic");
				g3.getBox(1).setComponent(Italic);

				
				labelSets.setFontSize(18);
				BoxGrid g3_0 = g3.getBox(2).getInsideGrid(2, new double[]{3, 5});
				g3_0.getBox(0, 0).setBottomInset(0);
				g3_0.setComponent(new Label(this, "add"), 0, 0);
				AddFont = new Button(this, "Font");
				AddFont.setFonrSize(18);
				g3_0.getBox(0, 1).setComponent(AddFont);

				g3_0.getBox(1, 0).setBottomInset(0);
				g3_0.setComponent(new Label(this, "add"), 1, 0);
				AddColour = new Button(this, "Color");
				g3_0.getBox(1, 1).setComponent(AddColour);

				BoxGrid gColour = g2.getBox(2).getInsideGrid(4, 3);
//				g2.getBox(2).setInsets(0, 0, -3, 0);

				
				int size = 12; //gColour.size():
				colorButtons = new Button[size];
				colors = new ArrayList<Color>(size);
				for (int i = 0; i < size; i++) {
					colorButtons[i] = new Button(this, "", "color");
					gColour.getBox(i).setSideInsets(4);
					gColour.getBox(i).setComponent(colorButtons[i]);
				}

				teamList.addElement("All");
				for (int i = 0; i < Team.TEAMS.length;)
					teamList.addElement("Team " + ++i);

				System.out.println(gColour.size());
				ButtonAction(colorButtons[0]);
				ButtonAction(Bold);
				teamList.setSelectedIndex(0);
			}

			public void ButtonAction(Button b) {
				if(b.code == "color"){
					if(selectedColour != null)
						selectedColour.setBorder(null);
					b.setBorder(selected);
					selectedColour = b;
				}
				if(b == Bold)
					b.setBorder((bold = !bold) ? selected : null);
				if(b == Italic)
					b.setBorder((italic = !italic) ? selected : null);

				if(b == Name || b == Points){
					int style = (bold ? Font.BOLD: 0) + (italic ? Font.ITALIC : 0);
					b.setFont(new Font(Fontname.getSelectedItem(), style, 24));

					Color c =  selectedColour.getBackground();
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
							System.out.println(team);
//							if(Team.TEAMS[team] == null)
//								continue;
							
							if(b == Name)
								Team.TEAMS[team].fontName.set(Fontname.getSelectedItem(), style, c);
							if(b == Points)
								Team.TEAMS[team].fontPoints.set(Fontname.getSelectedItem(), style, c);
						}
						
						MainMenu.updateLiveWindow();
					}
				}
				
				if(b == AddColour){
					System.out.println("Find");
					Color c = JColorChooser.showDialog(null, "Pick a color", selectedColour.getBackground());
					if (c == null)
						return;
					setSelectedColor(0);
					addColor(c);
				}
				
				if (b == AddFont)
					FontAdder.Open(true);
			}
			
			protected void onCloseing() {
				MainMenu.font.actionPerformed(null);
			}
		};
		MainMenu.docker.registerDockee(window, Docker.EAST_DOCKED);
		load();
		if(Fontname.getElementCount() > 0)
			Fontname.setSelectedIndex(0);
	}

	public static void Open(boolean view) {
		if (window == null)
			create();
		window.setVisible(view);
	}

	public static void addColors(int[] c) {
		for (int color : c)
			colors.add(new Color(color));
		updateCButtons();
	}

	public static void addColors(Color[] c) {
		for (Color color : c)
			colors.add(0, color);
		updateCButtons();
	}

	public static void addColor(int c) {
		addColor(new Color(c));
	}

	public static void addColor(Color c) {
		colors.add(0, c);
		updateCButtons();
	}
	
	public static void setSelectedColor(int index){
		if(index < 0 || index >= colorButtons.length)
			return;
		window.ButtonAction(colorButtons[index]);
	}
	
	private static void updateCButtons() {
		while (colors.size() > colorButtons.length)
			colors.remove(colors.size() - 1);

		for (int i = 0; i < colorButtons.length; i++)
			colorButtons[i].setBColor(colors.get(i));
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
		for (int i = colorButtons.length; i-- > 0;)
			colors.add(0, loadColor("FontColor_" + i, in));
		for (int i = 0; i < in.getInteger("FontNameAmount"); i++) {
			String s = in.getString("FontName_" + i);
			if (s == null)
				return;
			addFont(s);
		}
		updateCButtons();
	}

	private static void loadDefault() {
		addColors(PSData.colors);
		addFont("Areal");
	}

	public static void save() {
		if(window == null)
			return;
		RCObject out = new RCObject("Font Editor");

		out.addInteger("FontNameAmount", Fontname.getItemCount());
		for (int i = 0; i < Fontname.getItemCount(); i++)
			out.addString("FontName_" + i, Fontname.getItemAt(i));

		for (int i = 0; i < colors.size(); i++)
			out.addInteger("FontColor_" + i, colors.get(i).getRGB());
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

		private Button Add;

		private PopDownTextField remove, fonts;

		public FontAdder() {
			window = new Window("FontAdder", 200, 200) {
				private static final long serialVersionUID = 1L;

				public void Init() {
					setResizable(false);

					BoxGrid grid = getGrid(1, new double[] { 1, 1, 0.4, 1 });

					fonts = new PopDownTextField(this);
					grid.setComponent(fonts, 0);

					Add = new Button(this, "Add");
					grid.setComponent(Add, 1);

					grid.getBox(2).setBottomInset(-6);
					grid.setComponent(new Label(this, "Remove"), 2);

					remove = new PopDownTextField(this);
					grid.setComponent(remove, 3);
				}

				public void ButtonAction(Button button) {
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
