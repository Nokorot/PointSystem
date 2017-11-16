package no.nokorot.pointsystem.Element;

import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JLabel;

import no.nokorot.pointsystem.Windows.LiveWindow;
import no.nokorot.pointsystem.utils.FontObject;
import util.jly.objects.Body;
import util.swing.Label;

public class NamedTeam extends LiveWindow.Element {

	private static float nameHeight = 0.25f, overlapp = 0.07f;

	private Team team;
	
	private JLabel name, pointsLabel;

	private FontObject nameFont, pointsFont;
	
	private float x, y, width, height;

	public NamedTeam(Label l, Team team, float x, float y, float width, float height) {
		name = new JLabel(){
			private static final long serialVersionUID = 1L;

			protected void paintComponent(Graphics g) {
				Rectangle bounds = new Rectangle((int)(getWidth()*0.85), (int)(getHeight()*0.85));
				bounds.x = bounds.y = 0;
				nameFont.drawString(g, this.getText(), bounds);
			}
		};
		l.add(name);

		pointsLabel = new JLabel(){
			private static final long serialVersionUID = 1L;

			protected void paintComponent(Graphics g) {
				Rectangle bounds = new Rectangle((int)(getWidth()*0.75), (int)(getHeight()*0.75));
				bounds.x = bounds.y = 0;
				pointsFont.drawString(g, this.getText(), bounds);		
			}
		};
		l.add(pointsLabel);

		this.team = team;
		team.label = this;

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

	public void setPoints(String text) {
		pointsLabel.setText(text);
	}

	public void setName(String name) {
		this.name.setText(name);	
	}

	public void update() {
		int x = (int) (this.x * LiveWindow.getWidth());
		int y = (int) (this.y * LiveWindow.getHeight());

		int width = (int) (this.width *  LiveWindow.getWidth());
		int height = (int) (this.height * LiveWindow.getHeight());

		name.setLocation(x, y);
		name.setSize(width, (int) (height * nameHeight));
		nameFont = team.fontName;
		name.setText(team.name);
		
		pointsLabel.setLocation(x, y + (int) (height * (nameHeight - overlapp)));
		pointsLabel.setSize(width, (int) (height * (1 - nameHeight + overlapp)));
		pointsFont = team.fontPoints;
		pointsLabel.setText(team.stringedP());
	}

	public void setVisible(boolean visible) {
		name.setVisible(visible);
		pointsLabel.setVisible(visible);
	}

	@Override
	public void render(Graphics g) {
		//name.paintComponents(g);
//		if (name == null || nameFont == null)
//			return;

//		nameFont.drawString(g, name.getText(), name.getBounds());
//		pointsFont.drawString(g, pointsLabel.getText(), pointsLabel.getBounds());
	}

}
