package util.canvar.graphic;

import java.awt.image.BufferedImage;
import util.canvar.graphic.Sprite.Sprite;
import util.math.Vec2f;
import util.math.Vec4i;

public class SpriteSheet {
	private String path;
	public Vec4i[] pixels;
	private Sprite[] sprites;
	private int width;
	private int height;
	public final int SPRITE_SIZE;
	public final int SPRITE_WIDTH;
	public final int SPRITE_HEIGHT;

	public SpriteSheet(String path) {
		this.path = path;
		this.SPRITE_SIZE = -1;
		this.SPRITE_WIDTH = -1;
		this.SPRITE_HEIGHT = -1;
		load();
	}

	public SpriteSheet(String path, int spritewidth, int spriteheight) {
		this.path = path;
		this.SPRITE_WIDTH = spritewidth;
		this.SPRITE_HEIGHT = spriteheight;
		if (this.SPRITE_WIDTH == this.SPRITE_HEIGHT) {
			this.SPRITE_SIZE = this.SPRITE_WIDTH;
		} else
			this.SPRITE_SIZE = -1;
		load();

		int w = this.width / spritewidth;
		int h = this.height / spriteheight;

		int frame = 0;
		this.sprites = new Sprite[w * h];
		for (int ya = 0; ya < h; ya++) {
			for (int xa = 0; xa < w; xa++) {
				Vec4i[] spritePixels = new Vec4i[spritewidth * spriteheight];
				for (int y0 = 0; y0 < spriteheight; y0++) {
					for (int x0 = 0; x0 < spritewidth; x0++) {
						spritePixels[(x0 + y0 * spritewidth)] = this.pixels[(x0 + xa * spritewidth + (y0 + ya * spriteheight) * this.width)];
					}
				}
				Sprite sprite = new Sprite(spritePixels, spritewidth, spriteheight);
				this.sprites[(frame++)] = sprite;
			}
		}
	}

	public SpriteSheet(String path, int spriteSize) {
		this(path, spriteSize, spriteSize);
	}

	void load() {
		try {
			System.out.print("Trying to load: " + this.path + "... ");
			BufferedImage image = util.handelers.ImageHandeler.load(this.path);
			System.out.println("succeeded!");
			this.width = image.getWidth();
			this.height = image.getHeight();
			int[] l_pixels = new int[this.width * this.height];
			image.getRGB(0, 0, this.width, this.height, l_pixels, 0, this.width);

			this.pixels = new Vec4i[l_pixels.length];
			for (int i = 0; i < l_pixels.length; i++) {
				long lp = l_pixels[i];

				int a = (int) (lp >> 24);
				int r = (int) ((lp >> 16) % 256L);
				int g = (int) ((lp >> 8) % 256L);
				int b = (int) (lp % 256L);
				a += (a < 0 ? 256 : 0);
				r += (r < 0 ? 256 : 0);
				g += (g < 0 ? 256 : 0);
				b += (b < 0 ? 256 : 0);

				this.pixels[i] = new Vec4i(r, g, b, a);
			}
		} catch (Exception e) {
			System.err.println("failed!");
		}
	}

	public String getPath() {
		return this.path;
	}

	public int getWidth() {
		return this.width;
	}

	public int getHeight() {
		return this.height;
	}

	public Vec4i[] getPixels() {
		return this.pixels;
	}

	public Sprite[] getSprites() {
		return this.sprites;
	}

	public Sprite getSprit(int x, int y) {
		return this.sprites[(x + y * this.width)];
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

	public void render(Screen screen, Vec2f vec, boolean center) {
		render(screen, vec.x, vec.y, center);
	}
}