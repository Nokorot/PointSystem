package util;

import java.util.ArrayList;
import java.util.List;

import util.adds.UpdateAdd;

public class Platform implements Runnable {
	private List<Window> windows = new ArrayList<Window>();
	private List<UpdateAdd> upAdds = new ArrayList<UpdateAdd>();

	Thread thread = new Thread(this);
	boolean running = false;
	
	
//	Therm therm = new Therm();

	public static long time = 0L;
	public static boolean print = true;

	public static String resLoc = "";
	public static Platform staticInstance = new Platform();

	public Platform() {
		String OSName = System.getProperty("os.name");
		System.out.println(OSName);
		
		String localLoc = "";
		try {
			localLoc = this.getClass().getPackage().getName().toString();
		} catch (Exception e) {
		}

		resLoc = this.getClass().getResource("").toString();
		resLoc = resLoc.substring(resLoc.indexOf("/") + 1, resLoc.length() - localLoc.length() - 1);

		System.out.println(resLoc);
	}

	long lastTime = System.nanoTime();

	public void run() {
		while (this.running) {
			for (int i = 0; i < this.upAdds.size(); i++) {
				try {
					for (UpdateAdd u : this.upAdds) {
						update();
						u.mUpdate();
						if (System.nanoTime() - this.lastTime > 1000000000L) {
							u.mUpdate1();
							this.lastTime = System.nanoTime();
						}
					}
				} catch (Exception localException) {
				}
			}
		}
	}

	public void update() {
		time = System.currentTimeMillis();
	}

	public void start() {
		this.running = true;
		this.thread.start();
	}

	public void stop() {
		this.running = false;
		try {
			this.thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void addWindow(Window w, UpdateAdd upAdd) {
		this.windows.add(w);
		this.upAdds.add(upAdd);
	}

	public void addUpdate(UpdateAdd upAdd) {
		this.upAdds.add(upAdd);
	}

	public void removeWindows(Window window) {
		this.windows.remove(window);
		this.upAdds.remove(window.getUpAdd());
	}
}