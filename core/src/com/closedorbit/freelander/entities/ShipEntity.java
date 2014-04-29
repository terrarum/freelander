package com.closedorbit.freelander.entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;

import static com.closedorbit.freelander.utilities.B2DVars.PPM;

public class ShipEntity extends Entity {

    public Vector2 thrust;
    public Vector2 startingVelocity;
    public Vector2 startingPosition;

    public Vector2 getPosition() {
        return this.body.getPosition();
    }

    public float getHeight() {
        return this.sprite.getHeight();
    }

    public float getWidth() {
        return this.sprite.getWidth();
    }

    public void render(SpriteBatch sb) {
        sb.begin();
        sb.draw(sprite, getPosition().x * PPM - getWidth() / 2, getPosition().y * PPM - getHeight() / 2);
        sb.end();
    }
}
