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

import static org.lwjgl.glfw.GLFW.*;

@Slf4j
public class DummyGame implements IGameLogic {

    private static final float MOUSE_SENSITIVITY = 0.2f;
    private static final float DRAG_MOUSE_SENSITIVITY = 0.1f;

    private final Vector3f cameraInc;
    private final Vector3f cameraRot;

    private final Renderer renderer;

    private final Camera camera;

    private Scene scene;

    private static final float CAMERA_POS_STEP = 0.40f;

    private float angleInc;

    private float lightAngle;

    private boolean firstTime;

    private boolean sceneChanged;

    private Vector3f pointLightPos;

    public DummyGame() {
        renderer = new Renderer();
        camera = new Camera();
        cameraInc = new Vector3f(0.0f, 0.0f, 0.0f);
        cameraRot = new Vector3f(0.0f, 0.0f, 0.0f);
        angleInc = 0;
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
        cubeRock2.setPosition( 10.0f, 10.0f, 0.0f);

        Mesh[] cubeRockMesh3 = StaticMeshesLoader.load("models/cube/CubeRock.obj", "models/cube");
        GameItem cubeRock3 = new GameItem(cubeRockMesh3);
        cubeRock3.setPosition( 0.0f, 5.0f, 5.0f);


        Quaternionf cubeRotation = new Quaternionf();
        cubeRotation.x = 0f;
        cubeRotation.y = 0f;
        cubeRotation.z = 0f;
        cubeRotation.w = 1f;

        Vector3f rotationXYZ = new Vector3f(0,45,0);
        rotationXYZ.x = rotationXYZ.x % 360.0f;
        rotationXYZ.y = rotationXYZ.y % 360.0f;
        rotationXYZ.z = rotationXYZ.z % 360.0f;

        cubeRotation.rotateXYZ( (float) Math.toRadians(rotationXYZ.x), (float) Math.toRadians(rotationXYZ.y), (float) Math.toRadians(rotationXYZ.z));
        cubeRock3.setRotation(cubeRotation);



//        Mesh[] terrainMesh = StaticMeshesLoader.load("models/terrain/terrain.obj", "models/terrain");
        Mesh[] terrainMesh = StaticMeshesLoader.load("models/myterrain/MyTerrain2.obj", "models/myterrain");
        GameItem terrain = new GameItem(terrainMesh);
//        terrain.setScale(100.0f);
        terrain.setScale(10.0f);

        scene.setGameItems(new GameItem[]{cubeRock1, cubeRock2, cubeRock3, terrain});

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
        sceneChanged = false;
        cameraInc.set(0, 0, 0);
        cameraRot.set(0, 0, 0);

        //----KEYBOARD----//

        //move forward W/backward S
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

        //----MOUSE----//

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

    @Override
    public void update(float interval, vbartalis.engine.input.MouseInput mouseInput, Window window) {
//        if (mouseInput.isRightButtonPressed()) {
//            // Update camera based on mouse
//            Vector2f rotVec = mouseInput.getDisplVec();
//            camera.moveRotation(rotVec.x * MOUSE_SENSITIVITY, rotVec.y * MOUSE_SENSITIVITY, 0);
//            sceneChanged = true;
//        }

        // Update camera rotation
        camera.moveRotation(cameraRot.x, cameraRot.y, cameraRot.z);

        // Update camera position
        camera.movePosition(cameraInc.x * CAMERA_POS_STEP, cameraInc.y * CAMERA_POS_STEP, cameraInc.z * CAMERA_POS_STEP);

        lightAngle += angleInc;
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
