package vbartalis.game.util;

import lombok.Getter;
import vbartalis.engine.graph.Camera;
import vbartalis.game.input.InputService;

public class CameraUtil {

    @Getter
    private final Camera camera;

    private static final float CAMERA_POS_STEP = 0.40f;

    public CameraUtil() {
        this.camera = new Camera();
    }

    public void init() {
        camera.getPosition().x = -17.0f;
        camera.getPosition().y =  17.0f;
        camera.getPosition().z = -30.0f;
//        camera.getRotation().x = 20.0f;
        camera.getRotation().x = 45.0f;
        camera.getRotation().y = 140.f;
    }

    public void update(InputService inputService) {
        // Update camera rotation
        camera.moveRotation(inputService.getCameraRot().x,
                inputService.getCameraRot().y,
                inputService.getCameraRot().z);
        // Update camera position
        camera.movePosition(inputService.getCameraInc().x * CAMERA_POS_STEP,
                inputService.getCameraInc().y * CAMERA_POS_STEP,
                inputService.getCameraInc().z * CAMERA_POS_STEP);

        // Update view matrix
        camera.updateViewMatrix();
    }
}
