package vbartalis.engine.items;

import lombok.Getter;
import lombok.Setter;
import vbartalis.engine.graph.Mesh;

public class SelectableItem extends GameItem{

    @Getter @Setter
    private boolean selected;

    public SelectableItem() {
        this.selected = false;
    }

    public SelectableItem(Mesh mesh) {
        super(mesh);
        this.selected = false;
    }

    public SelectableItem(Mesh[] meshes) {
        super(meshes);
        this.selected = false;
    }
}
