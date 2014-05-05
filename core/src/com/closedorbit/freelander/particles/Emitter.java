package com.closedorbit.freelander.particles;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;

import java.util.ArrayList;

public class Emitter {

    private ArrayList<Particle> particles;
    public float x;
    public float y;
    private Body parentBody = null;
    public Sprite particleSprite;

    public Emitter() {
        System.out.println("New Emitter");
        particles = new ArrayList<Particle>();
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setParticleSprite(Sprite sprite) {
        this.particleSprite = sprite;
    }

    // Create a particle.
    public void createParticles(int particleCount) {
        int i = 0;
        while (i < particleCount) {
            particles.add(new Particle(this.x, this.y, 0, -10, 10, 100));
            i++;
        }
    }

    // Attach the emitter to a box2d body. Overrides the X/Y coordinates passed to the constructor.
    public void attachToBody(Body body) {
        this.parentBody = body;
    }

    // Update the emitter position if required, and update all of its particles.
    public void  update(float delta) {

        // Update emitter position to parentBody position.
        if (parentBody != null) {
            this.x = parentBody.getPosition().x;
            this.y = parentBody.getPosition().y;
        }

        for (Particle particle : particles) {
            particle.x += particle.dx;
            particle.y += particle.dy;
            particle.life--;
            if (particle.life <= 0) {
                particles.remove(particle);
            }
        }
    }

    public void render(SpriteBatch sb) {
        sb.begin();
        for (Particle particle : particles) {
            if (particle.update()) {
                sb.draw(particle.sprite, particle.x += particle.dx, particle.y += particle.dy);
            }
        }
        sb.end();
    }
}
