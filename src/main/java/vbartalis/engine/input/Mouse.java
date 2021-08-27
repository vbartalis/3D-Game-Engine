package vbartalis.engine.input;



import lombok.Getter;
import vbartalis.engine.window.Window;

import java.util.HashSet;
import java.util.Set;

import static org.lwjgl.glfw.GLFW.*;

public class Mouse {
	
	private final Window window;

	private Set<Integer> buttonsDown = new HashSet<Integer>();
	private Set<Integer> buttonsClickedThisFrame = new HashSet<Integer>();
	private Set<Integer> buttonsReleasedThisFrame = new HashSet<Integer>();

	@Getter
	private float x, y;
	@Getter
	private float dx, dy;
	@Getter
	private float scroll;
	private float lastX, lastY;
	
	public Mouse(Window window){
		this.window = window;
		addMoveListener(window.getId());
		addClickListener(window.getId());
		addScrollListener(window.getId());
	}
	
	public boolean isButtonDown(MouseButton button){
		return buttonsDown.contains(button.getGlfwId());
	}
	
	public boolean isClickEvent(MouseButton button){
		return buttonsClickedThisFrame.contains(button.getGlfwId());
	}
	
	public boolean isReleaseEvent(MouseButton button){
		return buttonsReleasedThisFrame.contains(button.getGlfwId());
	}
	
	public void update(){
		buttonsClickedThisFrame.clear();
		buttonsReleasedThisFrame.clear();
		updateDeltas();
		this.scroll = 0;
	}
	
	private void reportButtonClick(int button){
		buttonsClickedThisFrame.add(button);
		buttonsDown.add(button);
	}
	
	private void reportButtonRelease(int button){
		buttonsReleasedThisFrame.add(button);
		buttonsDown.remove((Integer)button);
	}
	
	private void updateDeltas(){
		this.dx = x - lastX;
		this.dy = y - lastY;
		this.lastX = x;
		this.lastY = y;
	}

	private void addMoveListener(long windowId){
		glfwSetCursorPosCallback(windowId, (currentWindow, xPos, yPos) -> {
			this.x = (float) (xPos / window.getScreenCoordsWidth());
			this.y = (float) (yPos / window.getScreenCoordsHeight());
		});
	}

	private void addClickListener(long windowId){
		glfwSetMouseButtonCallback(windowId, (window, button, action, mods) -> {
			if(action == GLFW_PRESS){
				reportButtonClick(button);
			}else if(action == GLFW_RELEASE){
				reportButtonRelease(button);
			}
		});
	}
	
	private void addScrollListener(long windowId){
		glfwSetScrollCallback(windowId, (window, scrollX, scrollY) -> {
			this.scroll = (float) scrollY;
		});
	}

}
