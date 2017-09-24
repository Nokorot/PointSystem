package util.swing;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.border.Border;

import util.Window;

public class ButtonSetings {
	public static int s_width 				= 0;
	public static int s_height 				= 0;
	public static int s_x 					= 0;
	public static int s_y 					= 0	;
	public static Color s_bColor 			= new JButton().getBackground();
	public static Color s_tColor 			= new JButton().getForeground();
	public static Icon s_icon 				= new JButton().getIcon();
	public static Font s_font 				= new JButton().getFont();
	public static Border s_border 			= new JButton().getBorder();
	public static CompPaint s_paint			= null;
	public static CompContainsOveride s_contains	= null;
	public static boolean s_contentAreaFilled = true;
	
	protected int width 				= s_width;
	protected int height 				= s_height;
	protected int x 					= s_x;
	protected int y 					= s_y;
	protected Color bColor 				= s_bColor;
	protected Color tColor 				= s_tColor;
	protected Icon icon 				= s_icon;
	protected Font font 				= s_font;
	protected Border border 			= s_border;
	protected CompPaint paint			= s_paint;
	protected CompContainsOveride contains		= s_contains;
	protected boolean contentAreaFilled = s_contentAreaFilled;

	protected ActionListener pListener;

	public ButtonSetings(final Window window) {
		this.pListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if ((e.getSource() instanceof Button)) {
					window.ButtonAction((Button) e.getSource());
				}
			}
		};
		
		System.out.println(s_bColor);
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

	public void setIcon(Icon icon) {
		this.icon = icon;
	}

	public void setFont(Font font) {
		this.font = font;
	}

	public void setBorder(Border border) {
		this.border = border;
	}

}