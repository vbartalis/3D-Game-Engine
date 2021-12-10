package vbartalis.game;

import lombok.extern.slf4j.Slf4j;
import vbartalis.engine.IGameLogic;
import vbartalis.engine.Window;
import vbartalis.engine.graph.Renderer;
import vbartalis.engine.input.KeyboardInput;
import vbartalis.engine.input.MouseInput;
import vbartalis.engine.items.SelectableItem;
import vbartalis.game.input.InputService;
import vbartalis.game.util.CameraUtil;
import vbartalis.game.util.SceneUtil;

@Slf4j
public class GameLogicImpl implements IGameLogic {

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
