package com.closedorbit.freelander.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.closedorbit.freelander.LevelPackLoader.LevelPack;
import com.closedorbit.freelander.LevelPackLoader.LevelPackLoader;
import com.closedorbit.freelander.LevelPackLoader.LevelPacks;

import java.util.ArrayList;

public class LevelPacksScreen extends DefaultScreen {

    Skin skin;
    Stage stage;
    SpriteBatch batch;

    BitmapFont titleFont;

    public LevelPacksScreen(Game game) {
        super(game);
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        // Create level pack loader.
        LevelPackLoader loader = new LevelPackLoader();

        // Load level packs.
        ArrayList<LevelPack> levelPacks = loader.loadLevelPacks();

        for (LevelPack pack : levelPacks) {
            System.out.println(pack.name);
        }

        batch.getProjectionMatrix().setToOrtho2D(0, 0, 480, 800);

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();

        batch.begin();

        batch.end();
    }

    @Override
    public void hide() {
        batch.dispose();
        stage.dispose();
    }
}
