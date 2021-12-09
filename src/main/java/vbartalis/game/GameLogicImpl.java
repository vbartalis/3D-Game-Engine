package vbartalis.game;

import lombok.extern.slf4j.Slf4j;
import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.system.MathUtil;
import vbartalis.engine.*;
import vbartalis.engine.graph.Camera;
import vbartalis.engine.graph.Mesh;
import vbartalis.engine.graph.Renderer;
import vbartalis.engine.graph.lights.DirectionalLight;
import vbartalis.engine.graph.lights.PointLight;
import vbartalis.engine.graph.weather.Fog;
import vbartalis.engine.input.KeyboardInput;
import vbartalis.engine.input.MouseInput;
import vbartalis.engine.items.GameItem;
import vbartalis.engine.items.SelectableItem;
import vbartalis.engine.items.SkyBox;
import vbartalis.engine.loaders.assimp.StaticMeshesLoader;
import vbartalis.game.input.InputService;
import vbartalis.game.util.CameraUtil;
import vbartalis.game.util.SceneUtil;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

@Slf4j
public class GameLogicImpl implements IGameLogic {

    private static final float MOUSE_SENSITIVITY = 0.2f;
    private static final float DRAG_MOUSE_SENSITIVITY = 0.1f;

    private final Renderer renderer;

    private final SceneUtil sceneUtil;

    private final CameraUtil cameraUtil;

    private boolean firstTime;

    private boolean sceneChanged;

    private final InputService inputService;

    public GameLogicImpl() {
        renderer = new Renderer();
        sceneUtil = new SceneUtil();
        cameraUtil = new CameraUtil();
        inputService = new InputService();
        firstTime = true;
    }

    @Override
    public void init(Window window) throws Exception {
        renderer.init(window);
        sceneUtil.init();
        cameraUtil.init();
    }



    @Override
    public void input(Window window, MouseInput mouseInput, KeyboardInput keyboardInput) {
        inputService.update(
                window,
                mouseInput,
                keyboardInput,
                cameraUtil.getCamera(),
                sceneUtil.getSelectableItems().toArray(new SelectableItem[0])
        );
    }

    @Override
    public void update(float interval, vbartalis.engine.input.MouseInput mouseInput, Window window) {
//        if (mouseInput.isRightButtonPressed()) {
//            // Update camera based on mouse
//            Vector2f rotVec = mouseInput.getDisplVec();
//            camera.moveRotation(rotVec.x * MOUSE_SENSITIVITY, rotVec.y * MOUSE_SENSITIVITY, 0);
//            sceneChanged = true;
//        }

        sceneUtil.update(inputService);
        cameraUtil.update(inputService);
    }

    @Override
    public void render(Window window) {
        if (firstTime) {
            sceneChanged = true;
            firstTime = false;
        }
        renderer.render(window, cameraUtil.getCamera(), sceneUtil.getScene(), sceneChanged);
    }

    @Override
    public void cleanup() {
        renderer.cleanup();
        sceneUtil.cleanup();
    }
}
