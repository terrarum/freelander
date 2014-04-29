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
import com.closedorbit.freelander.FontBuilder;
import com.closedorbit.freelander.GameLoop;
import com.closedorbit.freelander.entities.EntityManager;
import com.closedorbit.freelander.entities.PlanetEntity;
import com.closedorbit.freelander.entities.RectangleEntity;
import com.closedorbit.freelander.entities.ShipEntity;
import com.closedorbit.freelander.factories.RectangleFactory;
import com.closedorbit.freelander.factories.ShipFactory;
import com.closedorbit.freelander.levelPackLoader.Level;
import com.closedorbit.freelander.utilities.BoundedCamera;
import com.closedorbit.freelander.utilities.Vars;

public class GameScreen extends DefaultScreen {

    GameLoop gameLoop;

    // Sprite renderer.
    SpriteBatch sb;
    // Level data from JSON.
    Level levelData;
    // Box2d world.
    World world;

    // Sprite cam.
    BoundedCamera cam;
    // HUD cam.
    OrthographicCamera hudCam;

    // HUD
    Skin skin;
    FontBuilder fontBuilder;
    Stage stage;
    Label hud1;
    Label hud2;

    PlanetEntity planet;
    ShipEntity player;

    EntityManager entityManager;

    // Time Control.
    private double accumulator;
    private double currentTime;
    private float step = 1f / 60.0f;

    public GameScreen(Game game, Level levelData) {
        super(game);

        this.levelData = levelData;
        this.planet = levelData.planet;

        entityManager = new EntityManager();

        cam = new BoundedCamera();
        cam.setToOrtho(false, Vars.V_WIDTH, Vars.V_HEIGHT); // Screen Vars.V_WIDTH and Vars.V_HEIGHT
        cam.setBounds(planet.bounds.xMin, planet.bounds.xMax, planet.bounds.yMin, planet.bounds.yMax);

        hudCam = new OrthographicCamera();
        hudCam.setToOrtho(false, Vars.V_WIDTH, Vars.V_HEIGHT);

        world = new World(planet.gravity, true);
    }

    @Override
    public void show() {
        ShipFactory shipFact = new ShipFactory(world);
        player = shipFact.createShip(levelData, 0, 0, "images/dropship.png");
        gameLoop = new GameLoop(levelData, world, player);

        RectangleFactory rectFact = new RectangleFactory(world);


        RectangleEntity ground = rectFact.createRectangleEntity(1, 0 - Vars.V_HEIGHT / 4, Vars.V_WIDTH * 4, Vars.V_HEIGHT / 2, planet.groundImage);

        RectangleEntity marker1 = rectFact.createRectangleEntity(-30, 0, 10, 10, "images/marker.png");
        RectangleEntity marker2 = rectFact.createRectangleEntity(100, 100, 10, 10, "images/marker.png");

        entityManager.addEntity(ground);
        entityManager.addEntity(marker1);
        entityManager.addEntity(marker2);

        cam.setPosition(0, 0);
        cam.update();

        sb = new SpriteBatch();
        sb.getProjectionMatrix().setToOrtho2D(0, 0, Vars.V_WIDTH, Vars.V_HEIGHT);

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

        gameLoop.update();
        gameLoop.render();

        // Input.
        if (Gdx.input.isTouched()) {
            player.body.applyForce(player.thrust, player.body.getWorldCenter(), true);
        }

        // Camera follow player.
        cam.setPosition(player.getPosition().x * Vars.PPM, player.getPosition().y * Vars.PPM);
        cam.update();

        // Game.
        sb.setProjectionMatrix(cam.combined);
        player.render(sb);
        entityManager.render(sb);

        // HUD.
        sb.setProjectionMatrix(hudCam.combined);
        float altitude = player.getPosition().y * Vars.PPM - player.sprite.getHeight() / 2;
        Vector2 velocity = player.body.getLinearVelocityFromWorldPoint(new Vector2(0f, 0f));
        hud1.setText((int) altitude + "m");
        hud2.setText((int) Math.abs(velocity.y * Vars.PPM) + "m/s");
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();

        world.step(step, 6, 2);
    }

    @Override
    public void hide() {
        world.dispose();
        gameLoop.dispose();
    }

}
