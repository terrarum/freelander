package com.closedorbit.freelander.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.closedorbit.freelander.utilities.Vars;

import static com.closedorbit.freelander.utilities.Vars.PPM;

public class ShipEntity extends Entity {

    public Vector2 thrust;
    public Vector2 startingVelocity;
    public Vector2 startingPosition;
    public float startingHealth;

    private float altitude;
    private Vector2 velocity;
    private float health;

    public float getHealth() {
        return health;
    }

    public void setHealth(float health) {
        this.health = health;
    }

    public Vector2 getPosition() {
        return this.body.getPosition();
    }

    public float getHeight() {
        return this.sprite.getHeight();
    }

    public float getWidth() {
        return this.sprite.getWidth();
    }

    public float getAltitude() {
        return altitude;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public void update() {
        altitude = getPosition().y * Vars.PPM - sprite.getHeight() / 2;
        velocity = body.getLinearVelocityFromWorldPoint(new Vector2(0f, 0f));
    }

    public void render(SpriteBatch sb) {
        sb.begin();
        sb.draw(sprite, getPosition().x * PPM - getWidth() / 2, getPosition().y * PPM - getHeight() / 2);
        sb.end();
    }
}
