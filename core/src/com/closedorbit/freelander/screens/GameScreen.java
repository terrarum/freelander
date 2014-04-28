package com.closedorbit.freelander.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.closedorbit.freelander.GameStateManager;
import com.closedorbit.freelander.levelPackLoader.Level;

public class GameScreen extends DefaultScreen {

    Level levelData;

    // Set up viewport sizes and Box2d step rate.
    public static final int V_WIDTH = 480;
    public static final int V_HEIGHT = 800;
    public static final int SCALE = 1;
    // Equates to 60fps.
    public static final float STEP = 1 / 60f;

    // Accumulates time since start.
    private float accum;

    private SpriteBatch sb;
    private OrthographicCamera cam;
    private OrthographicCamera hudCam;

    private GameStateManager gsm;

    public GameScreen(Game game, Level levelData) {
        super(game);
        this.levelData = levelData;
    }

    @Override
    public void show() {
        sb = new SpriteBatch();
        cam = new OrthographicCamera();
        hudCam = new OrthographicCamera();
        cam.setToOrtho(false, V_WIDTH, V_HEIGHT);
        hudCam.setToOrtho(false, V_WIDTH, V_HEIGHT);

        gsm = new GameStateManager(this);
    }

    @Override
    public void render(float delta) {
        accum += Gdx.graphics.getDeltaTime();
        while (accum < STEP) {
            accum -= STEP;
            gsm.update(STEP);
            gsm.render();
        }
    }

    @Override
    public void hide() {

    }

    public SpriteBatch getSpriteBatch() { return sb; }
    public OrthographicCamera getCamera() { return cam; }
    public OrthographicCamera getHUDCamera() { return hudCam; }

    public void resize(int w, int h) {}

    public void pause() {}

    public void resume() {}

}
