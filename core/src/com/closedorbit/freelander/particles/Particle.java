package com.closedorbit.freelander.particles;

import com.badlogic.gdx.graphics.g2d.Sprite;

public class Particle {

    public float x, y, dx, dy, size, life, maxLife;
    public Sprite sprite;

    public Particle(float x, float y, float dx, float dy, float size, float life, Sprite sprite) {
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
        this.size = size;
        this.life = life;
        this.maxLife = life;
        this.sprite = sprite;
    }

    public boolean update() {
        this.x += dx;
        this.y += dy;
        life--;

        if (life <= 0) {
            return false;
        }
        return true;
    }
}
