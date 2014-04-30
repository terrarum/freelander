package com.closedorbit.freelander;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.closedorbit.freelander.entities.ShipEntity;
import com.closedorbit.freelander.utilities.Vars;

public class GameHUD {

    private Stage stage;
    private Skin skin;
    private FontBuilder fontBuilder;
    private Table table;

    private Label altitude;
    private Label velocity;

    public GameHUD() {
        stage = new Stage();
        skin = new Skin();
        fontBuilder = new FontBuilder();
        fontBuilder.addFonts(skin);
        skin.load(Gdx.files.internal("skin.json"));
        table = new Table();
    }

    public void create() {
        // Create labels.
        altitude = new Label("Freelander", skin, "normal-font");
        velocity = new Label("Freelander", skin, "normal-font");

        // Create layout.
        table.setFillParent(true);
        table.add(altitude).padBottom(20);
        table.row();
        table.add(velocity).padTop(20);

        // Add HUD to stage.
        stage.addActor(table);
    }

    public void update(ShipEntity player) {
        altitude.setText((int) player.getAltitude() + "m");
        velocity.setText((int) Math.abs(player.getVelocity().y * Vars.PPM) + "m/s");
    }

    public void render(SpriteBatch sb) {
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }
}