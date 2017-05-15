package util.canvar.graphic;

import util.canvar.graphic.Sprite.Sprite;
import util.math.Vec4i;

public class Font {
	private static SpriteSheet font;
	private static Sprite[] characters;
	private static String charindex = " !\"#$%&¥()*+,-./0123456789:'<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{;]~§∆Êÿ¯≈Â";

	public Font(String fontName) {
		font = new SpriteSheet("/fonts/" + fontName + ".png", 8);
		characters = font.getSprites();
	}

	public void render(int x, int y, String text, Screen screen) {
		render(x, y, 0, Vec4i.BLACK, text, screen);
	}

	public void render(int x, int y, Vec4i color, String text, Screen screen) {
		render(x, y, 0, color, text, screen);
	}

	public void render(int x, int y, int spacing, Vec4i color, String text, Screen screen) {
		int line = 0;
		int lineLetter = 0;
		for (int i = 0; i < text.length(); i++) {
			int yOffset = 0;
			char currentChar = text.charAt(i);
			if (currentChar == '\n') {
				line++;
				lineLetter = 0;
			} else {
				int index = charindex.indexOf(currentChar);
				if (index != -1)
					characters[index].render(screen, x + lineLetter * (font.SPRITE_WIDTH + spacing), y + yOffset + line * (font.SPRITE_HEIGHT + 2),
							color);
				lineLetter++;
			}
		}
	}
}