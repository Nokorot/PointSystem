package util.jly.objects;

import util.Window;
import util.jly.contObject;
import util.swing.NBTextField;

public class jlyTFeald {

	public static NBTextField Build(Window window, contObject o) {

		NBTextField tf = new NBTextField(window);
		if(o.getF("") != null)
			tf.setText(o.getF("").data);
		
		return tf;
	}

}
