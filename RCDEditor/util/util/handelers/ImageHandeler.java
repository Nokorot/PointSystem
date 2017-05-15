package util.handelers;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

public class ImageHandeler {

	public static final int NOTSCALE = 0;
	public static final int FYLL = 3241;
	public static final int TILLPASS = 3242;
	public static final int STREKK = 3243;

	// TODO: Use enum insted;

	public ImageHandeler() {
	}

	public static BufferedImage load(String path) {
		BufferedImage image = null;
		try {
			image = ImageIO.read(ImageHandeler.class.getResource(path));
			System.out.println("Sucsecd!!");
		} catch (Exception e) {
			System.err.println("Failed!! loding: " + path);
		}
		return image;
	}

	public static BufferedImage loadGloab(String path) {
		BufferedImage image = null;
		try {
			System.out.print("Loding Image on global: (" + path + ") .... ");
			image = ImageIO.read(new java.io.File(path));
			System.out.println("Sucsecd!!");
		} catch (Exception e) {
			System.err.println("\nFaild!! Exception! on: " + path);
		}

		return image;
	}

	public static BufferedImage cut(BufferedImage img, Rectangle rec) {
		try {
			img = img.getSubimage(rec.x, rec.y, rec.width, rec.height);
		} catch (Exception e) {
			System.out.println(img.getHeight() + ", " + img.getWidth() + ", " + rec);
		}

		return img;
	}

	public static BufferedImage cut(BufferedImage img, int x, int y, int size) {
		return cut(img, new Rectangle(x * size, y * size, size, size));
	}

	public static BufferedImage getScaledImage(BufferedImage image, int width, int height)
			throws java.io.IOException {
		if (image == null || width <= 0 || height <= 0)
			return null;
		double imageWidth = image.getWidth();
		double imageHeight = image.getHeight();

		double scaleX = width / imageWidth;
		double scaleY = height / imageHeight;
		AffineTransform scaleTransform = AffineTransform.getScaleInstance(scaleX, scaleY);
		AffineTransformOp bilinearScaleOp = new AffineTransformOp(scaleTransform, 2);

		return bilinearScaleOp.filter(image, new BufferedImage(width, height, image.getType()));
	}

	public static BufferedImage createCerImage(int width, int height, Color color, Color back) {
		BufferedImage img = new BufferedImage(width, height, 1);

		Graphics g = img.getGraphics();
		g.setColor(back);
		g.fillRect(0, 0, width, height);
		g.setColor(color);
		g.fillOval(0, 0, width, height);
		g.dispose();

		return img;
	}

	public static BufferedImage createRandomImage(int width, int height) {
		int[] pixels = new int[width * height];

		BufferedImage img = new BufferedImage(width, height, 1);
		pixels = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();

		Random rand = new Random();
		for (int i = 0; i < pixels.length; i++)
			pixels[i] = rand.nextInt(0xffffff);

		return img;
	}

	public static BufferedImage getScaledImage(BufferedImage icon, int width, int height, int scale)
			throws IOException {
		if (scale == STREKK)
			return getScaledImage(icon, width, height);
		else if (scale == TILLPASS) {
			double widthS = (double) width / (double) icon.getWidth();
			double heightS = (double) height / (double) icon.getHeight();
			width = (int) (Math.min(widthS, heightS) * icon.getWidth());
			height = (int) (Math.min(widthS, heightS) * icon.getHeight());
			return getScaledImage(icon, width, height);
		} else if (scale == FYLL) {
			double widthS = (double) width / (double) icon.getWidth();
			double heightS = (double) height / (double) icon.getHeight();
			return getScaledImage(icon, (int) ((double) icon.getWidth() * Math.max(widthS, heightS)),
					(int) ((double) icon.getHeight() * Math.max(widthS, heightS)));
		} else
			return icon;
	}
}