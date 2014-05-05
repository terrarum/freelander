package com.closedorbit.freelander.screens;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.closedorbit.freelander.Freelander;
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

        batch.getProjectionMatrix().setToOrtho2D(0, 0, Vars.V_WIDTH, Vars.V_HEIGHT);

        inputMultiplexer.addProcessor(stage);
        inputMultiplexer.addProcessor(this);
        Gdx.input.setInputProcessor(inputMultiplexer);

        Label title = new Label("Freelander", game.skin, "title-font");
        Label subTitle = new Label("Level Packs", game.skin, "sub-title-font");

        // Create level pack loader.
        LevelPackLoader loader = new LevelPackLoader();
        // Load level packs.
        final ArrayList<LevelPack> levelPacks = loader.loadLevelPacks();

        // Create layout table.
        Table table = new Table();
        table.debug();
        table.setFillParent(true);
        table.add(title).top().padTop(60);
        table.row();
        table.add(subTitle).top().padTop(20);

        // Create scrollPane and table to go inside it.
        final Table scrollTable = new Table();
        scrollTable.debug();
        scrollTable.padLeft(75).padRight(75);
        final ScrollPane pane = new ScrollPane(scrollTable, game.skin);

        // Only scroll horizontally.
        pane.setScrollingDisabled(false, true);
        pane.setFlickScroll(true);

        // Add scrollpane to main table.
        table.row();
        table.add(pane).fill().expand();

        // Loop through level packs, add each pack button to the scrollPane table.
        int i = 0;
        for (final LevelPack pack : levelPacks) {

            TextButton packButton = new TextButton(pack.name, game.skin);
            packButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                    game.setScreen(new LevelsScreen(game, pack));
                };
            });

            Container buttonWrapper;

            if (i == 0) {
                buttonWrapper = new Container(packButton).width(300);//.padLeft(150);
//                buttonWrapper.padLeft(150);
            }
            else if (i == levelPacks.size() - 1) {
                buttonWrapper = new Container(packButton).width(300);//.padRight(150);
//                buttonWrapper.padRight(150);
            }
            else {
                buttonWrapper = new Container(packButton).width(300);
            }
            scrollTable.add(buttonWrapper).width(Vars.V_WIDTH - 150);

            i++;
        }

        // Add the table to the stage.
        stage.addActor(table);
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.BACK) {
            game.setScreen(new MainMenu(game));
        }
        return true;
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
