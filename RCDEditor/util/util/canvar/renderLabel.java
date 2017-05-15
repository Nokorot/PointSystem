package util.canvar;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import util.Window;
import util.adds.UpdateAdd;
import util.canvar.graphic.Screen;
import util.swing.Label;

public class renderLabel extends Label {
	private static final long serialVersionUID = 1L;
	private BufferedImage image;
	private int[] pixels;
	private BufferedImage img;
	private int FPS;
	public Screen screen;

	Rectangle pixelSize = new Rectangle();
	Rectangle imageSize = new Rectangle();

	public int ScaleType = 0;

	public renderLabel(Window window, Rectangle rectangle, float scale) {
		super(window, "", rectangle, "RenderLabel");

		imageSize = rectangle;

		pixelSize = new Rectangle((int) (rectangle.getWidth() / scale), (int) (rectangle.getHeight() / scale));

		this.img = new BufferedImage(pixelSize.width, pixelSize.height, 1);
		this.pixels = ((DataBufferInt) this.img.getRaster().getDataBuffer()).getData();

		this.image = new BufferedImage(getWidth(), getHeight(), 1);

		this.screen = new Screen(pixelSize.width, pixelSize.height);

		start();

		window.addUpdate(new UpdateAdd() {
			public void mUpdate() {
				renderLabel.this.render();
			}

			public void mUpdate1() {
				renderLabel.this.update1();
			}
		});
	}

	public void Render() {
	}

	public void start() {
	}

	int frames = 0;

	private void render() {
		this.frames += 1;


		this.screen.clear();
		Render();

		for (int i = 0; i < this.pixels.length; i++)
			this.pixels[i] = this.screen.getIntPixel(i);

		Graphics g = this.image.getGraphics();
		g.drawImage(this.img, 0, 0, imageSize.width, imageSize.height, null);
		g.dispose();

		setIcon(image, ScaleType);
		repaint();
	}

	private void update1() {
		this.FPS = this.frames;
		this.frames = 0;
	}

	public int getFPS() {
		return this.FPS;
	}

	public int getPixelWidth() {
		return pixelSize.width;
	}
	
	public int getPixelHeight() {
		return pixelSize.height;
	}
	
	
}
