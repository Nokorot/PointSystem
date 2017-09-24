package no.nokorot.pointsystem.Element;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JLabel;

import no.nokorot.pointsystem.PSData;
import no.nokorot.pointsystem.Windows.LiveWindow;
import no.nokorot.pointsystem.utils.FontObject;
import util.swing.Label;

public class NamedTeam extends LiveWindow.Element {

	private static float nameHeight = 0.25f, overlapp = 0.07f;

	private Team team;

	private JLabel name, pointsLabel;

	private float x, y, width, height;

	public NamedTeam(Label l, Team team, float x, float y, float width, float height) {
		name = new JLabel();
		name.setHorizontalAlignment(JLabel.CENTER);
		name.setVerticalAlignment(JLabel.CENTER);
		l.add(name);

		pointsLabel = new JLabel();
		pointsLabel.setHorizontalAlignment(JLabel.CENTER);
		pointsLabel.setVerticalAlignment(JLabel.CENTER);
		l.add(pointsLabel);

		this.team = team;

		setRealatieBounds(x, y, width, height);
	}

	public NamedTeam(Label l, Team team, Rectangle r100) {
		this(l, team, r100.x / 100f, r100.y / 100f, r100.width / 100f, r100.height / 100f);
	}
	
	public void setRealatieBounds(float x, float y, float width, float height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public void setRealatieBounds(double x, double y, double width, double height) {
		this.x = (float) x;
		this.y = (float) y;
		this.width = (float) width;
		this.height = (float) height;
	}

	public void update() {
		int x = (int) (this.x * LiveWindow.getWidth());
		int y = (int) (this.y * LiveWindow.getHeight());

		int width = (int) (this.width *  LiveWindow.getWidth());
		int height = (int) (this.height * LiveWindow.getHeight());

		name.setLocation(x, y);
		name.setSize(width, (int) (height * nameHeight));
		name.setForeground(team.fontName.getColor());
		name.setText(team.name);
		setFontSize(name, team.fontName);
		
		pointsLabel.setLocation(x, y + (int) (height * (nameHeight - overlapp)));
		pointsLabel.setSize(width, (int) (height * (1 - nameHeight + overlapp)));
		pointsLabel.setForeground(team.fontPoints.getColor());
		pointsLabel.setText(team.stringedP());
		setFontSize(pointsLabel, team.fontPoints);
	}

	public void setVisible(boolean visible) {
		name.setVisible(visible);
		pointsLabel.setVisible(visible);
	}

	private void setFontSize(JLabel l, FontObject fontName){
		int size = fontName.getFittingFontSize(l.getWidth(), l.getHeight(), l.getText());
		l.setFont(new Font(fontName.getFontname(), fontName.getStyle(), size));
	}

	@Override
	public void render(Graphics g) {
		name.repaint();
	}

}
