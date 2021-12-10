package vbartalis.game;

import lombok.extern.slf4j.Slf4j;
import vbartalis.engine.IGameLogic;
import vbartalis.engine.Window;
import vbartalis.engine.graph.Renderer;
import vbartalis.engine.input.KeyboardInput;
import vbartalis.engine.input.MouseInput;
import vbartalis.engine.items.SelectableItem;
import vbartalis.game.control.InputControl;
import vbartalis.game.model.CameraModel;
import vbartalis.game.model.SceneModel;
import vbartalis.game.view.RendererView;

@Slf4j
public class GameLogicImpl implements IGameLogic {

    private final SceneModel sceneModel;

    private final CameraModel cameraModel;

    private final RendererView rendererView;

    private final InputControl inputControl;

    public GameLogicImpl() {
        sceneModel = new SceneModel();
        cameraModel = new CameraModel();
        inputControl = new InputControl();
        rendererView = new RendererView();
    }

    @Override
    public void init(Window window) throws Exception {
        rendererView.init(window);
        sceneModel.init();
        cameraModel.init();
    }

    @Override
    public void input(Window window, MouseInput mouseInput, KeyboardInput keyboardInput) {
        inputControl.update(
                window,
                mouseInput,
                keyboardInput,
                cameraModel.getCamera(),
                sceneModel.getSelectableItems().toArray(new SelectableItem[0])
        );
    }

    @Override
    public void update(float interval, vbartalis.engine.input.MouseInput mouseInput, Window window) {
        sceneModel.update(inputControl);
        cameraModel.update(inputControl);
    }

    @Override
    public void render(Window window) {
        rendererView.render(window, cameraModel, sceneModel);
    }

    @Override
    public void cleanup() {

        rendererView.cleanup();
        //todo
        sceneModel.save();
        sceneModel.cleanup();
    }
}
