package com.closedorbit.freelander.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.closedorbit.freelander.Freelander;
import com.closedorbit.freelander.utilities.FontBuilder;
import com.closedorbit.freelander.levelPackLoader.Level;
import com.closedorbit.freelander.levelPackLoader.LevelPack;
import com.closedorbit.freelander.utilities.Vars;

public class LevelsScreen extends DefaultScreen {

    Freelander game;
    LevelPack levelPack;
    Stage stage;
    SpriteBatch batch;

    public LevelsScreen(Freelander game, LevelPack levelPack) {
        super(game);
        this.game = game;
        this.levelPack = levelPack;
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
        Label packTitle = new Label(levelPack.name, game.skin, "sub-title-font");
        Table levelTable = new Table();

        // Create layout table.
        Table table = new Table();
        table.debug();
        table.setFillParent(true);
        table.add(title).top().padTop(60);
        table.row();
        table.add(packTitle).top().padTop(20);

        table.row();
        table.add(levelTable).fill().expand();

        int i = 0;
        int rowCount = 3;
        for (final Level level : levelPack.levels) {

            TextButton levelButton = new TextButton(Integer.toString(level.id), game.skin);
            levelButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    game.setScreen(new GameScreen(game, level));
                }
            });

            if (i % rowCount == 0) {
                levelTable.row();
            }
            levelTable.add(levelButton).width(70).height(70).padRight(20).padBottom(20);

            i++;
        }

        stage.addActor(table);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
        Table.drawDebug(stage);

        batch.begin();

        batch.end();
    }

    @Override
    public void hide() {
        batch.dispose();
        stage.dispose();
    }
}
