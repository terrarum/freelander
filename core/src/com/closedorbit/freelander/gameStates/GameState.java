package com.closedorbit.freelander.gameStates;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.closedorbit.freelander.GameStateManager;
import com.closedorbit.freelander.screens.GameScreen;

/**
 * Created by terrarum on 28/04/14.
 */
public abstract class GameState {
    protected GameStateManager gsm;
    protected GameScreen game;

    protected SpriteBatch sb;
    protected OrthographicCamera cam;
    protected OrthographicCamera hudCam;

    protected GameState(GameStateManager gsm) {
        this.gsm = gsm;
        game = gsm.game();
        sb = game.getSpriteBatch();
        cam = game.getCamera();
        hudCam = game.getHUDCamera();
    }

    public abstract void update(float dt);
    public abstract void render();
    public abstract void dispose();
}
