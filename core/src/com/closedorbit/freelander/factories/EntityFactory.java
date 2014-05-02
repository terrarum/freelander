package com.closedorbit.freelander.factories;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.closedorbit.freelander.Freelander;
import com.closedorbit.freelander.entities.RectangleEntity;
import com.closedorbit.freelander.entities.SpriteEntity;
import com.closedorbit.freelander.utilities.ImageCache;

import java.util.HashMap;

import static com.closedorbit.freelander.utilities.Vars.PPM;

public class EntityFactory {

    Freelander game;
    private World world;

    public EntityFactory(Freelander game, World world) {
        this.game = game;
        this.world = world;
    }

    public SpriteEntity createSpriteEntity(float x, float y, String imageName) {
        TextureRegion texture = game.imageCache.getTexture(imageName);
        SpriteEntity entity = new SpriteEntity();
        Sprite sprite = new Sprite(texture, 0, 0, texture.getRegionWidth(), texture.getRegionHeight());
        sprite.setPosition(x, y);
        entity.sprite = sprite;
        return entity;
    }

    public RectangleEntity createRectangleEntity(float x, float y, float w, float h, String imageName) {
        RectangleEntity entity = new RectangleEntity();

        BodyDef boxDef = new BodyDef();
        boxDef.position.set(new Vector2(x / PPM, y / PPM));
        Body boxBody = world.createBody(boxDef);
        PolygonShape boxShape = new PolygonShape();
        boxShape.setAsBox(w / 2 / PPM, h / 2 / PPM);
        boxBody.createFixture(boxShape, 0.0f);
        boxShape.dispose();

        if (imageName != null) {
            TextureRegion texture = game.imageCache.getTexture(imageName);
            int tWidth = texture.getRegionWidth();
            int tHeight = texture.getRegionHeight();
            Sprite sprite = new Sprite(texture, 0, 0, tWidth, tHeight);
            sprite.setPosition(x, y);

            entity.sprite = sprite;
        }

        entity.body = boxBody;

        return entity;
    }

}
