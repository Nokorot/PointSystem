package util;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import util.adds.UpdateAdd;
import util.swing.Button;
import util.swing.ButtonSetings;
import util.swing.LabelSetings;
import util.swing.TextArea;
import util.swing.TextAreaSetings;
import util.swing.TextField;
import util.swing.TextFieldSetings;
import util.swing.gride.BoxGrid;

public class Window extends JFrame {
	private static final long serialVersionUID = 1L;

	private UpdateAdd upAdd;
	private List<UpdateAdd> upAdds = new ArrayList<UpdateAdd>();

	private static int widthFix;
	private static int heightFix;
	
	static {
		JFrame frame = new JFrame();
		
		frame.setSize(200, 200);
		frame.setVisible(true);
		widthFix = 200 - frame.getContentPane().getWidth();
		heightFix = 200 - frame.getContentPane().getHeight();
		frame.dispose();
	}

	private static Image Logo;
	private static int DefaultCloseOperation = 3;

	protected Platform platform;

	public JPanel panel;

	public ButtonSetings buttonSets;
	public TextFieldSetings textfeldSets;
	public LabelSetings labelSets;
	public TextAreaSetings textareaSets;
	
	protected boolean running;
	public boolean[] keys = new boolean[68300];
	protected Point wMouse;
	protected Point sMouse;

	
	/**
	 * 
	 * @param platform
	 * @param title
	 * @param width
	 * @param height
	 */
	public Window(Platform platform, String title, int width, int height) {
		if (platform == null) {
			System.err.println("Sorry, but your platform does not exist!");
			System.exit(1);
		}
		this.platform = platform;
		
		First();

		setDefaultCloseOperation(DefaultCloseOperation);
		setTitle(title);
		setSize(width, height);

		setLocationRelativeTo(null);
		
		if (Logo != null)
			setIconImage(Logo);

		setPanel();
		addListaners();

		platform.addWindow(this, this.upAdd = new UpdateAdd() {
			public void mUpdate() {
				Window.this.Update();
				Window.this.update();
				try {
					for (UpdateAdd u : Window.this.upAdds) {
						u.mUpdate();
					}
				} catch (Exception e) {
				}
			}

			public void mUpdate1() {
				Window.this.Update1();
				try {
					for (UpdateAdd u : Window.this.upAdds) {
						u.mUpdate1();
					}
				} catch (Exception e) {
				}

			}

		});
		Objects();
		setVisible(true);
	}
	
	public Window(String title, int width, int height) {
		this(Platform.staticInstance, title, width, height);
	}

	private void addListaners() {
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				Window.this.dispose();
			}

			public void windowOpened(WindowEvent paramWindowEvent) {
				Window.this.onOpened();
			}

		});
		addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent e) {
				Window.this.MouseWheelRotation(e.getWheelRotation());
			}

		});
		addMouseMotionListener(new MouseMotionListener() {
			public void mouseDragged(MouseEvent e) {
				Window.this.wMouse = e.getPoint();
				Window.this.sMouse = e.getLocationOnScreen();
			}

			public void mouseMoved(MouseEvent e) {
				Window.this.wMouse = e.getPoint();
				Window.this.sMouse = e.getLocationOnScreen();
			}

		});
		addMouseListener(new MouseListener() {
			public void mousePressed(MouseEvent e) {
				Window.this.MousePresed = true;
				Window.this.MouseClick(e.getButton());
			}

			public void mouseReleased(MouseEvent e) {
				Window.this.MousePresed = false;
				Window.this.MouseReleas(e.getButton());
			}

			public void mouseEntered(MouseEvent e) {
				Window.this.MouseEntered();
				Window.this.MouseInside = true;
			}

			public void mouseExited(MouseEvent e) {
				Window.this.MouseExited();
				Window.this.MouseInside = false;
			}

			public void mouseClicked(MouseEvent e) {
			}
		});
		addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent e) {
				Window.this.keys[e.getKeyCode()] = true;
				Window.this.KeyPressed(e.getKeyCode());
			}

			public void keyReleased(KeyEvent e) {
				Window.this.keys[e.getKeyCode()] = false;
				Window.this.KeyReleased(e.getKeyCode());
			}

			public void keyTyped(KeyEvent e) {
			}
		});
	}

	private void setPanel() {
		this.panel = new JPanel();
		this.panel.setSize(super.getWidth(), super.getHeight() + 27);
		this.panel.setLayout(null);
		add(this.panel);

		this.buttonSets = new ButtonSetings(this);
		this.textfeldSets = new TextFieldSetings(this);
		this.labelSets = new LabelSetings(this);
		this.textareaSets = new TextAreaSetings(this);
	}

	protected Insets gridInsets = new Insets(5, 5, 5, 5);

	protected BoxGrid getGrid(double[] X, double[] Y) {

		BoxGrid gb = new BoxGrid(new Rectangle(gridInsets.left, gridInsets.top, getWidth() - gridInsets.left
				- gridInsets.right - widthFix, getHeight() - gridInsets.top - gridInsets.bottom - heightFix),
				X, Y);
		addUpdate(new UpdateAdd() {
			public void mUpdate1() {
			}

			public void mUpdate() {
				gb.updateBounds(new Rectangle(gridInsets.left, gridInsets.top, getWidth() - gridInsets.left
						- gridInsets.right - widthFix, getHeight() - gridInsets.top - gridInsets.bottom
						- heightFix));
			}
		});
		return gb;
	}

	protected BoxGrid getGrid(int x, double[] Y) {
		return getGrid(BoxGrid.newD(x), Y);
	}

	protected BoxGrid getGrid(double[] X, int y) {
		return getGrid(X, BoxGrid.newD(y));
	}

	protected BoxGrid getGrid(int x, int y) {
		return getGrid(BoxGrid.newD(x), BoxGrid.newD(y));
	}

	protected void First() {
	}

	public void Objects() {
		BoxGrid grid = this.getGrid(1, new double[]{1, 1, 3});

		Button b;
		TextField tf;
		TextArea ta;
		
		b = new Button(this, "Button");
		tf = new TextField(this);
		tf.setHorizontalAlignment(0);
		tf.setText("TextField");
		ta = new TextArea(this, true);
		ta.setText("TextArea");
		
		grid.getBox(0).setComponent(b);
		grid.getBox(1).setComponent(tf);
		grid.getBox(2).setComponent(ta);
		
	/*
		int x = getWidth() / 10;
		int y = getHeight() / 10;
		Grid g1 = new Grid(new Rectangle(x, y, getWidth() - 2 * x, getHeight() - 2 * y), 1, 5, x / 2, false);

		new Button(this, "Button", g1.getRec(0));
		TextField tf = new TextField(this, g1.getRec(1));
		tf.setHorizontalAlignment(0);
		tf.setText("TextField");
		TextArea ta = new TextArea(this, g1.getRec(0, 2, 0, 4), true);
		ta.setText("TextArea");*/
	}

	protected boolean MousePresed;
	protected boolean MouseInside;
	long lastTime = System.nanoTime();

	private void update() {
		if (!this.MouseInside) {
			this.wMouse = new Point(-100, -100);
		}
	}

	public void Update() {
	}

	public void Update1() {
	}

	protected void onCloseing() {
	}

	protected void onOpened() {
	}

	protected void MouseClick(int b) {
	}

	protected void MouseReleas(int b) {
	}

	protected void MouseWheelRotation(int r) {
	}

	protected void KeyPressed(int key) {
	}

	protected void KeyReleased(int key) {
	}

	public void ButtonAction(Button button) {
		System.out.println(button);
	}

	public void TextFieldAction(TextField field) {
	}

	public void MouseExited() {
	}

	public void MouseEntered() {
	}

	public void MouseWheelRotated(int rotation) {
	}

	public void setSize(int width, int height) {
		super.setSize(width + widthFix, height + heightFix);
	}

	public void setSize(Dimension d) {
		setSize(d.width, d.height);
	}

	public void setWidth(int width) {
		setSize(width, getTheHeight());
	}

	public void setHeight(int height) {
		setSize(getTheWidth(), height);
	}

	public int getTheWidth() {
		return getWidth() - widthFix;
//		return getContentPane().getWidth();
	}

	public int getTheHeight() {
		return getHeight() - heightFix;
//		return getContentPane().getHeight();
	}

	public UpdateAdd getUpAdd() {
		return this.upAdd;
	}

	public List<UpdateAdd> getUpAdds() {
		return this.upAdds;
	}

	public Dimension getSize() {
		return new Dimension(getWidth(), getHeight());
	}

	public Rectangle getBounds() {
		return new Rectangle(getX(), getY(), getWidth(), getHeight());
	}

	public void dispose() {
		onCloseing();
		platform.removeWindows(this);
		this.upAdds.retainAll(this.upAdds);
		super.dispose();
	}

	public void packCanv() {
		int width = getWidth();
		int height = getHeight();

		super.pack();
		setSize(width, height);
	}

	public static void setStandardIcon(Image icon) {
		Logo = icon;
	}

	public static void setStadartDefaultCloseOperation(int defaultCloseOperation) {
		if ((defaultCloseOperation > 0) && (defaultCloseOperation < 3)) {
			DefaultCloseOperation = defaultCloseOperation;
		}
	}

	public void setPlatform(Platform platform) {
		this.platform = platform;
	}

	public void addUpdate(UpdateAdd upAdd) {
		this.upAdds.add(upAdd);
	}

	public Image[] ListToArrayImage(List<Image> list) {
		Image[] arr = new Image[list.size()];
		for (int i = 0; i < list.size(); i++) {
			arr[i] = ((Image) list.get(i));
		}
		return arr;
	}
	
}