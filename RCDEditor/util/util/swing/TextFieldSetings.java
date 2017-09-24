package util.swing;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextField;
import javax.swing.border.Border;

import util.Window;

public class TextFieldSetings {
	public static int s_HorizontalAlignment = 0;
	public static int s_width 				= 0;
	public static int s_height 				= 0;
	public static int s_x 					= 0;
	public static int s_y 					= 0	;
	public static Color s_bColor 			= new JTextField().getBackground();
	public static Color s_tColor 			= new JTextField().getForeground();
	public static Font s_font 				= new JTextField().getFont();
	public static Border s_border 			= new JTextField().getBorder();
	
	protected int HorizontalAlignment 	= s_HorizontalAlignment;
	protected int width 				= s_width;
	protected int height 				= s_height;
	protected int x 					= s_x;
	protected int y 					= s_y;
	protected Color bColor 				= s_bColor;
	protected Color tColor 				= s_tColor;
	protected Font font 				= s_font;
	protected Border border 			= s_border;
	
	protected ActionListener pListener;
	public TextFieldSetings(final Window window) {
		this.pListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if ((e.getSource() instanceof TextField)) {
					window.TextFieldAction((TextField) e.getSource());
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

	public void setFont(Font font) {
		this.font = font;
	}

	public void setBorder(Border border) {
		this.border = border;
	}

	public void setFontSize(int i) {
		this.font = new Font(this.font.getName(), this.font.getStyle(), i);
	}
}