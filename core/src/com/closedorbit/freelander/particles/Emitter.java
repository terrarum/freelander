package com.closedorbit.freelander.particles;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.closedorbit.freelander.utilities.Vars;

import java.util.ArrayList;

public class Emitter {

    private ArrayList<Particle> particles;
    public float x;
    public float y;
    private Body parentBody = null;
    private float offsetX;
    private float offsetY;

    public Emitter() {
        particles = new ArrayList<Particle>();
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // Create a particle.
    public void createParticles(int particleCount, Sprite sprite) {
        int i = 0;
        while (i < particleCount) {
            // Delta should be affected by ship thrust.
            particles.add(new Particle(this.x, this.y, Vars.getRandomFloat(-0.5f, 0.3f), -7, 8, 30, sprite));
            i++;
        }
    }

    // Attach the emitter to a box2d body. Overrides the X/Y coordinates passed to the constructor.
    public void attachToBody(Body body, float offsetX, float offsetY) {
        this.parentBody = body;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.x = (body.getPosition().x + offsetX) * Vars.PPM;
        this.y = (body.getPosition().y + offsetY) * Vars.PPM;
    }

    // Update the emitter position if required, and update all of its particles.
    public void update() {
        // Update emitter position to parentBody position.
        if (parentBody != null) {
            this.x = (parentBody.getPosition().x + offsetX) * Vars.PPM;
            this.y = (parentBody.getPosition().y + offsetY) * Vars.PPM;
        }

        for (Particle particle : particles) {
            particle.update();
        }
    }

    public void render(SpriteBatch sb) {
        sb.begin();
        for (Particle particle : particles) {
            float life = particle.life / particle.maxLife;
            sb.setColor(1.0f, 1.0f, 1.0f, life);
            sb.draw(particle.sprite, particle.x, particle.y);
        }
        sb.end();
    }
}
