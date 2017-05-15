package no.nokorot.pointsystem.Windows;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JPanel;

import util.Window;
import util.handelers.ImageHandeler.ScaleType;
import util.math.Maths;
import util.swing.Label;
import util.swing.NBButton;
import util.swing.gride.BoxObject;
import util.swing.gride.XStrip;
import util.swing.gride.YStrip;

public class ColorSelector {

//	private ImageList colorList;
	
	private Window window;
	
	private Color currentColor;
	private Label current;
	
	public ColorSelector(String title){
		window = new Window(title, 300, 400){
			private static final long serialVersionUID = 1L;

			public void Init() {
				setMinimumSize(new Dimension(200, 300));
				XStrip root = new XStrip();
				
				YStrip y = new YStrip();
				y.append(new ColorField(this){
					protected void onColorSelected(int color) {
						setCurrentColor(new Color(color));
					}
				}, 5);
				
				current = new Label(this);
				current.setBackground(currentColor);
				current.setOpaque(true);
				y.append(current);
				
				root.append(y);
				
//				colorList = new ImageList(this, "");
//				root.append(colorList);
				
				this.getFrameBox().setBoxObject(root);
			}
		};
	}
	
	public void Open(){
		window.setVisible(true);
	}
	
	public void Close(){
		window.setVisible(false);
	}

	public void setCurrentColor(Color color){
		currentColor = color;
		current.setBackground(color);
		onColorChange(color);
	}
	
	public Color getCurrentColor() {
		return currentColor;
	}
	
	protected void onColorChange(Color color){
	}
	
	public boolean isOpen() {
		return window.isVisible();
	}
	
	private class ColorField implements BoxObject {
		
		private YStrip root;
		private Label fieldL, gliderL;
		
		private BufferedImage field, glider;
		private int[] Fpixels, Gpixels;
		private boolean buttonDown;
		
		private int mode = 0;
		private int glidvalue = 0;
		
		public ColorField(Window window) {
			root = new YStrip();
			
			XStrip x = new XStrip();
			x.append(new NBButton(window, "Red"){
				private static final long serialVersionUID = 1L;

				protected void onAction() {
					mode = 0; render();
				}
			});
			x.append(new NBButton(window, "Green"){
				private static final long serialVersionUID = 1L;

				protected void onAction() {
					mode = 1; render();
				}
			});
			x.append(new NBButton(window, "Blue"){
				private static final long serialVersionUID = 1L;

				protected void onAction() {
					mode = 2; render();
				}
			});
			root.append(x);
			
			x = new XStrip();
			
			fieldL = new Label(window);

			field = new BufferedImage(256, 256, BufferedImage.TYPE_INT_RGB);
			glider = new BufferedImage(1, 256, BufferedImage.TYPE_INT_RGB);
			Fpixels = ((DataBufferInt) field.getRaster().getDataBuffer()).getData();
			Gpixels = ((DataBufferInt) glider.getRaster().getDataBuffer()).getData();
			fieldL.setIcon(field, ScaleType.STREKK);
			fieldL.addMouseMotionListener(new MouseMotionListener() {
				public void mouseMoved(MouseEvent e) {
					MouseMove(e.getX(), e.getY(), fieldL);
				}
				public void mouseDragged(MouseEvent e) {
					MouseMove(e.getX(), e.getY(), fieldL);
				}
			});;
			fieldL.addMouseListener(new MouseListener() {
				public void mouseReleased(MouseEvent e) {
					buttonDown = false;
				}
				public void mousePressed(MouseEvent e) {
					buttonDown = true;
					MouseAction(e.getX(), e.getY(), fieldL);
				}
				public void mouseExited(MouseEvent e) {
				}
				public void mouseEntered(MouseEvent e) {
				}
				public void mouseClicked(MouseEvent e) {
				}
			});
			x.append(fieldL, 8);
			
			gliderL = new Label(window);
			gliderL.setIcon(glider, ScaleType.STREKK);
			gliderL.addMouseMotionListener(new MouseMotionListener() {
				public void mouseMoved(MouseEvent e) {
					MouseMove(e.getX(), e.getY(), gliderL);
				}
				public void mouseDragged(MouseEvent e) {
					MouseMove(e.getX(), e.getY(), gliderL);
				}
			});;
			gliderL.addMouseListener(new MouseListener() {
				public void mouseReleased(MouseEvent e) {
					buttonDown = false;
				}
				public void mousePressed(MouseEvent e) {
					buttonDown = true;
					MouseAction(e.getX(), e.getY(), gliderL);
				}
				public void mouseExited(MouseEvent e) {
				}
				public void mouseEntered(MouseEvent e) {
				}
				public void mouseClicked(MouseEvent e) {
				}
			});
			
			x.append(gliderL, 1);
			
			root.append(x, 3);
			
			render();
		}
		
		private void render(){
			int g = 0;
			switch (mode) {
			case 0: g = (glidvalue << 16); break;
			case 1: g = (glidvalue <<  8); break;
			case 2: g = (glidvalue <<  0); break;
			}
			for (int y = 0; y < 256; y++){
				for (int x = 0; x < 256; x++){
					switch (mode) {
					case 0: Fpixels[x + y * 256] = g | (x <<  8) | (y << 0); break;
					case 1: Fpixels[x + y * 256] = g | (x << 16) | (y << 0); break;
					case 2: Fpixels[x + y * 256] = g | (x << 16) | (y << 8); break;
					}
				}
			}
			fieldL.setIcon(field, ScaleType.STREKK);
			
			for (int y = 0; y < 256; y++){
				switch (mode) {
				case 0: Gpixels[y] = (y << 16); break;
				case 1: Gpixels[y] = (y <<  8); break;
				case 2: Gpixels[y] = (y <<  0); break;
				}
			}
			Gpixels[glidvalue] = 0xffffff;
			if (glidvalue < 255)
				Gpixels[glidvalue + 1] = 0;
			if (glidvalue > 0)
				Gpixels[glidvalue - 1] = 0;
			
			gliderL.setIcon(glider, ScaleType.STREKK);
		}
		
		private void MouseMove(int x, int y, Label label){
			if (buttonDown)
				MouseAction(x, y, label);
		}
		
		private void MouseAction(int x, int y, Label label){
			if (label == gliderL){
				glidvalue = Maths.clamp(y * 255 / label.getHeight(), 0, 255);
				render();
			}else if (label == fieldL){
				x = Maths.clamp(x * 255 / label.getWidth(), 0, 255);
				y = Maths.clamp(y * 255 / label.getHeight(), 0, 255);
				onColorSelected(Fpixels[x + y * 256]);
			}
		}
		
		protected void onColorSelected(int color){
			System.out.println(color);
		}
		
		public void setBounds(Rectangle rectangle) {
			root.setBounds(rectangle);
		}

		public void setVisible(boolean visable) {
			root.setVisible(visable);
		}

		public void setPane(JPanel pane) {
			root.setPane(pane);
		}
		
		
	}
	
}
