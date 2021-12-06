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
import vbartalis.engine.items.SkyBox;
import vbartalis.engine.loaders.assimp.StaticMeshesLoader;
import vbartalis.game.input.InputService;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

@Slf4j
public class DummyGame implements IGameLogic {

    private static final float MOUSE_SENSITIVITY = 0.2f;
    private static final float DRAG_MOUSE_SENSITIVITY = 0.1f;

//    private final Vector3f cameraInc;
//    private final Vector3f cameraRot;

    private final Renderer renderer;

    private final Camera camera;

    private Scene scene;

    private static final float CAMERA_POS_STEP = 0.40f;

//    private float angleInc;

    private float lightAngle;

    private boolean firstTime;

    private boolean sceneChanged;

    private Vector3f pointLightPos;

    private final InputService inputService;

    private ArrayList<GameItem> selectebleGameItems;
    private ArrayList<GameItem> gameItems;

    public DummyGame() {
        renderer = new Renderer();
        camera = new Camera();
        inputService = new InputService();

        lightAngle = 90;
        firstTime = true;
    }

    @Override
    public void init(Window window) throws Exception {
        renderer.init(window);


        scene = new Scene();

//        Mesh[] houseMesh = StaticMeshesLoader.load("models/house/house.obj", "models/house");
//        GameItem house = new GameItem(houseMesh);

        Mesh[] cubeRockMesh1 = StaticMeshesLoader.load("models/cube/CubeRock.obj", "models/cube");
        GameItem cubeRock1 = new GameItem(cubeRockMesh1);

        Mesh[] cubeRockMesh2 = StaticMeshesLoader.load("models/cube/CubeRock.obj", "models/cube");
        GameItem cubeRock2 = new GameItem(cubeRockMesh2);
        cubeRock2.setPosition(10.0f, 10.0f, 0.0f);

        Mesh[] cubeRockMesh3 = StaticMeshesLoader.load("models/cube/CubeRock.obj", "models/cube");
        GameItem cubeRock3 = new GameItem(cubeRockMesh3);
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



//        Mesh[] terrainMesh = StaticMeshesLoader.load("models/terrain/terrain.obj", "models/terrain");
        Mesh[] terrainMesh = StaticMeshesLoader.load("models/myterrain/MyTerrain2.obj", "models/myterrain");
        GameItem terrain = new GameItem(terrainMesh);
//        terrain.setScale(100.0f);
        terrain.setScale(10.0f);

//        gameItems = new GameItem[]{cubeRock1, cubeRock2, cubeRock3, terrain};
//        selectebleGameItems = new GameItem[]{cubeRock1, cubeRock2, cubeRock3};
        selectebleGameItems = new ArrayList<GameItem>(List.of(cubeRock1, cubeRock2, cubeRock3));
        gameItems = new ArrayList<GameItem>(List.of(terrain));
        gameItems.addAll(selectebleGameItems);

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

        camera.getPosition().x = -17.0f;
        camera.getPosition().y =  17.0f;
        camera.getPosition().z = -30.0f;
//        camera.getRotation().x = 20.0f;
        camera.getRotation().x = 45.0f;
        camera.getRotation().y = 140.f;
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

    @Override
    public void input(Window window, MouseInput mouseInput, KeyboardInput keyboardInput) {
        inputService.update(window, mouseInput, keyboardInput, camera, selectebleGameItems.toArray(new GameItem[0]));
    }

    @Override
    public void update(float interval, vbartalis.engine.input.MouseInput mouseInput, Window window) {
//        if (mouseInput.isRightButtonPressed()) {
//            // Update camera based on mouse
//            Vector2f rotVec = mouseInput.getDisplVec();
//            camera.moveRotation(rotVec.x * MOUSE_SENSITIVITY, rotVec.y * MOUSE_SENSITIVITY, 0);
//            sceneChanged = true;
//        }

        // Update camera rotation
        camera.moveRotation(inputService.getCameraRot().x,
                            inputService.getCameraRot().y,
                            inputService.getCameraRot().z);
        // Update camera position
        camera.movePosition(inputService.getCameraInc().x * CAMERA_POS_STEP,
                            inputService.getCameraInc().y * CAMERA_POS_STEP,
                            inputService.getCameraInc().z * CAMERA_POS_STEP);

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

        // Update view matrix
        camera.updateViewMatrix();
    }

    @Override
    public void render(Window window) {
        if (firstTime) {
            sceneChanged = true;
            firstTime = false;
        }
        renderer.render(window, camera, scene, sceneChanged);
    }

    @Override
    public void cleanup() {
        renderer.cleanup();

        scene.cleanup();
    }
}
