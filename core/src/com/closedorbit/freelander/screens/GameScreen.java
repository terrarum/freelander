package com.closedorbit.freelander.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.closedorbit.freelander.levelPackLoader.Level;

public class GameScreen extends DefaultScreen {

    Batch batch;
    Level levelData;
    World world;
    OrthographicCamera camera;
    Box2DDebugRenderer debugRenderer;

    // Box2d/World unit conversion.
    static final float WORLD_TO_BOX = 0.01f;
    static final float BOX_TO_WORLD = 100f;

    BodyDef shipDef;
    Body shipBody;
    PolygonShape shipShape;
    FixtureDef shipFixtureDef;
    Fixture shipFixture;
    Texture shipImage;
    Sprite shipSprite;

    float ConvertToBox(float x){
        return x * WORLD_TO_BOX;
    }

    public GameScreen(Game game, Level levelData) {
        super(game);
        this.levelData = levelData;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 480, 800);
    }

    @Override
    public void show() {
        float gravity = levelData.gravity;
        world = new World(new Vector2(0, -gravity), true);
        debugRenderer = new Box2DDebugRenderer();

        //Ground body
        BodyDef groundBodyDef = new BodyDef();
        groundBodyDef.position.set(new Vector2(0, 10));
        Body groundBody = world.createBody(groundBodyDef);
        PolygonShape groundBox = new PolygonShape();
        groundBox.setAsBox(camera.viewportWidth, 10.0f);
        groundBody.createFixture(groundBox, 0.0f);

        shipImage = new Texture(Gdx.files.internal("images/dropship.png"));
        shipSprite = new Sprite(shipImage, 0, 0, 24, 45);

        shipDef = new BodyDef();
        shipDef.type = BodyDef.BodyType.DynamicBody;
        shipDef.position.set(480 / 2 - 24 / 2, 1500);

        camera.position.set(240, shipDef.position.y - 100, 0);
        camera.update();

        shipBody = world.createBody(shipDef);
        shipShape = new PolygonShape();
        shipShape.setAsBox(24, 44);

        shipFixtureDef = new FixtureDef();
        shipFixtureDef.shape = shipShape;
        shipFixtureDef.density = 1.0f;
        shipFixtureDef.friction = 0.1f;
        shipFixtureDef.restitution = 0.1f;

        shipFixture = shipBody.createFixture(shipFixtureDef);
        shipShape.dispose();

        batch = new SpriteBatch();
        batch.getProjectionMatrix().setToOrtho2D(0, 0, 480, 800);
    }

    @Override
    public void render(float delta) {
//        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (Gdx.input.isTouched()) {
            Vector2 force = new Vector2(0, 14500);
            shipBody.applyForce(force, shipBody.getWorldCenter(), true);
        }

        shipSprite.setPosition(shipBody.getPosition().x - 24 / 2, shipBody.getPosition().y - 45 / 2);

        camera.position.set(shipBody.getPosition(), 0);
        camera.update();

        // Matches the batch coordinate system to the camera.
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        shipSprite.draw(batch);
        batch.end();

        debugRenderer.render(world, camera.combined);
        world.step(1/20f, 6, 2);
    }

    @Override
    public void hide() {
        world.dispose();
    }

}
