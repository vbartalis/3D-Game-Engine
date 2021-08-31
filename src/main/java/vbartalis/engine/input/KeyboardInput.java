package vbartalis.engine.input;

import java.util.HashSet;
import java.util.Set;

import static org.lwjgl.glfw.GLFW.*;

public class KeyboardInput {

    private Set<Integer> keysDown = new HashSet<>();
    private Set<Integer> keysPressedThisFrame = new HashSet<>();
//    private Set<Integer> keysRepeatedThisFrame = new HashSet<>();
    private Set<Integer> keysReleasedThisFrame = new HashSet<>();

    public void init(long windowHandle) {
        addKeyboardListener(windowHandle);
    }

    public void update() {
        keysPressedThisFrame.clear();
//        keysRepeatedThisFrame.clear();
        keysReleasedThisFrame.clear();
    }

    public boolean isKeyDown(int key) {
        return keysDown.contains(key);
    }
    public boolean isKeyPressed(int key) {
        return keysPressedThisFrame.contains(key);
    }
    public boolean isKeyReleased(int key) {
        return keysReleasedThisFrame.contains(key);
    }
//    public boolean KeyPressEvent(int key, boolean checkRepeats) {
//        return keysPressedThisFrame.contains(key);
//    }

    private void addKeyboardListener(long windowHandle) {
        glfwSetKeyCallback(windowHandle,  (window, key, scancode, action, mods) -> {
            if (action == GLFW_PRESS) { reportKeyPress(key); }
            else if (action == GLFW_RELEASE) { reportKeyRelease(key); }
//            else if (action == GLFW_REPEAT) { reportKeyRepeat(key); }
        });
    }

    private void reportKeyPress(int key) {
        keysDown.add(key);
        keysPressedThisFrame.add(key);
    }

    private void reportKeyRelease(int key) {
        keysDown.remove(key);
        keysReleasedThisFrame.add(key);
    }

//    private void reportKeyRepeat(int key) {
//        keysReleasedThisFrame.add(key);
//    }
}
