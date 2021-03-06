package engineTest;

import java.awt.Rectangle;

import engine.Display;
import util.swing.gride.XStrip;
import util.swing.gride.YStrip;

public class PSSeane extends Seane {

	private static final PSTeam[] teams = new PSTeam[9];

	public final static PSSeane inctance = new PSSeane();  

	private PSSeane() {
		setBackground("res/bg.jpg");
		
		orgenizeTeams(2);
	}
	
	public void setDisplay(Display display) {
		super.setDisplay(display);
		for (int i = 0; i<9; i++){
			if (teams[i] == null)
				break;
			teams[i].setDisplay(display);
		}
	}
	
	private static PSTeam getTeam(int index){
		if (teams[index] == null)
			teams[index] = new PSTeam();
		if (inctance != null && inctance.display != null)
			teams[index].setDisplay(inctance.display);
		teams[index].setVisible(true);
		return teams[index];
	}
	
	public static PSTeam orgenizeTeams(int amount) {
		YStrip root = new YStrip();
		
		if (amount > 9 || amount < 0)
			return null;
		
		// width is 3
		int i = 0;
		while (amount > 2 && amount != 4){
			XStrip x = new XStrip();
			x.append(getTeam(i++));
			x.append(getTeam(i++));
			x.append(getTeam(i++));
			root.append(x);
			amount -=3;
		}
		while (amount > 0 && amount %2 == 0){
			XStrip x = new XStrip();
			x.append(getTeam(i++));
			x.append(getTeam(i++));
			root.append(x);
			amount -=2;
		}
		if (amount > 0)
			root.append(getTeam(i++));
		
		root.setBounds(new Rectangle(-1000, -1000, 2000, 2000));
		
		for (; i<9; i++){
			if (teams[i] == null)
				break;
			teams[i].setVisible(false);
		}
		
		return null;
	}
}
