package vbartalis.engine.graph.shadow;

import vbartalis.engine.Utils;
import vbartalis.engine.Window;
import vbartalis.engine.graph.Mesh;
import vbartalis.engine.graph.ShaderProgram;
import vbartalis.engine.loaders.assimp.StaticMeshesLoader;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

public class ShadowTestRenderer {

    private ShaderProgram testShaderProgram;

    private Mesh quadMesh;

    public void init(Window window) throws Exception {
        setupTestShader();
    }

    private void setupTestShader() throws Exception {
        testShaderProgram = new ShaderProgram();
        testShaderProgram.createVertexShader(Utils.loadResource("/shaders/test_vertex.vs"));
        testShaderProgram.createFragmentShader(Utils.loadResource("/shaders/test_fragment.fs"));
        testShaderProgram.link();

        for (int i = 0; i < ShadowRenderer.NUM_CASCADES; i++) {
            testShaderProgram.createUniform("texture_sampler[" + i + "]");
        }

        quadMesh = StaticMeshesLoader.load("/models/quad.obj", "")[0];
    }

    public void renderTest(ShadowBuffer shadowMap) {
        testShaderProgram.bind();

        testShaderProgram.setUniform("texture_sampler[0]", 0);

        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, shadowMap.getDepthMapTexture().getIds()[0]);

        quadMesh.render();

        testShaderProgram.unbind();
    }

    public void cleanup() {
        if (testShaderProgram != null) {
            testShaderProgram.cleanup();
        }
    }
}
