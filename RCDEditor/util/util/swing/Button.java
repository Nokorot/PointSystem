package util.swing;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.JButton;

import util.Window;

public class Button extends JButton {
	private static final long serialVersionUID = 1L;
	public boolean activated = false;
	public String code = "-1";

	public Window window;
	
	protected CompPaint s_paint;
	protected CompContainsOveride s_contains;

	public Button(Window window, String text, Rectangle rec, String code) {
		ButtonSetings s = window.buttonSets;

		if (rec == null)
			rec = new Rectangle();
		if (code == null)
			code = "-1";

		this.window = window;
		this.code = code;
		setText(text);
		setBackground(s.bColor);
		setForeground(s.tColor);
		setFont(s.font);
		setBorder(s.border);
		setBounds(rec);
		addActionListener(s.pListener);
		window.panel.add(this);

		setContentAreaFilled(s.contentAreaFilled);
		this.s_paint = s.paint;
		this.s_contains = s.contains;
		addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Action();
			}
		});
	}

	public Button(Window window, String text, Rectangle rec) {
		this(window, text, rec, null);
	}

	public Button(Window window, String text, String code) {
		this(window, text, null, code);
	}

	public Button(Window window, String text) {
		this(window, text, null, null);
	}

	public Button(Window window, Icon icon, Rectangle rec, String code) {
		this(window, "", rec, code);
		if (icon == null)
			icon = window.buttonSets.icon;
		setIcon(icon);
	}

	public Button(Window window, Icon icon, Rectangle rec) {
		this(window, icon, rec, null);
	}

	public Button(Window window, Icon icon, String code) {
		this(window, icon, null, code);
	}

	public Button(Window window, Icon icon) {
		this(window, icon, null, null);
	}
	
	protected void Action(){
	}

	protected void paintComponent(Graphics g) {
		if(s_paint != null)
			s_paint.paintComponent(this, g);
		super.paintComponent(g);
	}

	public boolean contains(int x, int y) {
		if(s_contains != null)
			return s_contains.contains(this, x, y);
		return super.contains(x, y);
	}

	public void setBackground(Color c) {
		if (c == null)
			c = window.buttonSets.bColor;
		super.setBackground(c);
	}

	public void setFontSize(int size) {
		Font f = getFont();
		setFont(new Font(f.getName(), f.getStyle(), size));
	}

	public String toString() {
		return "Button [Code=" + this.code + ", Text=" + getText() + ", Bounds=" + getBounds() + ", Window="
				+ this.window.getTitle() + "]";
	}

	public CompPaint getS_paint() {
		return s_paint;
	}

	public void setS_paint(CompPaint s_paint) {
		this.s_paint = s_paint;
	}

	public CompContainsOveride getS_contains() {
		return s_contains;
	}

	public void setS_contains(CompContainsOveride s_contains) {
		this.s_contains = s_contains;
	}
	
}