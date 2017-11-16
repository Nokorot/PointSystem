package engineTest;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import engine.Display;
import fontMeshCreator.GUIText;
import fontRendering.FontMaterial;
import fontRendering.TextMaster;
import util.swing.gride.BoxObject;
import utils.Vector2f;
import utils.Vector4f;

public class PSTeam extends LiveContent implements BoxObject {
	
	private final static float nameOf = -.3f;
	private final static float pointsOf = .1f;
	
	public static FontMaterial fontMaterial;
	
	private Vector2f pos;
	
	// TODO: private boolean visible;
	
	private GUIText text, pointsG;
	
	private String name;
	private int points;
	
	public PSTeam() {
		if (fontMaterial == null)
			createFont();
		
		System.out.println("hey");
		
		text = new GUIText();
		text.setText("Name");
		text.setFontSize(5);
		text.setFontMaterial(fontMaterial);
		text.setCenterText(true);
		
		pointsG = new GUIText();
		pointsG.setText("00");
		pointsG.setFontSize(8);
		pointsG.setFontMaterial(fontMaterial);
		pointsG.setCenterText(true);
		TextMaster.addText(pointsG);
		TextMaster.addText(text);
		
		this.makeHandeler();
	}
	
	public PSTeam(Display display, Vector2f pos, Vector2f size) {
		setPosition(pos);
		setSize(size);
	}
	
	public void setDisplay(Display display) {
		super.setDisplay(display);
		text.setDisplay(display);
		pointsG.setDisplay(display);
	}
	
	public void setName(String name) {
		this.name = name;
		this.text.setText(name);
	}
	
	public String getName() {
		return name;
	}
	
	public void setPoints(int points) {
		this.points = points;
		this.pointsG.setText(points + "");
	}

	public void addPoints(int i) {
		this.points += i;
		this.pointsG.setText(points + "");
	}
	
	private void createFont() {
		try {
			fontMaterial = FontMaterial.loadFile("res/fontMaterial/basic.psfm");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void setPosition(float x, float y){
		setPosition(new Vector2f(x, y));
	}
	
	public void setPosition(Vector2f pos){
		this.pos = pos;
		text.setPosition(pos);
		pointsG.setPosition(pos);
	}
	
	public void setSize(float x, float y){
		text.setLineMaxSize(x / 2);
		pointsG.setLineMaxSize(x / 2);

		float y0 = pos.y  +  y * nameOf;
		text.setPosition(new Vector2f(text.getPosition().x, y0));
		
		y0 = pos.y  +  y * pointsOf;
		pointsG.setPosition(new Vector2f(pointsG.getPosition().x, y0));
		
//		text.getPosition().y = pos.y  +  y * nameOf;
		pointsG.getPosition().y = pos.y  +  y * pointsOf;
	}
	
	public void setSize(Vector2f size) {
		text.setLineMaxSize(size.x / 2);
		pointsG.setLineMaxSize(size.x / 2);

		text.getPosition().y = pos.y + size.y * nameOf;
		pointsG.getPosition().y = pos.y + size.y * pointsOf;
		
//		text.setSize(size.mult(1, .4f));
//		pointsG.setSize(size.mult(1, .6f));
	}
	
	@Override
	public void setBounds(Rectangle rec) {
		setPosition((float) (rec.x + rec.width/2) / 1000, (float) (rec.y + rec.height/2) / 1000);
		setSize((float) rec.width / 1000 * 1.3f, (float) rec.height / 1000);
	}

	@Override
	public void setVisible(boolean visable) {
		text.setVisible(visable);
		pointsG.setVisible(visable);
	}
	
	
	private static JFrame frame;
	private static int index = 1;
	
	private void makeHandeler(){
		if (frame == null){
			frame = new JFrame();
			frame.setTitle("PSTeam Handler" + index);
			frame.getContentPane().setPreferredSize(new Dimension(300, 10 + 55*2 * 4));
			frame.getContentPane().setLayout(null);
			frame.pack();
			frame.setResizable(false);

			frame.setVisible(true);
			
			JTextField nField = new JTextField("name");
			nField.setBounds(5, 5, 290, 50);
			nField.addActionListener((ActionEvent e) -> {
				int amount = Integer.parseInt(nField.getText());
				PSSeane.orgenizeTeams(amount);
			});
			frame.add(nField);
			
			JTextField color = new JTextField("name");
			color.setBounds(5, 60, 290, 50);
			color.addActionListener((ActionEvent e) -> {
				try{Vector4f c = Vector4f.parse(color.getText());
					fontMaterial.color = c;
				}catch (Exception ex) {
				}
			});
			frame.add(color);
		}
		
		int yOff = 5 + 55*2 * index++; 
		
		JTextField nField = new JTextField("name");
		nField.setBounds(5, 5 + yOff, 290, 50);
		nField.addActionListener((ActionEvent e) -> {
			this.setName(nField.getText());
		});
		frame.add(nField);
		
		JTextField pField = new JTextField("" + points);
		pField.setBounds(100, 60 + yOff, 100, 50);
		pField.addActionListener((ActionEvent e) -> {
			try {
				int i = Integer.parseInt(pField.getText());
				this.setPoints(i);
			} catch (Exception ex){
				pField.setText("" + points);
			}
		});
		frame.add(pField);
		
		JButton sub = new JButton("-");
		sub.setBounds(5, 60 + yOff, 90, 50);
		sub.addActionListener((ActionEvent e) -> {
			this.addPoints(-1);
			pField.setText("" + points);
		});
		frame.add(sub);
		
		JButton add = new JButton("+");
		add.setBounds(205, 60 + yOff, 90, 50);
		add.addActionListener((ActionEvent e) -> {
			this.addPoints(1);
			pField.setText("" + points);
		});
		frame.add(add);
		
		frame.repaint();
		
	}

	@Override
	public void setPane(JPanel pane) {
		// TODO Auto-generated method stub
	}

	@Override
	public void repaint() {
		// TODO Auto-generated method stub
		
	}
}

