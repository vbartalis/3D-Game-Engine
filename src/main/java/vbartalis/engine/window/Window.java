package vbartalis.engine.window;

import lombok.Getter;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {

	@Getter
	private final long id;
	private final Sync frameSync;
	@Getter
	private int pixelWidth, pixelHeight;
	private int desiredWidth, desiredHeight;
	@Getter
	private int screenCoordsWidth, screenCoordsHeight;
	@Getter
	private boolean fullscreen;
	@Getter
	private boolean vsync;
	@Getter
	private int fps;

	private List<WindowSizeListener> listeners = new ArrayList<WindowSizeListener>();

	public static WindowBuilder newWindow(int width, int height, String title) {
		return new WindowBuilder(width, height, title);
	}

	protected Window(long id, int desiredWidth, int desiredHeight, int fps, boolean fullscreen, boolean vsync) {
		this.id = id;
		this.desiredWidth = desiredWidth;
		this.desiredHeight = desiredHeight;
		this.fullscreen = fullscreen;
		this.vsync = vsync;
		this.fps = fps;
		this.frameSync = new Sync(fps);
		getInitialWindowSizes();
		addScreenSizeListener();
		addPixelSizeListener();
	}

	public float getAspectRatio() {
		return (float) pixelWidth / pixelHeight;
	}

	public void setFps(int fps) {
		frameSync.setFps(fps);
	}

	public void setVsync(boolean vsync) {
		this.vsync = vsync;
		glfwSwapInterval(vsync ? 1 : 0);
	}

	public void addSizeChangeListener(WindowSizeListener listener) {
		listeners.add(listener);
	}

	public void update() {
		glfwSwapBuffers(id);
		glfwPollEvents();
		frameSync.sync();
	}

	public boolean closeButtonPressed() {
		return glfwWindowShouldClose(id);
	}

	public void destroy() {
		glfwFreeCallbacks(id);
		glfwDestroyWindow(id);
		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}



	public void goFullScreen(boolean fullscreen) {
		long monitor = glfwGetPrimaryMonitor();
		GLFWVidMode vidMode = glfwGetVideoMode(monitor);
		if (fullscreen) {
			switchToFullScreen(monitor, vidMode);
		} else {
			switchToWindowed(vidMode);
		}
		this.fullscreen = fullscreen;
	}

	private void switchToFullScreen(long monitor, GLFWVidMode vidMode) {
		this.desiredWidth = screenCoordsWidth;
		this.desiredHeight = screenCoordsHeight;
		glfwSetWindowMonitor(id, monitor, 0, 0, vidMode.width(), vidMode.height(), vidMode.refreshRate());
		glfwSwapInterval(vsync ? 1 : 0);
	}

	private void switchToWindowed(GLFWVidMode vidMode) {
		glfwSetWindowMonitor(id, NULL, 0, 0, desiredWidth, desiredHeight, vidMode.refreshRate());
		glfwSetWindowPos(id, (vidMode.width() - desiredWidth) / 2, (vidMode.height() - desiredHeight) / 2);
	}

	private void addScreenSizeListener() {
		glfwSetWindowSizeCallback(id, (window, width, height) -> {
			if (validSizeChange(width, height, screenCoordsWidth, screenCoordsHeight)) {
				this.screenCoordsWidth = width;
				this.screenCoordsHeight = height;
			}
		});
	}

	private void addPixelSizeListener() {
		GLFW.glfwSetFramebufferSizeCallback(id, (window, width, height) -> {
			if (validSizeChange(width, height, pixelWidth, pixelHeight)) {
				this.pixelWidth = width;
				this.pixelHeight = height;
				notifyListeners();
			}
		});
	}

	private void getInitialWindowSizes() {
		try (MemoryStack stack = MemoryStack.stackPush()) {
			IntBuffer widthBuff = stack.mallocInt(1);
			IntBuffer heightBuff = stack.mallocInt(1);
			getInitialScreenSize(widthBuff, heightBuff);
			getInitialPixelSize(widthBuff, heightBuff);
		}
	}

	private void getInitialScreenSize(IntBuffer widthBuff, IntBuffer heightBuff) {
		glfwGetWindowSize(id, widthBuff, heightBuff);
		this.screenCoordsWidth = widthBuff.get(0);
		this.screenCoordsHeight = heightBuff.get(0);
		widthBuff.clear();
		heightBuff.clear();
	}

	private void getInitialPixelSize(IntBuffer widthBuff, IntBuffer heightBuff) {
		glfwGetFramebufferSize(id, widthBuff, heightBuff);
		this.pixelWidth = widthBuff.get(0);
		this.pixelHeight = heightBuff.get(0);
	}

	private boolean validSizeChange(int newWidth, int newHeight, int oldWidth, int oldHeight) {
		if (newWidth == 0 || newHeight == 0) {
			return false;
		}
		return newWidth != oldWidth || newHeight != oldHeight;
	}

	private void notifyListeners() {
		for (WindowSizeListener listener : listeners) {
			listener.sizeChanged(pixelWidth, pixelHeight);
		}
	}

}
