package engineTest;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import util.Platform;

@SuppressWarnings("serial")
public class Main extends JFrame {

	private static LiveWindow liveWindow;
	
	private Main() {
		super("Side window");
		
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(200, 335));
		setContentPane(panel);
		pack();
		setResizable(false);
		setLayout(null);
		
		JButton hideButton = new JButton("Hide");
		hideButton.setBounds(5, 5, 190, 50);
		hideButton.addActionListener((ActionEvent e) -> {
			liveWindow.visible(false);
		});
		add(hideButton);
		JButton showButton = new JButton("Show");
		showButton.setBounds(5, 60, 190, 50);
		showButton.addActionListener((ActionEvent e) -> {
			liveWindow.visible(true);
		});
		add(showButton);

		JTextField X = new JTextField("0");
		X.setBounds(5, 115, 90, 50);
		add(X);
		JTextField Y = new JTextField("0");
		Y.setBounds(105, 115, 90, 50);
		add(Y);

		JTextField width = new JTextField("800");
		width.setBounds(5, 170, 90, 50);
		add(width);
		JTextField height = new JTextField("600");
		height.setBounds(105, 170, 90, 50);
		add(height);
		
		JButton setSizeB = new JButton("Set Bounds");
		setSizeB.setBounds(5, 225, 190, 50);
		setSizeB.addActionListener((ActionEvent e) -> {
			try{
				int x = Integer.parseInt(X.getText());
				int y = Integer.parseInt(Y.getText());
				int w = Integer.parseInt(width.getText());
				int h = Integer.parseInt(height.getText());
				liveWindow.setWindowLocation(x, y);
				liveWindow.setWindowSize(w, h);
			}catch(Exception ex){
			}
		});
		add(setSizeB);
		
		setVisible(true);
		
	}

	public static void main(String[] args) {
		
		liveWindow = new LiveWindow(){
			protected void init() {
				super.init();
				this.seane = PSSeane.inctance;
				this.seane.setDisplay(this);
			}
		};
		liveWindow.open();
		
		Main mainWindow = new Main();
		mainWindow.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		mainWindow.addWindowListener(new WindowListener() {
			public void windowOpened(WindowEvent e) {
			}
			public void windowIconified(WindowEvent e) {
			}
			public void windowDeiconified(WindowEvent e) {
			}
			public void windowDeactivated(WindowEvent e) {
			}
			public void windowClosing(WindowEvent e) {
				liveWindow.close();
				System.exit(0);
			}
			public void windowClosed(WindowEvent e) {
			}
			public void windowActivated(WindowEvent e) {
			}
		});
		
		new Platform();
		FontManager.Open(true);

		new Thread(() -> {
		while(true){
			if (PSTeam.fontMaterial == null){
				try {
					Thread.sleep(2);
				} catch (InterruptedException e1) {
				}
				continue;
			}
			System.out.println("Heyyy");
			FontManager.setFontMaterial(PSTeam.fontMaterial);
			break;
		}}).start();
			
	}


	
}