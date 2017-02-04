package no.nokorot.pointsystem.utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import com.thecherno.raincloud.serialization.RCObject;

public class FontObject {

	public static Map<String, FontObject> fontObjects = new HashMap<String, FontObject>();

	private static Graphics g = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB).getGraphics();
	private FontMetrics f;

	private String fontname;
	private int style;
	private Color color;

	public FontObject(String name) {
		fontObjects.put(name, this);
		set("Areal", Color.WHITE);
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
		f = g.getFontMetrics(new Font(fontname, Font.BOLD, 100));
	}

	public void setStyle(int style){
		this.style = style;
	}
	
	public void setColor(Color color) {
		this.color = color;
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
		return out;
	}

	public void load(RCObject parent, String key) {
		RCObject in = parent.getSubObject(key);
		set(in.getString("fontname"), in.getInteger("style"), loadColor(in, "color"));
	}

	public Font getFont(int type, float size) {
		return new Font(fontname, type, (int) size);
	}

	public int getFittingFontSize(int width, int height, String s) {
		if (s == null)
			return 1;
		int length = Math.max(s.length(), 1);
		return (int) Math.min((width / length * getAspectRatio(s)), height * 0.85f);
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
		return new Color(in.getInteger("color"));
	}

}
