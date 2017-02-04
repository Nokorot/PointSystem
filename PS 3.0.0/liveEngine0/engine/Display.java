package engine;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwGetWindowSize;
import static org.lwjgl.glfw.GLFW.glfwHideWindow;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwSetFramebufferSizeCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwSetWindowSize;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.awt.Color;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

public abstract class Display {
	
	public static float aspect = 1;
	
	private int width = 300;
	private int height = 300;

	private long window;
	private Thread liveThread;
	private boolean running = false;
	private boolean close = false;

	public static interface Operation {
		public void handel();
	}
	
//	private class OperationKey {
//		
//		private Object object;
//		private String 
//		
//		@Override
//		public int hashCode() {
//			return super.hashCode();
//		}
//	}

//	private List<Operation> operations = new ArrayList<Operation>();
	private Map<String, Operation> operations = new HashMap<>();

	public Display() {
	}
	
	public Display(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public void open() {
		if (running)
			return;

		liveThread = new Thread(() -> {
			build();
			loop();
			terminate();
		});
		liveThread.start();
	}

	public void build() {
		GLFWErrorCallback.createPrint(System.err).set();

		if (!glfwInit())
			throw new IllegalStateException("Unable to initialize GLFW");

		windowHints();

		window = glfwCreateWindow(width, height, "Live Window", NULL, NULL);
		if (window == NULL)
			throw new RuntimeException("Failed to create the GLFW window");

		/*
		 * glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
		 * if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
		 * glfwSetWindowShouldClose(window, true); // We will detect this // in
		 * the rendering loop });
		 */
		
		glfwSetFramebufferSizeCallback(window, (long window, int width, int height) -> {
			// System.out.println(width + " " + height);
			glViewport(0, 0, width, height);
			aspect = (float) width / height;
		});

		try (MemoryStack stack = stackPush()) {
			IntBuffer pWidth = stack.mallocInt(1); // int*
			IntBuffer pHeight = stack.mallocInt(1); // int*

			glfwGetWindowSize(window, pWidth, pHeight);

			System.out.println(glfwGetPrimaryMonitor());
			
			GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

			glfwSetWindowPos(window, (vidmode.width() - pWidth.get(0)) / 2,
					(vidmode.height() - pHeight.get(0)) / 2);
		}

		glfwMakeContextCurrent(window);
		glfwSwapInterval(1);
	}

	public void close() {
		close = true;
		try {
			liveThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	protected abstract void init();

	// Overrideables
	protected void windowHints() {
		glfwDefaultWindowHints(); // optional, the current window hints are
									// already the default
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden
													// after creation
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be
													// resizable
	}

	protected abstract void update();

	protected abstract void render();

	public void addOperation(String key, Operation operation){
		operations.put(key, operation);
	}

	public void addOperation(Object object, String key, Operation operation){
		operations.put(object.hashCode() + key, operation);
	}
	
	public void visible(boolean b) {
		addOperation(this, "visible", () -> {
			if (b)
				glfwShowWindow(window);
			else
				glfwHideWindow(window);
		});
	}

	public void setWindowSize(int width, int height) {
		this.width = width;
		this.height = height;
		aspect = (float) this.width / this.height;
		addOperation(this, "setWindowSize", () -> {
			glfwSetWindowSize(window, width, height);
			glViewport(0, 0, width, height);
		});
	}

	public void setWindowLocation(int x, int y) {
		addOperation(this, "setWindowLocation", () -> {
			glfwSetWindowPos(window, x, y);
		});
	}

	public void setBounds(int x, int y, int width, int height) {
		this.setWindowLocation(x, y);
		this.setWindowSize(width, height);
	}

	public void setBackground(Color color) {
		addOperation(this, "setDisplayBackground", () -> {
			glClearColor(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, 1);
		});
	}

	public float getAspectratio() {
		return this.width / this.height;
	}
	
	private void handelInteraction() {
		for (Operation o : operations.values())
			o.handel();
		operations.clear();
	}

	private void loop() {
		// This line is critical for LWJGL's interoperation with GLFW's
		// OpenGL context, or any context that is managed externally.
		// LWJGL detects the context that is current in the current thread,
		// creates the GLCapabilities instance and makes the OpenGL
		// bindings available for use.
		GL.createCapabilities();

		// Set the clear color
		glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

		init();

		// Run the rendering loop until the user has attempted to close
		// the window or has pressed the ESCAPE key.
		while (!glfwWindowShouldClose(window) && !close) {
			handelInteraction();

			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the
			// framebuffer
			update();
			render();

			glfwSwapBuffers(window); // swap the color buffers

			// Poll for window events. The key callback above will only be
			// invoked during this call.
			glfwPollEvents();
		}
	}

	private void terminate() {
		// Free the window callbacks and destroy the window
		glfwFreeCallbacks(window);
		glfwDestroyWindow(window);

		// Terminate GLFW and free the error callback
		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}

}