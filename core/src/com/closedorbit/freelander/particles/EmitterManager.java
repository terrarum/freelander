package com.closedorbit.freelander.particles;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;

public class EmitterManager {

    private ArrayList<Emitter> emitters;

    public EmitterManager() {
        emitters = new ArrayList<Emitter>();
    }

    // Returns an emitter and adds it to the emitter list.
    public Emitter createEmitter() {
        Emitter emitter = new Emitter();
        emitters.add(emitter);
        return emitter;
    }

    // Updates each emitter.
    public void update(float delta) {
        for (Emitter emitter : emitters) {
            emitter.update();
        }
    }

    // Calls the render function of each emitter.
    public void render(SpriteBatch sb) {
        for (Emitter emitter : emitters) {
            emitter.render(sb);
        }
    }
}
