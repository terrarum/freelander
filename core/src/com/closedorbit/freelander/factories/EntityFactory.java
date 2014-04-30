package com.closedorbit.freelander.factories;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.closedorbit.freelander.entities.RectangleEntity;
import com.closedorbit.freelander.entities.SpriteEntity;

import static com.closedorbit.freelander.utilities.Vars.PPM;

public class EntityFactory {

    private World world;

    public EntityFactory(World world) {
        this.world = world;
    }

    public SpriteEntity createSpriteEntity(float x, float y, String imageName) {
        SpriteEntity entity = new SpriteEntity();

        Texture texture = new Texture(Gdx.files.internal("images/" + imageName + ".png"));
        int tWidth = texture.getWidth();
        int tHeight = texture.getHeight();
        Sprite sprite = new Sprite(texture, 0, 0, tWidth, tHeight);
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
            Texture texture = new Texture(Gdx.files.internal("images/" + imageName + ".png"));
            int tWidth = texture.getWidth();
            int tHeight = texture.getHeight();
            Sprite sprite = new Sprite(texture, 0, 0, tWidth, tHeight);
            sprite.setPosition(x, y);

            entity.sprite = sprite;
        }

        entity.body = boxBody;

        return entity;
    }

}
