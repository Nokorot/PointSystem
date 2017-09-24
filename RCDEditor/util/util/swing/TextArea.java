package util.swing;

import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import util.Window;

public class TextArea extends JTextArea {
	private static final long serialVersionUID = 1L;
	private JScrollPane scrol = new JScrollPane();

	public static KeyPresed SPresed = new KeyPresed() {
		public void KeyPrese(int i) {
		}
	};

	public String code = "-1";

	public TextArea(Window window, Rectangle rec, String code, boolean editeable) {
		TextAreaSetings s = window.textareaSets;
		
		setBackground(s.bColor);
		setForeground(s.tColor);
		setFont(s.font);
		setBorder(s.border);
		setEditable(editeable);
		this.code = code;

		
		if(rec != null){
			super.setBounds(rec);
			this.scrol.setBounds(rec);
			this.scrol.setViewportView(this);
			
		}
		window.panel.add(this.scrol);
			
	}

	public TextArea(Window window, Rectangle rec, boolean editeable) {
		this(window, rec, null,editeable);
	}

	public TextArea(Window window, String code, boolean editeable) {
		this(window, null, code, editeable);
	}

	public TextArea(Window window, boolean editeable) {
		this(window, null, null, editeable);
	}
	
	@Override
	public void setBounds(Rectangle r) {
		super.setBounds(r);
		scrol.setBounds(r);
		scrol.setViewportView(this);
		
	}
	
	public void setKeyPrese(KeyPresed pressed){
		addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent e) {
				KeyPresed.keys[e.getKeyCode()] = true;
				pressed.KeyPrese(e.getKeyCode());
			}

			public void keyReleased(KeyEvent e) {
				KeyPresed.keys[e.getKeyCode()] = false;
			}

			public void keyTyped(KeyEvent e) {
			}
		});
	}

	public void setFontSize(int size) {
		Font f = getFont();
		setFont(new Font(f.getName(), f.getStyle(), size));
	}

}