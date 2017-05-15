package util.canvar.graphic;

import static util.prints.print;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;

import util.handelers.ImageHandeler;
import util.math.Vec2f;
import util.math.Vec4i;

public class Screen {
	public int Width;
	public int Height;
	public Vec4i[] pixels;
	private Vec4i[] backgroundPixels;
	private Vec2f center;
	private Vec2f size;
	public static final Vec4i ALPHA_COL = new Vec4i(255, 0, 255, 255);
	public static final Vec4i BETA_COL = new Vec4i(255, 255, 255, 255);

	public boolean scroll = false;
	double doss;

	public Screen(int width, int height) {
		this.Width = width;
		this.Height = height;
		this.pixels = new Vec4i[width * height];

		this.center = new Vec2f(width / 2, height / 2);
		this.size = new Vec2f(width, height);

		this.backgroundPixels = new Vec4i[width * height];
		setBackgrondColor(Vec4i.BLACK);
	}

	public void clear() {
		for (int i = 0; i < this.Width * this.Height; i++) {
			this.pixels[i] = this.backgroundPixels[i];
		}
	}

	public void doss(double value) {
		this.doss += value;
		if (this.doss > 1.0D) {
			for (int i = 0; (i < this.Width * this.Height) && (this.doss >= 1.0D); i++) {
				int red = this.pixels[i].x;
				int green = this.pixels[i].y;
				int blue = this.pixels[i].z;

				if (red > 0)
					red -= (int) this.doss;
				if (green > 0)
					green -= (int) this.doss;
				if (blue > 0) {
					blue -= (int) this.doss;
				}
				this.pixels[i] = new Vec4i(red, green, blue, 0);
			}

			this.doss -= (int) this.doss;
		}
	}

	public Vec2f getSize() {
		return this.size;
	}

	public Vec2f getCenter() {
		return this.center;
	}

	public void setBackgrondColor(Vec4i col) {
		for (int i = 0; i < this.Width * this.Height; i++)
			this.backgroundPixels[i] = col;
	}
	
	public void setBackgrondColor(Color col) {
		setBackgrondColor(Vec4i.fromColor(col));
	}

	public void setBackgrondImage(BufferedImage img) {
		try {
			print(new String[] { "hey" });
			if ((img.getWidth() != this.Width) || (img.getHeight() != this.Height))
				img = ImageHandeler.getScaledImage(img, this.Width, this.Height);
		} catch (IOException e) {
			e.printStackTrace();
		}
		int[] l_pixels = new int[this.Width * this.Height];
		img.getRGB(0, 0, this.Width, this.Height, l_pixels, 0, this.Width);

		for (int i = 0; i < l_pixels.length; i++) {
			long lp = l_pixels[i];

			int a = (int) (lp >> 24);
			int r = (int) ((lp >> 16) % 256L);
			int g = (int) ((lp >> 8) % 256L);
			int b = (int) (lp % 256L);
			if (a < 0)
				a += 256;
			if (r < 0)
				r += 256;
			if (g < 0)
				g += 256;
			if (b < 0) {
				b += 256;
			}
			this.backgroundPixels[i] = new Vec4i(r, g, b, a);
		}
	}

	public void setPixel(double xd, double yd, Vec4i col) {
		int x = (int) xd;
		int y = (int) yd;
		if ((x < 0) || (y < 0) || (x >= this.Width) || (y >= this.Height)) {
			return;
		}

		int alpha = col.w;
		if (alpha >= 255) {
			this.pixels[(x + y * this.Width)] = col;
		} else {
			Vec4i oldCol = this.pixels[(x + y * this.Width)];
			int red = (col.x * col.w + oldCol.x * (255 - col.w)) / 255;
			int green = (col.y * col.w + oldCol.y * (255 - col.w)) / 255;
			int blue = (col.z * col.w + oldCol.z * (255 - col.w)) / 255;

			this.pixels[(x + y * this.Width)] = new Vec4i(red, green, blue, 0);
		}
	}

	public void setPixel(Vec2f vec2, Vec4i col) {
		setPixel(vec2.x, vec2.y, col);
	}

	public void renderRec(int x0, int y0, int width, int height, Vec4i color) {
		for (int y = y0; y < y0 + height; y++) {
			for (int x = x0; x < x0 + width; x++) {
				if ((x >= 0) && (y >= 0) && (x < this.Width) && (y < this.Height)) {

					setPixel(x, y, color);
				}
			}
		}
	}

	public int getIntPixel(int i) {
		Vec4i p = this.pixels[i];
		return (p.w << 24) + (p.x << 16) + (p.y << 8) + p.z;
	}

	public int ScrollX(int xp) {
		if (!this.scroll)
			return xp;
		xp %= this.Width;
		if (xp < 0)
			xp += this.Width;
		return xp;
	}

	public int ScrollY(int yp) {
		if (!this.scroll)
			return yp;
		yp %= this.Height;
		if (yp < 0)
			yp += this.Height;
		return yp;
	}
}