package util.swing;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import util.Window;
import util.handelers.ImageHandeler;

public class Label extends JLabel {
	private static final long serialVersionUID = 1L;
	
	public static final int NOTSCALE = ImageHandeler.NOTSCALE;
	public static final int FYLL = ImageHandeler.FYLL;
	public static final int TILLPASS = ImageHandeler.TILLPASS;
	public static final int STREKK = ImageHandeler.STREKK;
	
	public boolean activated = false;
	public String code = "-1";

	public Window window;
	protected BufferedImage image;
	private int scale = 0;
	
	protected double xScale, yScale;
	
	private CompPaint s_paint;

	public Label(Window window, String text, Rectangle rec, String code) {
		LabelSetings s = window.labelSets;

		if (rec != null)
			setBounds(rec);
		if (code == null)
			code = "-1";

		this.window = window;
		this.code = code;
		setText(text);
		setBackground(s.bColor);
		setForeground(s.tColor);
		setFont(s.font);
		setBorder(s.border);

		setHorizontalAlignment(s.horizontalAlignment);
		setVerticalAlignment(s.verticalAlignment);

		s_paint = s.paint;
		
		if (s.icon != null) {
			setIcon(s.icon, s.iconScale);
		}
		window.panel.add(this);
		
	}
	
	public Label(Window window, BufferedImage icon, int scale) {
		this(window, null, null, null);
		setIcon(icon, scale);
	}

	public Label(Window window, BufferedImage icon, int scale, Rectangle rec) {
		this(window, null, null, null);
		try {
			setIcon(ImageHandeler.getScaledImage(icon, rec.width, rec.height), scale);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Label(Window w){
		this(w, null, null, null);
	}

	public void setIcon(BufferedImage icon, int scale) {
		image = icon;
		this.scale = scale;
		try {
			icon = ImageHandeler.getScaledImage(image, getWidth(), getHeight(), scale);
			xScale = ((double) image.getWidth() / (double) icon.getWidth());
			yScale = ((double) image.getHeight() / (double) icon.getHeight());
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (icon != null) {
			setIcon(new ImageIcon(icon));
		}
	}
	
	protected void paintComponent(Graphics g) {
		if(s_paint != null)
			s_paint.paintComponent(this, g);
		super.paintComponent(g);
	}
	
	public void setBounds(Rectangle r){
		super.setBounds(r);
		BufferedImage icon = image;
		try {
			icon = ImageHandeler.getScaledImage(icon, getWidth(), getHeight(), scale);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (icon != null) {
			setIcon(new ImageIcon(icon));
		}
	}

	public String toString() {
		return "Button [code=" + this.code + ", window=" + this.window + ", getText()=" + getText()
				+ ", getBounds()=" + getBounds() + ", toString()=" + super.toString() + "]";
	}

	public void setFontSize(int size) {
		Font f = getFont();
		setFont(new Font(f.getName(), f.getStyle(), size));
	}

}