package no.nokorot.pointsystem.utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import com.thecherno.raincloud.serialization.RCObject;

import sun.text.resources.cldr.om.FormatData_om;
import util.handelers.ImageHandeler;

/**
 * @author nokorot
 *
 */
public class FontObject {

	private static Graphics g = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB).getGraphics();
	private FontMetrics f;
	
	private String fontname;
	private int style;
	private Color color = Color.BLUE;
	
	
	private Color border = Color.BLACK;
	private float borderwidth = .50f;
	
	public FontObject(String name) {
		set("Areal", Color.WHITE);
	}
	
	public FontObject(FontObject fo){
		copy(fo);
	}

	int ShiftY(int p, int distance) {
		return (p + distance);
	}
	int ShiftX(int p, int distance) {
		return (p + distance);
	}
	
	public void drawString(Graphics g, String text, Rectangle bounds) {
		Rectangle r = bounds;

		Font font = new Font(fontname, style, getFittingFontSize(r.width, r.height, text));
		FontMetrics m = g.getFontMetrics(font);

//		Fitt it;
//		int length = Math.max(s.length(), 1);
//		Math.min((width / length * getAspectRatio(s)), height * 0.85f);
		
		int x = (r.width - m.stringWidth(text)) / 2 + r.x;
		int y = (int) ((r.height + font.getSize() * .75) / 2) + r.y;

		int borderwidth = (int) (this.borderwidth * Math.sqrt(font.getSize()));
		
		int n = ShiftY(y, -borderwidth);
		int s = ShiftY(y, borderwidth);
		int e = ShiftX(x, borderwidth);
		int w = ShiftX(x, -borderwidth);
		
		g.setFont(font);
		g.setColor(border);
		for (int i = -borderwidth; i <= borderwidth; i++) {
			g.drawString(text, ShiftX(x, i), n);
			g.drawString(text, ShiftX(x, i), s);
			g.drawString(text, e, ShiftY(y, i));
			g.drawString(text, w, ShiftY(y, i));
		}
		g.setColor(color);
		g.drawString(text, x, y);
	}
	
	public void copy(FontObject other) {
		setFontname(other.fontname);
		setStyle(other.style);
		setColor(other.color);
		setBorderColor(other.border);
		setBorderWidth(other.borderwidth);
	}
	
	public void set(String fontname, Color color) {
		setFontname(fontname);
		setColor(color);
	}

	public void set(String fontname, int style, Color color) {
		setFontname(fontname);
		setStyle(style);
		setColor(color);
	}

	public void setFontname(String fontname) {
		this.fontname = fontname;
		f = g.getFontMetrics(new Font(fontname, style, 100));
	}

	public void setStyle(int style) {
		this.style = style;
		f = g.getFontMetrics(new Font(fontname, style, 100));
	}

	public void setColor(Color color) {
		this.color = color;
	}
	
	public void setBold(boolean bold) {
		style = bold ? (style | 1) : (style & (0xff - 1));
	}

	public void setBorderColor(Color color) {
		border = color;
	}
	
	public Color getBorderColor() {
		return border;
	}

	public void setBorderWidth(float thiknes) {
		borderwidth = thiknes;
	}
	
	public float getBorderWidth() {
		return borderwidth;
	}
	
	public boolean isBold() {
		return (style & 1) > 0;
	}
	
	public void setItalic(boolean italic) {
		style = italic ? (style | 2) : (style & (0xff - 2));
	}
	
	public boolean isItalic() {
		return (style & 2) > 0;
	}

	public String getFontname() {
		return fontname;
	}

	public int getStyle() {
		return style;
	}

	public Color getColor() {
		return color;
	}

	public RCObject save(String name) {
		RCObject out = new RCObject(name);
		out.addString("fontname", fontname);
		out.addInteger("style", style);
		out.addInteger("color", color.getRGB());
		out.addInteger("border", border.getRGB());
		out.addFloat("borderWidth", borderwidth);
		return out;
	}

	public void load(RCObject parent, String key) {
		RCObject in = parent.getSubObject(key);
		set(in.getString("fontname"), in.getInteger("style"), loadColor(in, "color"));
		setBorderColor(loadColor(in, "border"));
		setBorderWidth(in.getFlaot("borderWidth"));
	}

	public Font getFont(int type, float size) {
		return new Font(fontname, type, (int) size);
	}

	public int getFittingFontSize(int width, int height, String s) {
		
		String ss = "abcdefghijklmnopqrstuvwxyz";
		ss += ss.toUpperCase();
		ss += "1234567890";
		ss += "!-.,<>[]()";
		
		float ww = 0;
		for (char c : ss.toCharArray())
			ww += f.charWidth(c);
		ww /= ss.length();
		
		float w = ww*s.length();//f.stringWidth(s);
		
		
		if (s == null || s.length() == 0)
			return 1;
		
		return (int) Math.min( width * 100 / w , height * 0.85f);
		
//		int length = Math.max(s.length(), 1);
//		return (int) Math.min((width / length * getAspectRatio(s)), height * 0.85f);
	}

	private double getAspectRatio(String s) {
		if (s == null)
			return 1;
		int width = 0;
		for (char c : s.toCharArray())
			width += f.charWidth(c);
		int length = Math.max(s.length(), 1);
		return 100f / (width / length);
	}

	public Color loadColor(RCObject in, String key) {
		return new Color(in.getInteger(key));
	}

	@Override
	public String toString() {
		return "FontObject [fontname=" + fontname + ", style=" + style + ", color=" + color
				+ ", border=" + border + ", borderwidth=" + borderwidth + "]";
	}

	public BufferedImage getIcon() {
		int width = 100, height = 100;
		BufferedImage icon = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);//ImageHandeler.createImage_Cercel(50, 50, Color.WHITE, null);
		
		Graphics g = icon.getGraphics();
//		g.setColor(Color.GREEN);
//		g.fillRect(0, 0, width, height);
		this.drawString(g, "A", new Rectangle(width, height));
		g.dispose();
		
		return icon;
	}
	
	
}
