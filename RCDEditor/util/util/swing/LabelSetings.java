package util.swing;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.JLabel;
import javax.swing.border.Border;

import util.Window;

public class LabelSetings {
	public static int s_width = 0;
	public static int s_height = 0;
	public static int s_x = 0;
	public static int s_y = 0;
	public static Color s_bColor = new JLabel().getBackground();
	public static Color s_tColor = new JLabel().getForeground();
	public static Font s_font = new JLabel().getFont();
	public static Border s_border = new JLabel().getBorder();
	public static int s_horizontalAlignment = 0;
	public static int s_verticalAlignment = 0;
	public static CompPaint s_paint;
	private static BufferedImage s_icon = (BufferedImage) new JLabel().getIcon();
	private static int s_iconScale = 0;


	protected int width = s_width;
	protected int height = s_height;
	protected int x = s_x;
	protected int y = s_y;
	protected Color bColor = s_bColor;
	protected Color tColor = s_tColor;
	protected BufferedImage icon = s_icon;
	protected int iconScale = s_iconScale;
	protected Font font = s_font;
	protected Border border = s_border;
	protected int horizontalAlignment = s_horizontalAlignment;
	protected int verticalAlignment = s_verticalAlignment;
	protected CompPaint paint = s_paint;

	protected ActionListener pListener;

	public LabelSetings(final Window window) {
		this.pListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if ((e.getSource() instanceof Button)) {
					window.ButtonAction((Button) e.getSource());
				}
			}
		};
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void setBColor(Color bColor) {
		this.bColor = bColor;
	}

	public void setTColor(Color tColor) {
		this.tColor = tColor;
	}

	public void setIcon(BufferedImage icon) {
		this.icon = icon;
	}

	public void setFont(Font font) {
		this.font = font;
	}

	public void setBorder(Border border) {
		this.border = border;
	}

	public void setIcon(BufferedImage icon, int scale){
		this.icon = icon;
		this.iconScale = scale;
	}
	
	public static void setStandardIcon(BufferedImage icon, int scale){
		s_icon = icon;
		s_iconScale = scale;
	}
}