package com.closedorbit.freelander.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.closedorbit.freelander.Freelander;
import com.closedorbit.freelander.utilities.Vars;

public class LevelCompleteScreen extends DefaultScreen {

    Freelander game;
    Stage stage;
    SpriteBatch batch;
    String message;

    public LevelCompleteScreen(Freelander game, String message) {
        super(game);
        this.game = game;
        this.message = message;
    }

    public void show() {
        batch = new SpriteBatch();
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        batch.getProjectionMatrix().setToOrtho2D(0, 0, Vars.V_WIDTH, Vars.V_HEIGHT);

        // Makes the stage listen to input?
        Gdx.input.setInputProcessor(stage);

        // Create UI pieces.
        Label status = new Label(message, game.skin, "normal-font");

        // Create a layout table.
        Table table = new Table();
        table.setFillParent(true);
        table.add(status);
        stage.addActor(table);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void hide() {
        batch.dispose();
        stage.dispose();
    }
}
