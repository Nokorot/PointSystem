package no.nokorot.pointsystem.Element;

import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.imageio.plugins.jpeg.JPEG;
import com.thecherno.raincloud.serialization.RCDatabase;

import javafx.print.PageOrientation;
import util.handelers.ImageHandeler;

public class ElementFile {

	String path;
	BufferedImage image;
	
	public ElementFile(String path, BufferedImage img) {
	}
	
	public static void main(String[] args) {
		
		String path = "/home/noko/Pictures/Wallpapers/1.jpg";
		BufferedImage img = ImageHandeler.loadGloab(path);
		
		RCDatabase db = new RCDatabase("/home/noko/Desctop/db.rcb");
		
		DataBuffer b = (DataBuffer) img.getData().getDataBuffer();
		
	}
	
}
