package vbartalis.game.view;

import vbartalis.engine.Window;
import vbartalis.engine.graph.Renderer;
import vbartalis.game.model.CameraModel;
import vbartalis.game.model.SceneModel;

public class RendererView {

    private final Renderer renderer;

    private boolean firstTime;
    private boolean sceneChanged;

    public RendererView() {
        renderer = new Renderer();
        firstTime = true;
    }

    public void init(Window window) throws Exception {
        renderer.init(window);
    }

    public void render(Window window, CameraModel cameraModel, SceneModel sceneModel) {
        if (firstTime) {
            sceneChanged = true;
            firstTime = false;
        }
        renderer.render(window, cameraModel.getCamera(), sceneModel.getScene(), sceneChanged);
    }

    public void cleanup() {
        renderer.cleanup();
    }
}
