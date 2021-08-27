package vbartalis.engine.input;

import lombok.Getter;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Keyboard {

	private Set<Integer> keysPressedThisFrame = new HashSet<Integer>();
	private Set<Integer> keysRepeatedThisFrame = new HashSet<Integer>();
	private Set<Integer> keysReleasedThisFrame = new HashSet<Integer>();
	private Set<Integer> keysDown = new HashSet<Integer>();
	@Getter
	private List<Integer> charsThisFrame = new ArrayList<Integer>();

	public Keyboard(long windowId) {
		addKeyListener(windowId);
		addTextListener(windowId);
	}

	public void update() {
		keysPressedThisFrame.clear();
		keysReleasedThisFrame.clear();
		keysRepeatedThisFrame.clear();
		charsThisFrame.clear();;
	}

	public boolean isKeyDown(int key) {
		return keysDown.contains(key);
	}

	public boolean keyPressEvent(int key) {
		return keysPressedThisFrame.contains(key);
	}

	public boolean keyPressEvent(int key, boolean checkRepeats) {
		return keysPressedThisFrame.contains(key) || (checkRepeats && keysRepeatedThisFrame.contains(key));
	}

	public boolean keyReleaseEvent(int key) {
		return keysReleasedThisFrame.contains(key);
	}

	private void reportKeyPress(int key) {
		keysDown.add(key);
		keysPressedThisFrame.add(key);
	}

	private void reportKeyRelease(int key) {
		keysDown.remove(((Integer) key));
		keysReleasedThisFrame.add(key);
	}

	private void addTextListener(long windowId) {
		GLFW.glfwSetCharCallback(windowId, (window, unicode) -> {
			charsThisFrame.add(unicode);
		});
	}

	private void addKeyListener(long windowId) {
		GLFW.glfwSetKeyCallback(windowId, (window, key, scancode, action, mods) -> {
			if (action == GLFW.GLFW_PRESS) {
				reportKeyPress(key);
			} else if (action == GLFW.GLFW_RELEASE) {
				reportKeyRelease(key);
			} else if (action == GLFW.GLFW_REPEAT) {
				keysRepeatedThisFrame.add(key);
			}
		});
	}

}
