package com.closedorbit.freelander.entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;

import static com.closedorbit.freelander.utilities.B2DVars.PPM;

/**
 * Created by terrarum on 29/04/14.
 */
public class ShipEntity {
    public Sprite sprite;
    public Body body;

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
