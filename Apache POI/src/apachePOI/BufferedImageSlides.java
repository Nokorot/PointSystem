package apachePOI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.xmlbeans.impl.tool.PrettyPrinter;

public class BufferedImageSlides {

	private static final double scale = 2;

	private String filename;

	private AffineTransform at;
	private Dimension pgsize;

	private List<XSLFSlide> slides;
	private Map<XSLFSlide, BufferedImage> imageSlides = new HashMap();

	public BufferedImageSlides(String filename) {
		this.filename = filename;
		// if (filename.endsWith(".pptx"))
		LoadPPTX();
	}

	private void LoadPPTX() {
		try {
//			InputStream is = (InputStream) BufferedImageSlides.class.getResource(filename).openStream();
			InputStream is = new FileInputStream(new File(filename));
			XMLSlideShow ppt = new XMLSlideShow(is);
			is.close();

			this.pgsize = ppt.getPageSize();
			this.slides = ppt.getSlides();

		} catch (IOException e) {
		}
	}

	public int numberOfPages() {
		return slides.size();
	}

	public void drawPage(Graphics graphics, int index, int scale) {

		// graphics.setTransform(at);

		graphics.setColor(Color.white);
		graphics.fillRect(0, 0, pgsize.width, pgsize.height);
		slides.get(index).draw((Graphics2D) graphics);
		graphics.dispose();
	}

	public BufferedImage getBufferedImage(int index) {
		if (at == null) {
			at = new AffineTransform();
			at.setToScale(scale, scale);
		}

		XSLFSlide slide = slides.get(index);
		if (imageSlides.containsKey(slide))
			return imageSlides.get(slide);

		BufferedImage img = new BufferedImage((int) Math.ceil(pgsize.width * scale),
				(int) Math.ceil(pgsize.height * scale), BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = img.createGraphics();
		graphics.setTransform(at);

		graphics.setPaint(Color.white);
		graphics.fill(new Rectangle2D.Float(0, 0, pgsize.width, pgsize.height));
		slide.draw(graphics);
		graphics.dispose();

		imageSlides.put(slide, img);
		return img;
	}
	
	private void WriteImage2PNG(BufferedImage img, String filename) throws IOException {
		FileOutputStream out = new FileOutputStream(filename);
		javax.imageio.ImageIO.write(img, "png", out);
		out.close();
	}

}