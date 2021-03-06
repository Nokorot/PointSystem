package engineTest;

import java.awt.Color;

import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;
import javax.swing.text.PlainDocument;

import fontRendering.FontMaterial;
import util.Window;
import util.swing.Label;
import util.swing.NBButton;
import util.swing.NBTextField;
import util.swing.gride.BoxObject;
import util.swing.gride.XStrip;
import util.swing.gride.YStrip;

public class FontManager extends Window {
	private static final long serialVersionUID = 1L;

	private static FontManager inctance;

	private FontMaterial material;

	private FontManager() {
		super("Font Manager", 500, 400);
	}

	public static void Open(boolean viwe) {
		if (inctance == null)
			inctance = new FontManager();

		inctance.setVisible(viwe);
	}

	public void Init() {
		// super.Init();

		panel2.buttonSets.background = Color.DARK_GRAY;

		panel2.textfeldSets.bColor = Color.DARK_GRAY;
		panel2.textfeldSets.tColor = Color.LIGHT_GRAY;

		float w = 1;

		YStrip root = new YStrip();

		XStrip x = new XStrip();
		XStrip xs = new XStrip();
		YStrip ys = new YStrip();
		x.append(new Label(this, "Color"));
		ys.append(new Slider(this, "R", 0, 255, 255));
		ys.append(new Slider(this, "G", 0, 255, 255));
		ys.append(new Slider(this, "B", 0, 255, 255));
		xs.append(ys);
		x.append(xs, w);
		root.append(x);

		x = new XStrip();
		xs = new XStrip();
		ys = new YStrip();
		x.append(new Label(this, "Border Color"));
//		xs.append(new Button(this, "", "bcolor"));
		ys.append(new Slider(this, "bR", 0, 255, 0));
		ys.append(new Slider(this, "bG", 0, 255, 0));
		ys.append(new Slider(this, "bB", 0, 255, 0));
		xs.append(ys);
		x.append(xs, w);
		root.append(x);

		x = new XStrip();
		xs = new XStrip();
		x.append(new Label(this, "Width"));
//		xs.append(new NumField(this, 0.5, "width"));
		xs.append(new Slider(this, "width", 35, 75, 50));
		x.append(xs, w);
		root.append(x);

		x = new XStrip();
		xs = new XStrip();
		x.append(new Label(this, "Border Width"));
//		xs.append(new NumField(this, 0.5, "bwidth"));
		xs.append(new Slider(this, "bwidth", 0, 20, 10));
		x.append(xs, w);
		root.append(x);

		x = new XStrip();
		xs = new XStrip();
		x.append(new Label(this, "DecayRate"));
//		xs.append(new NumField(this, 0.5, "decay"));
		xs.append(new Slider(this, "decay", 0, 1000, 50));
		x.append(xs, w);
		root.append(x);

		x = new XStrip();
		xs = new XStrip();
		x.append(new Label(this, "Border DecayRate"));
//		xs.append(new NumField(this, 0.5, "bdecay"));
		xs.append(new Slider(this, "bdecay", 35, 75, 50));
		x.append(xs, w);
		root.append(x);

		x = new XStrip();
		xs = new XStrip();
		x.append(new Label(this, "Offset"));
		xs.append(new NumField(this, 0.5, "xoff"));
		xs.append(new NumField(this, 0.5, "yoff"));
		x.append(xs, w);
		root.append(x);

		this.getFrameBox().setBoxObject(root);
	}

	public void ButtonAction(NBButton button) {
		super.ButtonAction(button);
	}

	public void TextFieldAction(NBTextField field) {
		if (field.code == null)
			return;

		switch (field.code) {
		case "width":
			this.material.width = ((NumField) field).getValue();
			break;
		case "bwidth":
			this.material.borderWidth = ((NumField) field).getValue();
			break;
		case "decay":
			this.material.decayRate = ((NumField) field).getValue();
			break;
		case "bdecay":
			this.material.borderDecayRate = ((NumField) field).getValue();
			break;
		case "xoff":
			this.material.borderOffset.x = ((NumField) field).getValue();
			break;
		case "yoff":
			this.material.borderOffset.y = ((NumField) field).getValue();
			break;
		}
	}
	
	public void SliderChange(JSlider jslider) {
		
		if (jslider == null)
			return;
		
		Slider slider = (Slider) jslider;
	
		if (slider.code == null)
			return;
		
		System.out.println(slider.getValue());
		
		switch (slider.code) {
		case "R": this.material.color.x = slider.getValue() / 255f; break;
		case "G": this.material.color.y = slider.getValue() / 255f; break;
		case "B": this.material.color.z = slider.getValue() / 255f; break;
		case "bR": this.material.borderColor.x = slider.getValue() / 255f; break;
		case "bG": this.material.borderColor.y = slider.getValue() / 255f; break;
		case "bB": this.material.borderColor.z = slider.getValue() / 255f; break;
		case "width": this.material.width = slider.getValue() / 100f; break;
		case "bwidth": this.material.borderWidth = slider.getValue() / 100f; break;
		case "decay": this.material.decayRate = slider.getValue() / 100f; break;
		case "bdecay": this.material.borderDecayRate = slider.getValue() / 100f; break;
		case "xoff": this.material.borderOffset.x = slider.getValue(); break;
		case "yoff": this.material.borderOffset.y = slider.getValue(); break;
		}
		
	}

	public static void setFontMaterial(FontMaterial material) {
		inctance.material = material;
	}

	static class NumField extends NBTextField {

		private float value;

		public NumField(Window window, double value, String code) {
			super(window, "" + value, code);
			this.value = (float) value;

			PlainDocument doc = (PlainDocument) this.getDocument();
			doc.setDocumentFilter(new MyIntFilter());
		}

		public float getValue() {
			return value;
		}

		class MyIntFilter extends DocumentFilter {
			@Override
			public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
					throws BadLocationException {

				Document doc = fb.getDocument();
				StringBuilder sb = new StringBuilder();
				sb.append(doc.getText(0, doc.getLength()));
				sb.insert(offset, string);

				if (test(sb.toString())) {
					super.insertString(fb, offset, string, attr);
				} else {
					// warn the user and don't allow the insert
				}
			}

			private boolean test(String text) {
				try {
					value = Float.parseFloat(text);
					return true;
				} catch (NumberFormatException e) {
					return false;
				}
			}

			@Override
			public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
					throws BadLocationException {

				Document doc = fb.getDocument();
				StringBuilder sb = new StringBuilder();
				sb.append(doc.getText(0, doc.getLength()));
				sb.replace(offset, offset + length, text);

				if (test(sb.toString())) {
					super.replace(fb, offset, length, text, attrs);
				} else {
					// warn the user and don't allow the insert
				}

			}

			@Override
			public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
				Document doc = fb.getDocument();
				StringBuilder sb = new StringBuilder();
				sb.append(doc.getText(0, doc.getLength()));
				sb.delete(offset, offset + length);

				if (test(sb.toString())) {
					super.remove(fb, offset, length);
				} else {
					// warn the user and don't allow the insert
				}

			}
		}

	}

	static class Slider extends JSlider implements BoxObject {
		
		private String code;
		private Window window;
		
		public Slider(Window window, String code, int min, int max, int value) {
			super(min, max, value);
			this.code = code;
			this.window = window;
			
			this.addChangeListener(new ChangeListener() {
				
				@Override
				public void stateChanged(ChangeEvent e) {
					window.SliderChange((JSlider) e.getSource());
				}
			});
			
			window.add(this);
		}

		@Override
		public void setPane(JPanel pane) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
}

