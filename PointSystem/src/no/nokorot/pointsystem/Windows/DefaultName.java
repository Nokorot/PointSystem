package no.nokorot.pointsystem.Windows;

import java.util.ArrayList;
import java.util.List;

import util.Window;
import util.swing.Button;
import util.swing.TextField;
import util.swing.gride.XStrip;
import util.swing.gride.YStrip;

public class DefaultName extends Window {
	private static final long serialVersionUID = 1L;

	static List<String[]> lists = new ArrayList<>();
	
	static DefaultName object;
	static TextField dName;
	static String text;

	private DefaultName() {
		super("Set Default Names", 260, 140);

		YStrip root = new YStrip();

		XStrip addons = new XStrip();
		addons.append(new Button(this, "index", "index"));
		addons.append(new Button(this, "char", "char"));
		addons.append(new Button(this, "rome", "rome"));
		root.append(addons);

		root.append(dName = new TextField(this, text, "dName"));

		XStrip x = new XStrip();
		x.append(new Button(this, "cansel", "cansel"));
		x.append(new Button(this, "done", "done"));
		root.append(x);

		setVisible(true);
		getFrameBox().setInsets(10);
		getFrameBox().setBoxObject(root);
	}
	
	static void Open() {
		if (object == null)
			object = new DefaultName();
		object.setVisible(true);
	}

	@Override
	public void ButtonAction(Button button) {
		switch (button.code) {
		case "index":
			dName.setText(dName.getText() + "%i");
			break;
		case "char":
			dName.setText(dName.getText() + "%a");
			break;
		case "rome":
			dName.setText(dName.getText() + "%r");
			break;
		case "cansel":
			this.setVisible(false);
			return;
		case "done":
			this.setVisible(false);
		}
		
		if (getName(1) == null){
			System.err.println("Text is not valid!!!");
			return;
		}
		text = dName.getText();
	}
	
	static String getName(int i) {
		try {
			String s = text;
			
			int j = s.indexOf('{');
			int k = s.indexOf('}', j);
			while (j > 0){
				String sub = s.substring(j, k+1);
				String content = s.substring(j+1, k);
				s = s.replace(sub, content.split(",")[i]);
				j = s.indexOf('{');
				k = s.indexOf('}', j);
			}

			s = s.replace("%r", 
					(new String[]{"i", "ii", "iii", "iv", "v", "vi", "vii", "viii", "ix", "x"})[i]);
			
			s = s.replace("%a", "abcdefghijklmn".charAt(i) + "");
			
			return s.replace("%i", "" + (i+1));
		}catch (Exception e){
			return null;
		}
		
	}

}