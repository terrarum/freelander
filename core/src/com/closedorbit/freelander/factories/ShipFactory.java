package com.closedorbit.freelander.factories;

import box2dLight.PointLight;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;
import com.closedorbit.freelander.Freelander;
import com.closedorbit.freelander.entities.PlayerEntity;
import com.closedorbit.freelander.levelPackLoader.Level;

import static com.closedorbit.freelander.utilities.Vars.PPM;

public class ShipFactory {

    Freelander game;
    World world;

    public ShipFactory(Freelander game, World world) {
        this.game = game;
        this.world = world;
    }

    public PlayerEntity createPlayer(Level levelData, float x, float y) {

        TextureRegion texture = game.imageCache.getTexture(levelData.ship.image);
        Sprite shipNormal = new Sprite(texture, 0, 0, texture.getRegionWidth(), texture.getRegionHeight());
        texture = game.imageCache.getTexture(levelData.ship.image + "-damage-light");
        Sprite shipDamagedLight = new Sprite(texture, 0, 0, texture.getRegionWidth(), texture.getRegionHeight());
        texture = game.imageCache.getTexture(levelData.ship.image + "-damage-heavy");
        Sprite shipDamagedHeavy = new Sprite(texture, 0, 0, texture.getRegionWidth(), texture.getRegionHeight());

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
        shape.setAsBox(texture.getRegionWidth() / 2 / PPM, texture.getRegionHeight() / 2 / PPM);

        // Create fixture definition.
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1.0f;
        fixtureDef.friction = 0f;
        fixtureDef.restitution = 0f;

        body.createFixture(fixtureDef);
        shape.dispose();

        PlayerEntity ship = new PlayerEntity();
        body.setUserData(ship);
        ship.sprite = shipNormal;
        ship.spriteNormal = shipNormal;
        ship.spriteDamageLight = shipDamagedLight;
        ship.spriteDamageHeavy = shipDamagedHeavy;
        ship.body = body;

        // This should be using the same ship entity that's being mapped to from the json reader but I did it wrong.
        ship.thrust = levelData.ship.thrust;
        ship.setHealth(levelData.ship.startingHealth);
        ship.maxHealth = levelData.ship.maxHealth;
        ship.setFuel(levelData.ship.startingFuel);
        ship.maxFuel = levelData.ship.maxFuel;
        ship.maxFuelConsumptionRate = levelData.ship.maxFuelConsumptionRate;

        return ship;
    }
}
