package com.closedorbit.freelander.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.*;
import com.closedorbit.freelander.Freelander;
import com.closedorbit.freelander.GameHUD;
import com.closedorbit.freelander.GameLoop;
import com.closedorbit.freelander.entities.PlanetEntity;
import com.closedorbit.freelander.entities.PlayerEntity;
import com.closedorbit.freelander.factories.ShipFactory;
import com.closedorbit.freelander.levelPackLoader.Level;
import com.closedorbit.freelander.utilities.BoundedCamera;
import com.closedorbit.freelander.utilities.Vars;

public class GameScreen extends DefaultScreen {

    Freelander game;
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

    GameHUD hud;
    PlanetEntity planet;
    PlayerEntity player;

    // Time Control.
    private double accumulator;
    private double currentTime;
    private float step = 1f / 60.0f;

    public GameScreen(Freelander game, Level levelData) {
        super(game);
        this.game = game;

        this.levelData = levelData;
        this.planet = levelData.planet;

        cam = new BoundedCamera();
        cam.setToOrtho(false, Vars.V_WIDTH, Vars.V_HEIGHT); // Screen Vars.V_WIDTH and Vars.V_HEIGHT
        cam.setBounds(planet.bounds.xMin, planet.bounds.xMax, planet.bounds.yMin, planet.bounds.yMax);

        hudCam = new OrthographicCamera();
        hudCam.setToOrtho(false, Vars.V_WIDTH, Vars.V_HEIGHT);

        world = new World(planet.gravity, true);
    }

    @Override
    public void show() {
        ShipFactory shipFact = new ShipFactory(game, world);
        player = shipFact.createPlayer(levelData, 0, 0);
        gameLoop = new GameLoop(game, levelData, world, player, cam);

        hud = new GameHUD(game, player, game.skin);
        hud.create();

        sb = new SpriteBatch();
        sb.getProjectionMatrix().setToOrtho2D(0, 0, Vars.V_WIDTH, Vars.V_HEIGHT);
    }

    @Override
    public void render(float delta) {

        if (player.getHealth() == 0) {
            game.setScreen(new LevelCompleteScreen(game, "You crashed.\nEverybody is dead."));
        }

        System.out.println(player.body.getPosition().x);

        // If the player is no longer moving.
        if (player.body.getLinearVelocity().y == 0 && player.body.getLinearVelocity().x == 0) {
            if (player.body.getPosition().y < 10 && player.body.getPosition().x * Vars.PPM > -15 && player.body.getPosition().x * Vars.PPM < 25) {
                game.setScreen(new LevelCompleteScreen(game, "You did it!"));
            }
        }

        // Clear the screen.
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Update player stats.
        player.update();
        // Update game.
        gameLoop.update();
        // Update HUD.
        hud.update();

        // Render the game.
        sb.setProjectionMatrix(cam.combined);
        gameLoop.render(sb);

        // Should really be handled by the entity manager.
        player.render(sb);

        // Render the HUD.
        sb.setProjectionMatrix(hudCam.combined);
        hud.render(sb);

        world.step(step, 6, 2);
    }

    @Override
    public void hide() {
//        world.dispose();
//        gameLoop.dispose();
    }

}
