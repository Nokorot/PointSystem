package util.canvar.graphic.Sprite;

import util.canvar.graphic.Screen;
import util.math.Vec4i;

public class Sprite {
	private int width;
	private int height;
	public Vec4i[] pixels;
	protected util.canvar.graphic.SpriteSheet sheet;

	public Sprite(Vec4i[] pixels, int width, int height) {
		this.width = width;
		this.height = height;
		this.pixels = new Vec4i[pixels.length];
		for (int i = 0; i < pixels.length; i++) {
			this.pixels[i] = pixels[i];
		}
	}

	public Sprite(Vec4i color, int size) {
		this.width = size;
		this.height = size;
		this.pixels = new Vec4i[size * size];
		for (int i = 0; i < this.pixels.length; i++) {
			this.pixels[i] = color;
		}
	}

	public void render(Screen screen, double x0, double y0, boolean center) {
		for (int y = 0; y < this.height; y++) {
			for (int x = 0; x < this.width; x++) {
				int xp = screen.ScrollX((int) (x + x0 - (center ? this.width / 2 : 0)));
				int yp = screen.ScrollY((int) (y + y0 - (center ? this.height / 2 : 0)));

				if ((xp >= 0) && (yp >= 0) && (xp < screen.Width) && (yp < screen.Height)) {
					Vec4i col = this.pixels[(x + y * this.width)];
					if (!col.equals(Screen.ALPHA_COL))
						screen.pixels[(xp + yp * screen.Width)] = col;
				}
			}
		}
	}

	public void render(Screen screen, util.math.Vec2f vec, boolean center) {
		render(screen, vec.x, vec.y, center);
	}

	public int getWidth() {
		return this.width;
	}

	public int getHeight() {
		return this.height;
	}

	public void render(Screen screen, int xp, int yp, Vec4i color) {
		for (int y = 0; y < this.height; y++) {
			int ya = y + yp;
			for (int x = 0; x < this.width; x++) {
				int xa = x + xp;
				if ((xa >= 0) && (xa < screen.Width) && (ya >= 0) && (ya < screen.Height)) {
					Vec4i col = this.pixels[(x + y * this.width)];
					if (col.equals(Screen.BETA_COL))
						col = color;
					if (!col.equals(Screen.ALPHA_COL)) {
						screen.pixels[(xa + ya * screen.Width)] = col;
					}
				}
			}
		}
	}
}