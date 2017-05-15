import util.Platform;
import util.Window;
import util.swing.TextArea;
import util.swing.gride.BoxGrid;

import com.thecherno.raincloud.serialization.RCDatabase;


public class MainHey {
	
	private static String text;
	
	public MainHey() {
		setUpWindow();
		
		Platform.staticInstance.start();
	}
	
	private void setUpWindow() {
		new Window("RCDEditor ", 500, 600){
			private static final long serialVersionUID = 1L;
			
			@Override
			public void Objects() {
				BoxGrid g = this.getGrid(1, 1);
				
				TextArea ta = new TextArea(this, false);
				
				ta.setText(text);
				
				g.getBox(0).setComponent(ta);
			}
			
		};
	}

	public static void main(String[] args) {

		if(args.length < 1)
			text = "no file is choosen!";
		else{
			RCDatabase data = RCDatabase.DeserializeFromFile(args[0]);
			if(data != null)
				text = data.toString();
			else
				text = "could not deserialize \"" + args[0] + "\"";
		}
		
		new MainHey();
	}

}
