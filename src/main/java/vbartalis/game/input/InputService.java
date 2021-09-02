package vbartalis.game.input;

import lombok.Getter;
import org.joml.Vector3f;
import vbartalis.engine.Window;
import vbartalis.engine.input.KeyboardInput;
import vbartalis.engine.input.MouseInput;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_E;

public class InputService {

    private static final float MOUSE_SENSITIVITY = 0.2f;
    private static final float DRAG_MOUSE_SENSITIVITY = 0.1f;

    @Getter
    private boolean sceneChanged;
    @Getter
    private Vector3f cameraInc;
    @Getter
    private Vector3f cameraRot;

    @Getter
    private float angleInc;
    @Getter
    private Vector3f pointLightPos;

    public InputService() {
        cameraInc = new Vector3f(0.0f, 0.0f, 0.0f);
        cameraRot = new Vector3f(0.0f, 0.0f, 0.0f);

        pointLightPos = new Vector3f(0.0f, 25.0f, 0.0f);
        angleInc = 0;

    }

    public void update(Window window, MouseInput mouseInput, KeyboardInput keyboardInput) {
        sceneChanged = false;
        cameraInc.set(0, 0, 0);
        cameraRot.set(0, 0, 0);

        handleKeyboardInput(window, keyboardInput);
        handleMouseInput(window, mouseInput);
    }

    public void handleKeyboardInput(Window window, KeyboardInput keyboardInput) {

        if (keyboardInput.isKeyDown(GLFW_KEY_W)) {
            sceneChanged = true;
            cameraInc.z = -1;
        } else if (keyboardInput.isKeyDown(GLFW_KEY_S)) {
            sceneChanged = true;
            cameraInc.z = 1;
        }
        //move left A/right D
        if (keyboardInput.isKeyDown(GLFW_KEY_A)) {
            sceneChanged = true;
            cameraInc.x = -1;
        } else if (keyboardInput.isKeyDown(GLFW_KEY_D)) {
            sceneChanged = true;
            cameraInc.x = 1;
        }
        //move down Z/up X
        if (keyboardInput.isKeyDown(GLFW_KEY_Z)) {
            sceneChanged = true;
            cameraInc.y = -1;
        } else if (keyboardInput.isKeyDown(GLFW_KEY_X)) {
            sceneChanged = true;
            cameraInc.y = 1;
        }

        //rotate camera
        if (keyboardInput.isKeyDown(GLFW_KEY_Q)) {
            sceneChanged = true;
            cameraRot.y = -1;
        } else if (keyboardInput.isKeyDown(GLFW_KEY_E)) {
            sceneChanged = true;
            cameraRot.y = 1;
        }

        //move directional light
        if (keyboardInput.isKeyDown(GLFW_KEY_LEFT)) {
            sceneChanged = true;
            angleInc -= 0.05f;
        } else if (keyboardInput.isKeyDown(GLFW_KEY_RIGHT)) {
            sceneChanged = true;
            angleInc += 0.05f;
        } else {
            angleInc = 0;
        }
        //point light closer/farther
        if (keyboardInput.isKeyDown(GLFW_KEY_UP)) {
            sceneChanged = true;
            pointLightPos.y += 0.5f;
        } else if (keyboardInput.isKeyDown(GLFW_KEY_DOWN)) {
            sceneChanged = true;
            pointLightPos.y -= 0.5f;
        }

        //Quit
        if (keyboardInput.isKeyReleased(GLFW_KEY_ESCAPE)) {
            glfwSetWindowShouldClose(window.getWindowHandle(), true);
        }
    }

    public void handleMouseInput(Window window, MouseInput mouseInput) {
        if (mouseInput.getScroll() != 0) {
            if (mouseInput.getScroll() > 0) {
                sceneChanged = true;
                cameraInc.y = -1;
            } else if (mouseInput.getScroll() < 0) {
                sceneChanged = true;
                cameraInc.y = 1;
            }
        }

        if (mouseInput.isButtonDown(GLFW_MOUSE_BUTTON_MIDDLE)) {
            cameraInc.x = mouseInput.getDeltaPosition().x;
            cameraInc.z = mouseInput.getDeltaPosition().y;
            cameraInc.mul(-DRAG_MOUSE_SENSITIVITY);
        }
    }
}
