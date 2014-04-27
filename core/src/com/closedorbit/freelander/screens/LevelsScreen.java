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
import com.closedorbit.freelander.FontBuilder;
import com.closedorbit.freelander.levelPackLoader.Level;
import com.closedorbit.freelander.levelPackLoader.LevelPack;

public class LevelsScreen extends DefaultScreen {

    LevelPack levelPack;
    Skin skin;
    Stage stage;
    SpriteBatch batch;
    FontBuilder fontBuilder;

    public LevelsScreen(Game game, LevelPack levelPack) {
        super(game);
        this.levelPack = levelPack;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        batch.getProjectionMatrix().setToOrtho2D(0, 0, 480, 800);

        // Create UI.
        skin = new Skin();

        // Add fonts to skin.
        fontBuilder = new FontBuilder();
        fontBuilder.addFonts(skin);

        // Load ui skin for use now that fonts have been added.
        skin.load(Gdx.files.internal("skin.json"));

        // Makes the stage listen to input?
        Gdx.input.setInputProcessor(stage);

        Label title = new Label("Freelander", skin, "title-font");
        Label packTitle = new Label(levelPack.name, skin, "normal-font");

        // Create layout table.
        Table table = new Table();
        table.setFillParent(true);
        table.add(title).expand().top().padTop(100);

        for (final Level level : levelPack.levels) {
            table.row();

            TextButton levelButton = new TextButton(level.name, skin);

            levelButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    game.setScreen(new GameScreen(game, level));
                }
            });

            table.add(levelButton).width(300).padBottom(20);
        }

        table.row();
        table.add(packTitle).padTop(50);

        stage.addActor(table);
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
        fontBuilder.dispose();
        batch.dispose();
        stage.dispose();
    }
}
