package util.jly.objects;

import util.Window;
import util.jly.contObject;
import util.swing.Button;

public class jlyButton {

	public static Button Build(Window window, contObject o) {
		
		Button b = new Button(window);
		b.setText(o.getF("").data);
		
		return b;
	}

}
