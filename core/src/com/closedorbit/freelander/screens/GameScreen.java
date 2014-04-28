package com.closedorbit.freelander.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.closedorbit.freelander.FontBuilder;
import com.closedorbit.freelander.levelPackLoader.Level;

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
    Texture shipTexture;
    Sprite shipSprite;

    Texture groundTexture;
    Sprite groundSprite;

    // HUD
    OrthographicCamera hudCam;
    Skin skin;
    FontBuilder fontBuilder;
    Stage stage;
    Label hud1;
    Label hud2;
    
    float V_WIDTH = 480;
    float V_HEIGHT = 800;

    public GameScreen(Game game, Level levelData) {
        super(game);
        this.levelData = levelData;
        camera = new OrthographicCamera();
        b2dCam = new OrthographicCamera();
        camera.setToOrtho(false, V_WIDTH, V_HEIGHT); // Screen V_WIDTH and V_HEIGHT
        b2dCam.setToOrtho(false, V_WIDTH / PPM, V_HEIGHT / PPM);
    }

    public Body createBox(float x, float y, float w, float h) {
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
        world = new World(levelData.gravity, true);
        debugRenderer = new Box2DDebugRenderer();

        //Ground body.
        createBox(1, 0 - V_HEIGHT / 4, V_WIDTH * 4, V_HEIGHT / 2);

        // Markers.
        createBox(-30, 0,    10, 10);
        createBox(-30, 200,  10, 10);
        createBox(-30, 400,  10, 10);
        createBox(-30, 600,  10, 10);
        createBox(-30, 800,  10, 10);
        createBox(-30, 1000, 10, 10);

        shipTexture = new Texture(Gdx.files.internal("images/dropship.png")); // Probably not using the atlas.
        shipSprite = new Sprite(shipTexture, 0, 0, 24, 45);

        groundTexture = new Texture(Gdx.files.internal("images/groundTile.png"));
        groundTexture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

        shipDef = new BodyDef();
        shipDef.type = BodyDef.BodyType.DynamicBody;
        shipDef.position.set(levelData.ship.x / PPM, (levelData.ship.y + shipSprite.getHeight() / 2) / PPM );

        shipBody = world.createBody(shipDef);
        shipBody.setLinearVelocity(levelData.shipStartingVelocity.x / PPM, levelData.shipStartingVelocity.y / PPM);
        shipShape = new PolygonShape();
        shipShape.setAsBox(24 / 2 / PPM, 45 / 2 / PPM);

        shipFixtureDef = new FixtureDef();
        shipFixtureDef.shape = shipShape;
        shipFixtureDef.density = 1.0f;
        shipFixtureDef.friction = 0.5f;
        shipFixtureDef.restitution = 0f;

        shipFixture = shipBody.createFixture(shipFixtureDef);
        shipShape.dispose();

        camera.position.set(shipDef.position.x, shipDef.position.y - 100, 0);
        camera.update();

        batch = new SpriteBatch();
        batch.getProjectionMatrix().setToOrtho2D(0, 0, V_WIDTH, V_HEIGHT);

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
            Vector2 force = new Vector2(0, 400);
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
        batch.draw(groundTexture, 0 * PPM, 0 * PPM, groundTexture.getWidth() * 3, groundTexture.getHeight() * 3, 0, 3, 3, 0);
        batch.end();

        world.step(1/60f, 6, 2);
    }

    @Override
    public void hide() {
        world.dispose();
    }

}
