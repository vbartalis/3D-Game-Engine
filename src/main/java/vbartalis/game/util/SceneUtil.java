package vbartalis.game.util;

import lombok.Getter;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import vbartalis.engine.Scene;
import vbartalis.engine.SceneLight;
import vbartalis.engine.graph.Mesh;
import vbartalis.engine.graph.lights.DirectionalLight;
import vbartalis.engine.graph.lights.PointLight;
import vbartalis.engine.items.GameItem;
import vbartalis.engine.items.SelectableItem;
import vbartalis.engine.loaders.assimp.StaticMeshesLoader;
import vbartalis.game.input.InputService;

import java.util.ArrayList;
import java.util.List;

public class SceneUtil {

    @Getter
    private Scene scene;

    @Getter
    private ArrayList<SelectableItem> selectableItems = new ArrayList<>();
    @Getter
    private ArrayList<GameItem> terrainItems = new ArrayList<>();
    @Getter
    private ArrayList<GameItem> gameItems = new ArrayList<>();

    private Vector3f pointLightPos;

    private float lightAngle;

    public SceneUtil() {
        lightAngle = 90;
    }

    public void init() throws Exception {
        scene = new Scene();

//        Mesh[] houseMesh = StaticMeshesLoader.load("models/house/house.obj", "models/house");
//        GameItem house = new GameItem(houseMesh);

        Mesh[] cubeRockMesh1 = StaticMeshesLoader.load("models/cube/CubeRock.obj", "models/cube");
        SelectableItem cubeRock1 = new SelectableItem(cubeRockMesh1);

        Mesh[] cubeRockMesh2 = StaticMeshesLoader.load("models/cube/CubeRock.obj", "models/cube");
        SelectableItem cubeRock2 = new SelectableItem(cubeRockMesh2);
        cubeRock2.setPosition(10.0f, 10.0f, 0.0f);

        Mesh[] cubeRockMesh3 = StaticMeshesLoader.load("models/cube/CubeRock.obj", "models/cube");
        SelectableItem cubeRock3 = new SelectableItem(cubeRockMesh3);
        cubeRock3.setPosition(0.0f, 5.0f, 5.0f);

        Quaternionf cubeRotation = new Quaternionf();
        cubeRotation.x = 0f;
        cubeRotation.y = 0f;
        cubeRotation.z = 0f;
        cubeRotation.w = 1f;

        Vector3f rotationXYZ = new Vector3f(0, 45, 0);
        rotationXYZ.x = rotationXYZ.x % 360.0f;
        rotationXYZ.y = rotationXYZ.y % 360.0f;
        rotationXYZ.z = rotationXYZ.z % 360.0f;

        cubeRotation.rotateXYZ((float) Math.toRadians(rotationXYZ.x),
                (float) Math.toRadians(rotationXYZ.y),
                (float) Math.toRadians(rotationXYZ.z));
        cubeRock3.setRotation(cubeRotation);

        SelectableItem cubeRock4 = new SelectableItem(cubeRockMesh3);
        cubeRock4.setPosition(5.0f, 5.0f, 5.0f);

        selectableItems = new ArrayList<>(List.of(cubeRock1, cubeRock2, cubeRock3,cubeRock4));


//        Mesh[] terrainMesh = StaticMeshesLoader.load("models/terrain/terrain.obj", "models/terrain");
        Mesh[] terrainMesh = StaticMeshesLoader.load("models/myterrain/MyTerrain2.obj", "models/myterrain");
        GameItem terrain = new GameItem(terrainMesh);
//        terrain.setScale(100.0f);
        terrain.setScale(10.0f);

        terrainItems = new ArrayList<>(List.of(terrain));
        gameItems.addAll(selectableItems);
        gameItems.addAll(terrainItems);

        scene.setGameItems(gameItems.toArray(new GameItem[0]));

        // Shadows
        scene.setRenderShadows(true);

        // Fog
//        Vector3f fogColour = new Vector3f(0.5f, 0.5f, 0.5f);
//        scene.setFog(new Fog(true, fogColour, 0.02f));

        // Setup  SkyBox
//        float skyBoxScale = 100.0f;
//        SkyBox skyBox = new SkyBox("models/skybox.obj", new Vector4f(0.65f, 0.65f, 0.65f, 1.0f));
//        SkyBox skyBox = new SkyBox("models/skybox.obj", "textures/skybox.png");
//        skyBox.setScale(skyBoxScale);
//        scene.setSkyBox(skyBox);

        // Setup Lights
        setupLights();
    }

    private void setupLights() {
        SceneLight sceneLight = new SceneLight();
        scene.setSceneLight(sceneLight);

        // Ambient Light
        sceneLight.setAmbientLight(new Vector3f(0.3f, 0.3f, 0.3f));
        sceneLight.setSkyBoxLight(new Vector3f(1.0f, 1.0f, 1.0f));

        // Directional Light
        float lightIntensity = 1.0f;
        Vector3f lightDirection = new Vector3f(0, 1, 1);
        DirectionalLight directionalLight = new DirectionalLight(new Vector3f(1, 1, 1), lightDirection, lightIntensity);
        sceneLight.setDirectionalLight(directionalLight);

        pointLightPos = new Vector3f(0.0f, 25.0f, 0.0f);
//        Vector3f pointLightColour = new Vector3f(0.0f, 1.0f, 0.0f);
        Vector3f pointLightColour = new Vector3f(1.0f, 1.0f, 1.0f);
        PointLight.Attenuation attenuation = new PointLight.Attenuation(1, 0.0f, 0);
        PointLight pointLight = new PointLight(pointLightColour, pointLightPos, lightIntensity, attenuation);
        sceneLight.setPointLightList( new PointLight[] {pointLight});
    }

    public void update(InputService inputService) {

        pointLightPos.set(inputService.getPointLightPos());

        lightAngle += inputService.getAngleInc();
        if (lightAngle < 0) {
            lightAngle = 0;
        } else if (lightAngle > 180) {
            lightAngle = 180;
        }
        float zValue = (float) Math.cos(Math.toRadians(lightAngle));
        float yValue = (float) Math.sin(Math.toRadians(lightAngle));
        Vector3f lightDirection = this.scene.getSceneLight().getDirectionalLight().getDirection();
        lightDirection.x = 0;
        lightDirection.y = yValue;
        lightDirection.z = zValue;
        lightDirection.normalize();
    }

    public void cleanup() {
        scene.cleanup();
    }


}
