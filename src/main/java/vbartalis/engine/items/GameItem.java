package vbartalis.engine.items;

import lombok.Getter;
import lombok.Setter;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import vbartalis.engine.graph.Mesh;

public class GameItem {

    @Getter @Setter
    private Mesh[] meshes;

    @Getter
    private final Vector3f position;

    @Getter @Setter
    private float scale;

    @Getter
    private final Quaternionf rotation;

    @Getter @Setter
    private int textPos;

    @Getter @Setter
    private boolean disableFrustumCulling;

    @Getter @Setter
    private boolean insideFrustum;

    public GameItem() {
        position = new Vector3f();
        scale = 1;
        rotation = new Quaternionf();
        textPos = 0;
        insideFrustum = true;
        disableFrustumCulling = false;
    }

    public GameItem(Mesh mesh) {
        this();
        this.meshes = new Mesh[]{mesh};
    }

    public GameItem(Mesh[] meshes) {
        this();
        this.meshes = meshes;
    }

    public final void setPosition(float x, float y, float z) {
        this.position.x = x;
        this.position.y = y;
        this.position.z = z;
    }

    public final void setRotation(Quaternionf q) {
        this.rotation.set(q);
    }

    public Mesh getMesh() {
        return meshes[0];
    }

    public void setMesh(Mesh mesh) {
        this.meshes = new Mesh[]{mesh};
    }

    public void cleanup() {
        int numMeshes = this.meshes != null ? this.meshes.length : 0;
        for (int i = 0; i < numMeshes; i++) {
            this.meshes[i].cleanUp();
        }
    }


}
