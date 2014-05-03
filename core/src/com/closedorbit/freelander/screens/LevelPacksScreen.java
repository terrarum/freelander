package com.closedorbit.freelander.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.closedorbit.freelander.Freelander;
import com.closedorbit.freelander.utilities.FontBuilder;
import com.closedorbit.freelander.levelPackLoader.LevelPack;
import com.closedorbit.freelander.levelPackLoader.LevelPackLoader;
import com.closedorbit.freelander.utilities.Vars;

import java.util.ArrayList;

public class LevelPacksScreen extends DefaultScreen {

    Freelander game;
    Stage stage;
    SpriteBatch batch;

    public LevelPacksScreen(Freelander game) {
        super(game);
        this.game = game;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        stage = new Stage(new ExtendViewport(Vars.V_WIDTH, Vars.V_HEIGHT));
        Gdx.input.setInputProcessor(stage);

        batch.getProjectionMatrix().setToOrtho2D(0, 0, Vars.V_WIDTH, Vars.V_HEIGHT);

        // Makes the stage listen to input?
        Gdx.input.setInputProcessor(stage);

        Label title = new Label("Freelander", game.skin, "title-font");
        Label subTitle = new Label("Level Packs", game.skin, "sub-title-font");

        // Create level pack loader.
        LevelPackLoader loader = new LevelPackLoader();
        // Load level packs.
        final ArrayList<LevelPack> levelPacks = loader.loadLevelPacks();

        // Create layout table.
        Table table = new Table();
//        table.debug();
        table.setFillParent(true);
        table.add(title).top().padTop(100);
        table.row();
        table.add(subTitle).top().padTop(20);

        // Create scrollPane and table to go inside it.
        final Table scrollTable = new Table();
//        scrollTable.debug();
        final ScrollPane pane = new ScrollPane(scrollTable, game.skin);

        // Only scroll horizontally.
        pane.setScrollingDisabled(false, true);
        pane.setFlickScroll(true);

        // Add scrollpane to main table.
        table.row();
        table.add(pane).fill().expand();

        // Loop through level packs, add each pack button to the scrollPane table.
        for (final LevelPack pack : levelPacks) {

            TextButton packButton = new TextButton(pack.name, game.skin);
            Container buttonWrapper = new Container(packButton);

            packButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                    game.setScreen(new LevelsScreen(game, pack));
                };
            });
            scrollTable.add(buttonWrapper).width(Vars.V_WIDTH);
        }

        // Add the table to the stage.
        stage.addActor(table);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
//        Table.drawDebug(stage);

        batch.begin();

        batch.end();
    }

    @Override
    public void hide() {
        batch.dispose();
        stage.dispose();
    }
}
