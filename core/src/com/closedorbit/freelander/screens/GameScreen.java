package com.closedorbit.freelander.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.TimeUtils;
import com.closedorbit.freelander.FontBuilder;
import com.closedorbit.freelander.entities.PlanetEntity;
import com.closedorbit.freelander.entities.RectangleEntity;
import com.closedorbit.freelander.entities.ShipEntity;
import com.closedorbit.freelander.factories.RectangleFactory;
import com.closedorbit.freelander.factories.ShipFactory;
import com.closedorbit.freelander.levelPackLoader.Level;
import com.closedorbit.freelander.utilities.BoundedCamera;

import static com.closedorbit.freelander.utilities.B2DVars.PPM;

public class GameScreen extends DefaultScreen {

    SpriteBatch sb;
    Level levelData;
    World world;

    BoundedCamera b2dCam;
    Box2DDebugRenderer debugRenderer;

    BoundedCamera cam;
    OrthographicCamera hudCam;

    // HUD
    Skin skin;
    FontBuilder fontBuilder;
    Stage stage;
    Label hud1;
    Label hud2;

    PlanetEntity planet;
    ShipEntity player;
    RectangleEntity ground;
    RectangleEntity marker1;
    RectangleEntity marker2;

    // Time Control.
    private double accumulator;
    private double currentTime;
    private float step = 1f / 60.0f;

    float V_WIDTH = 480;
    float V_HEIGHT = 800;

    public GameScreen(Game game, Level levelData) {
        super(game);
        this.levelData = levelData;
        this.planet = levelData.planet;

        cam = new BoundedCamera();
        cam.setToOrtho(false, V_WIDTH, V_HEIGHT); // Screen V_WIDTH and V_HEIGHT
        cam.setBounds(planet.bounds.xMin, planet.bounds.xMax, planet.bounds.yMin, planet.bounds.yMax);

        hudCam = new OrthographicCamera();
        hudCam.setToOrtho(false, V_WIDTH, V_HEIGHT);

        b2dCam = new BoundedCamera();
        b2dCam.setToOrtho(false, V_WIDTH / PPM, V_HEIGHT / PPM);
        b2dCam.setBounds(planet.bounds.xMin / PPM, planet.bounds.xMax / PPM, planet.bounds.yMin / PPM, planet.bounds.yMax / PPM);

        world = new World(planet.gravity, true);
    }

    @Override
    public void show() {
        debugRenderer = new Box2DDebugRenderer();

        RectangleFactory rectFact = new RectangleFactory(world);
        ShipFactory shipFact = new ShipFactory(world);

        player = shipFact.createShip(levelData, 0, 0, "images/dropship.png");

        ground = rectFact.createRectangleEntity(1, 0 - V_HEIGHT / 4, V_WIDTH * 4, V_HEIGHT / 2, planet.groundImage);

        marker1 = rectFact.createRectangleEntity(-30, 0, 10, 10, "images/marker.png");
        marker2 = rectFact.createRectangleEntity(100, 100, 10, 10, "images/marker.png");

//        cam.position.set(player.getPosition().x, player.getPosition().y - 100, 0);
        cam.setPosition(0, 0);
        cam.update();
        b2dCam.setPosition(0 / PPM, 0 / PPM);
        b2dCam.update();

        sb = new SpriteBatch();
        sb.getProjectionMatrix().setToOrtho2D(0, 0, V_WIDTH, V_HEIGHT);

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

        // Input.
        if (Gdx.input.isTouched()) {
            Vector2 force = new Vector2(0, 400);
            player.body.applyForce(force, player.body.getWorldCenter(), true);
        }

        // Camera follow player.
//        cam.setPosition(player.getPosition().x * PPM + V_WIDTH / 4, player.getPosition().y * PPM + V_HEIGHT / 4);
        cam.setPosition(player.getPosition().x * PPM, player.getPosition().y * PPM);
        cam.update();

        // Box2d debug.
//        b2dCam.setPosition(player.getPosition().x + V_WIDTH / 4 / PPM, player.getPosition().y + V_HEIGHT / 4 / PPM);
        b2dCam.setPosition(player.getPosition().x, player.getPosition().y);
        b2dCam.update();
        debugRenderer.render(world, b2dCam.combined);

        // Game.
        sb.setProjectionMatrix(cam.combined);
        player.render(sb);
        sb.begin();
        sb.draw(marker1.sprite, marker1.body.getPosition().x * PPM - marker1.sprite.getWidth() / 2, marker1.body.getPosition().y * PPM - marker1.sprite.getHeight() / 2);
        sb.draw(marker2.sprite, marker2.body.getPosition().x * PPM - marker2.sprite.getWidth() / 2, marker2.body.getPosition().y * PPM - marker2.sprite.getHeight() / 2);
        sb.draw(ground.sprite, ground.body.getPosition().x * PPM - ground.sprite.getWidth() / 2, ground.body.getPosition().y * PPM - ground.sprite.getHeight() / 2);
        sb.end();

        // HUD.
        sb.setProjectionMatrix(hudCam.combined);
        float altitude = player.getPosition().y * PPM - player.sprite.getHeight() / 2;
        Vector2 velocity = player.body.getLinearVelocityFromWorldPoint(new Vector2(0f, 0f));
        hud1.setText((int) altitude + "m");
        hud2.setText((int) Math.abs(velocity.y * PPM) + "m/s");
//        hud2.setText("Pos: " + (int) cam.position.x + "/" + (int) cam.position.y);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();

        world.step(step, 6, 2);
    }

    @Override
    public void hide() {
        world.dispose();
    }

}
