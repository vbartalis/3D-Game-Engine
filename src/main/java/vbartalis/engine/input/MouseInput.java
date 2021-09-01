package vbartalis.engine.input;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.joml.Vector2f;
import vbartalis.engine.Window;

import java.util.HashSet;
import java.util.Set;

import static org.lwjgl.glfw.GLFW.*;

@Slf4j
public class MouseInput {

    private Set<Integer> buttonsDown = new HashSet<>();
    private Set<Integer> buttonsPressedThisFrame = new HashSet<>();
    private Set<Integer> buttonsReleasedThisFrame = new HashSet<>();

    @Getter
    private Vector2f currentPosition;
    private Vector2f lastPosition;
    @Getter
    private Vector2f deltaPosition;
    @Getter
    private float scroll;

//    private Window window;


    public MouseInput() {
        currentPosition = new Vector2f();
        lastPosition = new Vector2f();
        deltaPosition = new Vector2f();
    }

    public boolean isButtonDown(int key) {
        return buttonsDown.contains(key);
    }
    public boolean isButtonPressed(int key) {
        return buttonsPressedThisFrame.contains(key);
    }
    public boolean isButtonReleased(int key) {
        return buttonsReleasedThisFrame.contains(key);
    }

    public void init(Window window) {
        addMoveListener(window.getWindowHandle());
        addClickListener(window.getWindowHandle());
        addScrollListener(window.getWindowHandle());
//        this.window = window;
    }

    public void update() {
        buttonsPressedThisFrame.clear();
        buttonsReleasedThisFrame.clear();
        updateDeltas();
        scroll = 0;
    }

    //todo
    private void addMoveListener(long windowHandle){
        glfwSetCursorPosCallback(windowHandle, (currentWindow, xPos, yPos) -> {
//            this.currentPosition.x = (float) (xPos / window.getScreenCoordWidth());
//            this.currentPosition.y = (float) (yPos / window.getScreenCoordHeight());
            this.currentPosition.x = (float) xPos ;
            this.currentPosition.y = (float) yPos ;
        });
    }

    private void addClickListener(long windowHandle){
        glfwSetMouseButtonCallback(windowHandle, (window, button, action, mods) -> {
            if(action == GLFW_PRESS){
                reportButtonClick(button);
            }else if(action == GLFW_RELEASE){
                reportButtonRelease(button);
            }
        });
    }

    private void addScrollListener(long windowHandle){
        glfwSetScrollCallback(windowHandle, (window, scrollX, scrollY) -> {
            this.scroll = (float) scrollY;
        });
    }

    private void reportButtonClick(int key) {
        buttonsDown.add(key);
        buttonsPressedThisFrame.add(key);
    }

    private void reportButtonRelease(int key) {
        buttonsDown.remove(key);
        buttonsReleasedThisFrame.add(key);
    }

    private void updateDeltas() {
        deltaPosition.x = currentPosition.x - lastPosition.x;
        deltaPosition.y = currentPosition.y - lastPosition.y;
//        deltaPosition = currentPosition.sub(lastPosition);
        lastPosition.set(currentPosition);

    }
}
