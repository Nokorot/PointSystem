package util.swing;

import java.awt.Font;
import java.awt.Rectangle;

import javax.swing.JTextField;

import util.Window;

public class TextField extends JTextField {
	private static final long serialVersionUID = 1L;
	public String code = "-1";

	public int MAX_SIZE;
	public Window window;
	private TextFieldSetings s;

	public TextField(Window window, Rectangle rec, String code) {
		TextFieldSetings s = window.textfeldSets;

		if (rec != null)
			setBounds(rec);

		this.window = window;
		if (code != null)
			this.code = code;
		setBackground(s.bColor);
		setForeground(s.tColor);
		setFont(s.font);
		setBorder(s.border);
		addActionListener(s.pListener);
		setHorizontalAlignment(s.HorizontalAlignment);
		window.panel.add(this);
	}

	public TextField(Window window, Rectangle rec) {
		this(window, rec, null);
	}

	public TextField(Window window, String code) {
		this(window, null, code);
	}

	public TextField(Window window) {
		this(window, null, null);
	}

	public void setFonrSize(int size) {
		Font f = getFont();
		setFont(new Font(f.getName(), f.getStyle(), size));
	}
}