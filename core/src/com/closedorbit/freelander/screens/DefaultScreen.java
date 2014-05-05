package com.closedorbit.freelander.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.closedorbit.freelander.Freelander;

public abstract class DefaultScreen implements Screen, InputProcessor {
    final Freelander game;
    public InputMultiplexer inputMultiplexer;

    public DefaultScreen(Freelander game) {
        this.game = game;
        inputMultiplexer = new InputMultiplexer();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }

    public boolean keyDown(int keycode) {
        return true;
    }
    public boolean keyUp(int keycode) {
        return true;
    }
    public boolean keyTyped(char character) {
        return true;
    }
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return true;
    }
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return true;
    }
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return true;
    }
    public boolean mouseMoved(int screenX, int screenY) {
        return true;
    }
    public boolean scrolled(int amount) {
        return true;
    }
}