package com.closedorbit.freelander.screens;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.closedorbit.freelander.Freelander;
import com.closedorbit.freelander.utilities.Vars;

public class MainMenu extends DefaultScreen {

    Freelander game;
    Stage stage;
    SpriteBatch sb;

    public MainMenu(Freelander game) {
        super(game);
        this.game = game;
    }

    @Override
    public void show() {
        sb = new SpriteBatch();
        // No scaling
//        stage = new Stage();
        // Stretches stage to fix the screen. Will change aspect ratio.
//        stage = new Stage(new StretchViewport(Vars.V_WIDTH, Vars.V_HEIGHT));
        // Scales while maintaining aspect ratio. Will show black bars if required.
//        stage = new Stage(new FitViewport(Vars.V_WIDTH, Vars.V_HEIGHT));
        // Scales until width or height hits the screen size, expands view for the shorter dimension. Maintains aspect ratio.
        stage = new Stage(new ExtendViewport(Vars.V_WIDTH, Vars.V_HEIGHT));
        Gdx.input.setInputProcessor(stage);

        sb.getProjectionMatrix().setToOrtho2D(0, 0, Vars.V_WIDTH, Vars.V_HEIGHT);

        // Makes the stage listen to input?
        Gdx.input.setInputProcessor(stage);

        // Create UI pieces.
        Label title = new Label("Freelander", game.skin, "title-font");
        TextButton playButton = new TextButton("Play", game.skin);
        TextButton optionsButton = new TextButton("Options", game.skin);

        // Create a layout table.
        Table table = new Table();
        table.setFillParent(true);
        table.add(title).expand().top().padTop(60);
        table.row();
        table.add(playButton).padBottom(100);
        table.row();
        table.add(optionsButton).padBottom(200);
//        table.debug();
        stage.addActor(table);

        playButton.addListener(new ChangeListener() {
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
            game.setScreen(new LevelPacksScreen(game));
        };
    });
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
//        Table.drawDebug(stage);

        sb.begin();

        sb.end();
    }

    @Override
    public void hide() {
        // Screen stuff.
        sb.dispose();
        stage.dispose();
    }
}
