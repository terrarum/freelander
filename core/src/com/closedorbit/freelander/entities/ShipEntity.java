package com.closedorbit.freelander.entities;

import box2dLight.Light;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.closedorbit.freelander.utilities.Vars;

import static com.closedorbit.freelander.utilities.Vars.PPM;

public class ShipEntity extends Entity {

    Float delta;

    public Vector2 thrust;
    public Vector2 startingVelocity;
    public Vector2 startingPosition;
    public float maxHealth;
    public float startingHealth;
    public float maxFuel;
    public float startingFuel;

    private float altitude;
    private Vector2 velocity;
    private float health;
    private float fuel;
    public String image;

    public Light rocketLight;

    public float maxFuelConsumptionRate;

    public float getHealth() {
        return health;
    }

    public float reduceHealth(float damage) {
        float currentHealth = getHealth() - damage;
        currentHealth = currentHealth < 0 ? 0 : currentHealth;
        setHealth(currentHealth);
        return currentHealth;
    }

    public void setHealth(float health) {
        this.health = health;
    }

    public float getFuel() {
        return fuel;
    }

    public void consumeFuel(float rate) {
        if (rate > maxFuelConsumptionRate) {
            rate = maxFuelConsumptionRate;
        }
        float newFuelCapacity = getFuel() - rate;
        newFuelCapacity = newFuelCapacity < 0 ? 0 : newFuelCapacity;
        setFuel(newFuelCapacity);
    }

    public void setFuel(float fuel) {
        this.fuel = fuel;
    }

    public void thrust(Vector2 vector) {
        if (getFuel() > 0) {
            body.applyForce(vector, body.getWorldCenter(), true);

            rocketLight.setActive(true);
        }
        else {
            rocketLight.setActive(false);
        }
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

    public void update(float delta) {
        this.delta = delta;
        altitude = getPosition().y * Vars.PPM - sprite.getHeight() / 2;
        velocity = body.getLinearVelocityFromWorldPoint(new Vector2(0f, 0f));
    }

    public void render(SpriteBatch sb) {
        sb.begin();
        sb.draw(sprite, getPosition().x * PPM - getWidth() / 2, getPosition().y * PPM - getHeight() / 2);
        sb.end();
    }
}
