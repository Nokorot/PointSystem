import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import apachePOI.BufferedImageSlides;

public class Main extends JPanel {

	private static JFrame frame;
	private static JLabel label;

	static BufferedImageSlides bis;
	private static int index = 0;

	public Main() {
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

		bis = new BufferedImageSlides("Test.pptx");
		JButton previus = new JButton("Previus");
		previus.addActionListener((ActionEvent e) -> {
			index = Math.max(0, index - 1);
			updateLabel();
		});
		JButton next = new JButton("Next");
		next.addActionListener((ActionEvent e) -> {
			index = Math.min(bis.numberOfPages() - 1, index + 1);
			updateLabel();
		});

		add(previus);
		add(label = new JLabel());
		add(next);

		frame.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				updateLabel();
			}
		});
	}

	private void updateLabel() {
		try {
			BufferedImage img = bis.getBufferedImage(index);
			ImageIcon icon = new ImageIcon(
					getScaledImage(img, frame.getWidth(), frame.getHeight(), ScaleType.TILLPASS));
			label.setIcon(icon);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public static void main(String[] args) {
		frame = new JFrame();
		frame.setDefaultCloseOperation(3);
		// Dimension size = new Dimension(500, 350);
		frame.setSize(new Dimension(850, 500));
		frame.setMinimumSize(new Dimension(600, 500));
		frame.add(new Main());
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	public static BufferedImage getScaledImage(BufferedImage image, int width, int height) throws java.io.IOException {
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

	public static enum ScaleType {
		NOTSCALE, FYLL, TILLPASS, STREKK;

		public static ScaleType parse(String in) {
			if (in == null)
				return NOTSCALE;
			return valueOf(in);
		}
	}

	public static BufferedImage getScaledImage(BufferedImage icon, int width, int height, ScaleType scale)
			throws IOException {
		if (icon == null)
			return null;
		if (scale == ScaleType.STREKK)
			return getScaledImage(icon, width, height);
		else if (scale == ScaleType.TILLPASS) {
			double widthS = (double) width / (double) icon.getWidth();
			double heightS = (double) height / (double) icon.getHeight();
			width = (int) (Math.min(widthS, heightS) * icon.getWidth());
			height = (int) (Math.min(widthS, heightS) * icon.getHeight());
			return getScaledImage(icon, width, height);
		} else if (scale == ScaleType.FYLL) {
			double widthS = (double) width / (double) icon.getWidth();
			double heightS = (double) height / (double) icon.getHeight();
			return getScaledImage(icon, (int) ((double) icon.getWidth() * Math.max(widthS, heightS)),
					(int) ((double) icon.getHeight() * Math.max(widthS, heightS)));
		} else
			return icon;
	}

}
