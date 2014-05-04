package com.closedorbit.freelander;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.closedorbit.freelander.entities.PlayerEntity;
import com.closedorbit.freelander.entities.ShipEntity;
import com.closedorbit.freelander.utilities.FontBuilder;
import com.closedorbit.freelander.utilities.Vars;

public class GameHUD {

    private Freelander game;

    private Stage stage;
    private Skin skin;
    private Table table;

    private Label altitude;
    private Label velocity;
    private Label thrustX;
    private Label thrustY;
    private Image healthbar;

    PlayerEntity player;

    public GameHUD(Freelander game, PlayerEntity player, Skin skin) {
        this.game = game;
        this.stage = new Stage(new ExtendViewport(Vars.V_WIDTH, Vars.V_HEIGHT));
        this.player = player;
        this.skin = skin;
        table = new Table();
        table.setFillParent(true);
        table.debug();
    }

    public void create() {

        // Create labels.
        altitude = new Label("Freelander", skin, "hud-font");
        velocity = new Label("Freelander", skin, "hud-font");
        thrustX = new Label("10%", skin, "hud-font");
        thrustY = new Label("10%", skin, "hud-font");

        altitude.setAlignment(Align.right);
        velocity.setAlignment(Align.right);

        Table leftTable = new Table();
        leftTable.debug();
        leftTable.add(thrustY).fillX().expandX().padBottom(20);
        leftTable.row();
        leftTable.add(thrustX).fillX().expandX().padTop(20);

        Table rightTable = new Table();
        rightTable.debug();
        rightTable.add(altitude).fillX().expandX().padBottom(20);
        rightTable.row();
        rightTable.add(velocity).fillX().expandX().padTop(20);

        Container fuelBarContainer = new Container();
        Container shipSpace = new Container();
        Container healthBarContainer = new Container();

        table.add(leftTable).left().width(170).padLeft(10).height(100).fillX().expandX();

        table.add(fuelBarContainer).width(10).height(100);

        table.add(shipSpace).width(100).height(100);

        table.add(healthBarContainer).width(10).height(100);

        table.add(rightTable).right().width(170).padRight(10).height(100).fillX().expandX();

        TextureRegionDrawable healthbarDrawable = new TextureRegionDrawable(new TextureRegion(game.imageCache.getTexture("slider")));
        healthbar = new Image(healthbarDrawable);

//        // Create layout.
//        table.setFillParent(true);
//        table.add(altitude).padBottom(30);
//        table.row();
//        table.add(velocity).padTop(30);
//        table.add(healthbar);

        // Add HUD to stage.
        stage.addActor(table);
    }

    public void update() {
        altitude.setText((int) player.getAltitude() + "m");
        velocity.setText((int) Math.abs(player.getVelocity().y * Vars.PPM) + "m/s");
        healthbar.setScaleY(player.getHealth());
    }

    public void render(SpriteBatch sb) {
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
//        Table.drawDebug(stage);
    }
}