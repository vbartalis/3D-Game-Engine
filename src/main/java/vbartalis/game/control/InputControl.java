package vbartalis.game.control;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.joml.Vector3f;
import vbartalis.engine.Window;
import vbartalis.engine.graph.Camera;
import vbartalis.engine.input.KeyboardInput;
import vbartalis.engine.input.MouseInput;
import vbartalis.engine.items.SelectableItem;
import vbartalis.game.control.service.MouseBoxSelectionDetector;

import static org.lwjgl.glfw.GLFW.*;

@Slf4j
public class InputControl {

    private static final float MOUSE_SENSITIVITY = 0.2f;
    private static final float MOUSE_ZOOM_SENSITIVITY = 5.0f;
    private static final float DRAG_MOUSE_SENSITIVITY = 0.1f;

    private final MouseBoxSelectionDetector selectDetector;

    @Getter
    private boolean sceneChanged;
    @Getter
    private final Vector3f cameraInc;
    @Getter
    private final Vector3f cameraRot;

    @Getter
    private float angleInc;
    @Getter
    private final Vector3f pointLightPos;

    public InputControl() {
        cameraInc = new Vector3f(0.0f, 0.0f, 0.0f);
        cameraRot = new Vector3f(0.0f, 0.0f, 0.0f);

        pointLightPos = new Vector3f(0.0f, 25.0f, 0.0f);
        angleInc = 0;
        selectDetector = new MouseBoxSelectionDetector();

    }

    public void update(Window window, MouseInput mouseInput, KeyboardInput keyboardInput, Camera camera, SelectableItem[] gameItems) {
        sceneChanged = false;
        cameraInc.set(0, 0, 0);
        cameraRot.set(0, 0, 0);

        handleKeyboardInput(window, keyboardInput);
        handleMouseInput(window, mouseInput, camera,gameItems);
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

    public void handleMouseInput(Window window, MouseInput mouseInput, Camera camera, SelectableItem[] gameItems) {
        if (mouseInput.getScroll() != 0) {
            if (mouseInput.getScroll() > 0) {
                sceneChanged = true;
                cameraInc.y = -1 * MOUSE_ZOOM_SENSITIVITY;
            } else if (mouseInput.getScroll() < 0) {
                sceneChanged = true;
                cameraInc.y = 1  * MOUSE_ZOOM_SENSITIVITY;
            }
        }

        if (mouseInput.isButtonDown(GLFW_MOUSE_BUTTON_MIDDLE)) {
            cameraInc.x = mouseInput.getDeltaPosition().x;
            cameraInc.z = mouseInput.getDeltaPosition().y;
            cameraInc.mul(-DRAG_MOUSE_SENSITIVITY);
        }

        if (mouseInput.isButtonPressed(GLFW_MOUSE_BUTTON_LEFT)
                && this.selectDetector.selectGameItem(gameItems,window,mouseInput.getCurrentPosition(), camera)) {
            log.info("selected");
        }

        //        if (mouseInput.isRightButtonPressed()) {
//            // Update camera based on mouse
//            Vector2f rotVec = mouseInput.getDisplVec();
//            camera.moveRotation(rotVec.x * MOUSE_SENSITIVITY, rotVec.y * MOUSE_SENSITIVITY, 0);
//            sceneChanged = true;
//        }
    }
}
