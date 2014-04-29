package com.closedorbit.freelander.factories;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.closedorbit.freelander.entities.ShipEntity;
import com.closedorbit.freelander.levelPackLoader.Level;

import static com.closedorbit.freelander.utilities.B2DVars.PPM;

/**
 * Created by terrarum on 28/04/14.
 */
public class ShipFactory {

    World world;

    public ShipFactory(World world) {
        this.world = world;
    }

    public ShipEntity createShip(Level levelData, float x, float y, String imageUrl) {

        Texture texture = new Texture(Gdx.files.internal("images/dropship.png"));
        Sprite sprite = new Sprite(texture, 0, 0, texture.getWidth(), texture.getHeight());

        // Create bodyDef.
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(levelData.ship.startingPosition.x / PPM, levelData.ship.startingPosition.y / PPM );
        bodyDef.fixedRotation = true;

        // Create body.
        Body body = world.createBody(bodyDef);
        body.setLinearVelocity(levelData.ship.startingVelocity.x / PPM, levelData.ship.startingVelocity.y / PPM);

        // Create body shape.
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(texture.getWidth() / 2 / PPM, texture.getHeight() / 2 / PPM);

        // Create fixture definition.
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1.0f;
        fixtureDef.friction = 0f;
        fixtureDef.restitution = 0f;

        body.createFixture(fixtureDef);
        shape.dispose();

        ShipEntity ship = new ShipEntity();
        ship.sprite = sprite;
        ship.body = body;

        return ship;
    }
}
