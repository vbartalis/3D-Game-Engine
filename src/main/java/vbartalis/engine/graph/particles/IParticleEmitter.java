package vbartalis.engine.graph.particles;


import vbartalis.engine.items.GameItem;

import java.util.List;

public interface IParticleEmitter {

    void cleanup();
    
    Particle getBaseParticle();
    
    List<GameItem> getParticles();
}
