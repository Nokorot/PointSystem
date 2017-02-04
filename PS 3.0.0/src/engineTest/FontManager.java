package engineTest;

import util.Window;

public class FontManager extends Window {
	private static final long serialVersionUID = 1L;
	
	private static FontManager inctance;
	
	private FontManager(){
		super("Font Manager", 500, 500);
	}
	
	public static void Open(boolean viwe) {
		if (inctance == null)
			inctance = new FontManager();
		
		inctance.setVisible(viwe);
	}
	
	public void Init() {
		super.Init();
	}
	

}