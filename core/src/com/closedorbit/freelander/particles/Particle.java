package com.closedorbit.freelander.particles;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Particle {

    // Should have an image instead of a Color.
    // Should have transparency.

    public float x, y, dx, dy, size, life;
    public Sprite sprite;

    public Particle(float x, float y, float dx, float dy, float size, float life) {
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
        this.size = size;
        this.life = life;
    }

    public boolean update() {
        x += dx;
        y += dy;
        life--;
        if(life <= 0)
            return true;
        return false;
    }
}
