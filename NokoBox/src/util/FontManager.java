package util;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class FontManager {
	
	private static BufferedImage canves; 
	
	
	public static Graphics getCanvesGraphichs(int minWidth, int minHeight) {
		System.out.println(minWidth + " " + minHeight);
		if (canves == null || canves.getWidth() < minWidth || canves.getHeight() < minHeight)
			canves = new BufferedImage(minWidth, minHeight, BufferedImage.TYPE_3BYTE_BGR);
		return canves.getGraphics();
	}
	
	public static int getStringWidth(Font f, String string) {
		FontMetrics fm = new FontMetrics(f) {};
		return (int) fm.getStringBounds(string, 
				getCanvesGraphichs(1, 1)).getWidth();
	}

}
