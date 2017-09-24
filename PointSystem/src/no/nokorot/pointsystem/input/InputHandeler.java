package no.nokorot.pointsystem.input;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class InputHandeler implements KeyListener, FocusListener, MouseListener, MouseMotionListener {
	public boolean[] key = new boolean[68836];
	public static int MouseX;
	public static int MouseY;

	public void mouseDragged(MouseEvent e) {
	}

	public void mouseMoved(MouseEvent e) {
		MouseX = e.getX();
		MouseY = e.getY();
	}

	public void mouseClicked(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}

	public void focusGained(FocusEvent e) {
	}

	public void focusLost(FocusEvent e) {
		for (int i = 0; i < this.key.length; i++) {
			this.key[i] = false;
		}
	}

	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		if ((keyCode > 0) && (keyCode < this.key.length)) {
			this.key[keyCode] = true;
		}
	}

	public void keyReleased(KeyEvent e) {
		int keyCode = e.getKeyCode();
		if ((keyCode > 0) && (keyCode < this.key.length)) {
			this.key[keyCode] = false;
		}
	}

	public void keyTyped(KeyEvent e) {
	}
}
