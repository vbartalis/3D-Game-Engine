package vbartalis.engine;

import lombok.Getter;
import lombok.Setter;
import vbartalis.engine.graph.InstancedMesh;
import vbartalis.engine.graph.Mesh;
import vbartalis.engine.graph.particles.IParticleEmitter;
import vbartalis.engine.graph.weather.Fog;
import vbartalis.engine.items.GameItem;
import vbartalis.engine.items.SkyBox;
import vbartalis.engine.items.Terrain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scene {

    @Getter
    private final Map<Mesh, List<GameItem>> meshMap;

    @Getter
    private final Map<InstancedMesh, List<GameItem>> instancedMeshMap;

    @Getter @Setter
    private SkyBox skyBox;

//    @Getter @Setter
//    private Terrain terrain;

    @Getter @Setter
    private SceneLight sceneLight;

    @Getter @Setter
    private Fog fog;

    @Getter @Setter
    private boolean renderShadows;

    @Getter @Setter
    private IParticleEmitter[] particleEmitters;

    public Scene() {
        meshMap = new HashMap();
        instancedMeshMap = new HashMap();
        fog = Fog.NOFOG;
        renderShadows = true;
    }

    public void setGameItems(GameItem[] gameItems) {
        // Create a map of meshes to speed up rendering
        int numGameItems = gameItems != null ? gameItems.length : 0;
        for (int i = 0; i < numGameItems; i++) {
            GameItem gameItem = gameItems[i];
            Mesh[] meshes = gameItem.getMeshes();
            for (Mesh mesh : meshes) {
                boolean instancedMesh = mesh instanceof InstancedMesh;
                List<GameItem> list = instancedMesh ? instancedMeshMap.get(mesh) : meshMap.get(mesh);
                if (list == null) {
                    list = new ArrayList<>();
                    if (instancedMesh) {
                        instancedMeshMap.put((InstancedMesh)mesh, list);
                    } else {
                        meshMap.put(mesh, list);
                    }
                }
                list.add(gameItem);
            }
        }
    }

    public void cleanup() {
        for (Mesh mesh : meshMap.keySet()) {
            mesh.cleanUp();
        }
        for (Mesh mesh : instancedMeshMap.keySet()) {
            mesh.cleanUp();
        }
        if (particleEmitters != null) {
            for (IParticleEmitter particleEmitter : particleEmitters) {
                particleEmitter.cleanup();
            }
        }
    }
}
