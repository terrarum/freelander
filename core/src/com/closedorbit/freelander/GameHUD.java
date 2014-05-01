package com.closedorbit.freelander;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.closedorbit.freelander.entities.PlayerEntity;
import com.closedorbit.freelander.entities.ShipEntity;
import com.closedorbit.freelander.utilities.FontBuilder;
import com.closedorbit.freelander.utilities.Vars;

public class GameHUD {

    private Stage stage;
    private Skin skin;
    private FontBuilder fontBuilder;
    private Table table;

    private Label altitude;
    private Label velocity;
    private Image healthbar;

    PlayerEntity player;

    public GameHUD(PlayerEntity player) {
        stage = new Stage();
        skin = new Skin();
        this.player = player;
        fontBuilder = new FontBuilder();
        fontBuilder.addFonts(skin);
        skin.load(Gdx.files.internal("skin.json"));
        table = new Table();
    }

    public void create() {
        TextureRegionDrawable healthbarDrawable = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("images/slider.png"))));
        healthbar = new Image(healthbarDrawable);

        // Create labels.
        altitude = new Label("Freelander", skin, "normal-font");
        velocity = new Label("Freelander", skin, "normal-font");

        // Create layout.
        table.setFillParent(true);
        table.add(altitude).padBottom(30);
        table.row();
        table.add(velocity).padTop(30);
        table.add(healthbar);

        // Add HUD to stage.
        stage.addActor(table);
    }

    public void update() {
        altitude.setText((int) player.getAltitude() + "m");
        velocity.setText((int) Math.abs(player.getVelocity().y * Vars.PPM) + "m/s");
//        healthbar.setHeight(player.getHealth());
        healthbar.setScaleY(player.getHealth());
    }

    public void render(SpriteBatch sb) {
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }
}