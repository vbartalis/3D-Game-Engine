package vbartalis.game.model.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import vbartalis.game.model.SceneModel;

import java.io.File;
import java.io.IOException;

public class SceneLoader {

    ObjectMapper objectMapper;

    public SceneLoader() {
        this.objectMapper = new ObjectMapper();
    }

    public void saveScene(SceneModel sceneModel) throws IOException {
        File file = new File("saves/save.scene.json");
        file.createNewFile();
        objectMapper.writerWithDefaultPrettyPrinter().writeValue( file, sceneModel);
    }
}
