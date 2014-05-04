package com.closedorbit.freelander.screens;

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

    // Time Control.
    private float accumulator = 0;
    private float step = 1f / 60.0f;

    @Override
    public void render(float delta) {

        // Clear the screen.
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        accumulator += delta;

        while (accumulator >= delta) {
            delta = Math.min(delta, 1 / 30f);
            world.step(step, 6, 2);
            accumulator -= step;

            // Update player stats.
            player.update(delta);
            // Update game.
            gameLoop.update(delta);
        }
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
    }

    @Override
    public void hide() {
        world.dispose();
        gameLoop.dispose();
    }

}
