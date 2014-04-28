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
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.closedorbit.freelander.FontBuilder;
import com.closedorbit.freelander.levelPackLoader.Level;

import java.text.DecimalFormat;

import static com.closedorbit.freelander.utilities.B2DVars.PPM;

public class GameScreen extends DefaultScreen {

    Batch batch;
    Level levelData;
    World world;
    OrthographicCamera camera;
    OrthographicCamera b2dCam;
    Box2DDebugRenderer debugRenderer;

    BodyDef shipDef;
    Body shipBody;
    PolygonShape shipShape;
    FixtureDef shipFixtureDef;
    Fixture shipFixture;
    Texture shipImage;
    Sprite shipSprite;

    // HUD
    OrthographicCamera hudCam;
    Skin skin;
    FontBuilder fontBuilder;
    Stage stage;
    Label hud1;
    Label hud2;

    public GameScreen(Game game, Level levelData) {
        super(game);
        this.levelData = levelData;
        camera = new OrthographicCamera();
        b2dCam = new OrthographicCamera();
        camera.setToOrtho(false, 480, 800);
        b2dCam.setToOrtho(false, 480 / PPM, 800 / PPM);
    }

    public Body createBox(float w, float h, float x, float y) {
        BodyDef boxDef = new BodyDef();
        boxDef.position.set(new Vector2(x / PPM, y / PPM));
        Body boxBody = world.createBody(boxDef);
        PolygonShape boxShape = new PolygonShape();
        boxShape.setAsBox(w / 2 / PPM, h / 2 / PPM);
        boxBody.createFixture(boxShape, 0.0f);

        return boxBody;
    }

    @Override
    public void show() {
        float gravity = levelData.gravity;
        world = new World(new Vector2(0, -gravity), true);
        debugRenderer = new Box2DDebugRenderer();

        //Ground body.
        createBox(camera.viewportWidth, 10, 0, -5);

        // Markers.
        createBox(10, 10, 0, 0);
        createBox(10, 10, 0, 200);
        createBox(10, 10, 0, 400);
        createBox(10, 10, 0, 600);
        createBox(10, 10, 0, 800);
        createBox(10, 10, 0, 1000);

        shipImage = new Texture(Gdx.files.internal("images/dropship.png"));
        shipSprite = new Sprite(shipImage, 0, 0, 24, 45);

        shipDef = new BodyDef();
        shipDef.type = BodyDef.BodyType.DynamicBody;
        shipDef.position.set(100 / PPM, 200 / PPM);

        shipBody = world.createBody(shipDef);
        shipShape = new PolygonShape();
        shipShape.setAsBox(24 / 2 / PPM, 45 / 2 / PPM);

        shipFixtureDef = new FixtureDef();
        shipFixtureDef.shape = shipShape;
        shipFixtureDef.density = 1.0f;
        shipFixtureDef.friction = 0.5f;
        shipFixtureDef.restitution = 0.1f;

        shipFixture = shipBody.createFixture(shipFixtureDef);
        shipShape.dispose();

        camera.position.set(240, shipDef.position.y - 100, 0);
        camera.update();

        batch = new SpriteBatch();
        batch.getProjectionMatrix().setToOrtho2D(0, 0, 480, 800);

        // HUD.
        stage = new Stage();
        skin = new Skin();
        fontBuilder = new FontBuilder();
        fontBuilder.addFonts(skin);
        skin.load(Gdx.files.internal("skin.json"));

        hud1 = new Label("Freelander", skin, "normal-font");
        hud2 = new Label("Freelander", skin, "normal-font");
        Table table = new Table();
        table.setFillParent(true);
        table.add(hud1).padBottom(20);
        table.row();
        table.add(hud2).padTop(20);
        stage.addActor(table);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (Gdx.input.isTouched()) {
            Vector2 force = new Vector2(0, 450);
            shipBody.applyForce(force, shipBody.getWorldCenter(), true);
        }

        shipSprite.setPosition(shipBody.getPosition().x - 24 / 2, shipBody.getPosition().y - 45 / 2);

        camera.position.set(shipBody.getPosition(), 0);
        camera.update();

        b2dCam.position.set(shipBody.getPosition(), 0);
        b2dCam.update();
        debugRenderer.render(world, b2dCam.combined);

        // HUD.
        float altitude = shipBody.getPosition().y * PPM - shipSprite.getHeight() / 2;
        Vector2 velocity = shipBody.getLinearVelocityFromWorldPoint(new Vector2(0f, 0f));
        hud1.setText("Altitude: " + (int) altitude);
        hud2.setText("Velocity: " + (int) Math.abs(velocity.y * PPM));
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();

        // Matches the batch coordinate system to the camera.
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        shipSprite.draw(batch);
        batch.end();

        world.step(1/60f, 6, 2);
    }

    @Override
    public void hide() {
        world.dispose();
    }

}
