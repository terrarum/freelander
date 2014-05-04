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
    private Image fuelbar;

    private float currentHealth;
    private float currentFuel;

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
        thrustX = new Label(" 10%", skin, "hud-font");
        thrustY = new Label(" 10%", skin, "hud-font");

        // Aligns the label text to the right.
        altitude.setAlignment(Align.right);
        velocity.setAlignment(Align.right);

        int labelPad = 17;

        // Create the left HUD elements.
        Table leftTable = new Table();
        leftTable.debug();
        // The padding here is to make the top and bottom of the text align with
        // the top and bottom of the table.
        leftTable.add(thrustY).fillX().expandX().padBottom(labelPad);
        leftTable.row();
        leftTable.add(thrustX).fillX().expandX().padTop(labelPad);

        // Create the right HUD elements.
        Table rightTable = new Table();
        rightTable.debug();
        rightTable.add(altitude).fillX().expandX().padBottom(labelPad);
        rightTable.row();
        rightTable.add(velocity).fillX().expandX().padTop(labelPad);

        // Create the health and fuel bars. These are single pixel high bars that are scaled up.
        TextureRegionDrawable healthbarDrawable = new TextureRegionDrawable(new TextureRegion(game.imageCache.getTexture("slider-health")));
        TextureRegionDrawable fuelbarDrawable = new TextureRegionDrawable(new TextureRegion(game.imageCache.getTexture("slider-fuel")));
        healthbar = new Image(healthbarDrawable);
        fuelbar = new Image(fuelbarDrawable);

        // Containers for the bars.
        Container fuelBarContainer = new Container(fuelbar).align(Align.bottom);
        Container shipSpace = new Container();
        Container healthBarContainer = new Container(healthbar).align(Align.bottom);

        table.add(leftTable).left().width(100).padLeft(10).height(100).fillX().expandX();

        table.add(fuelBarContainer).width(10).height(100);

        table.add(shipSpace).width(180).height(100);

        table.add(healthBarContainer).width(10).height(100);

        table.add(rightTable).right().width(100).padRight(10).height(100).fillX().expandX();

        // Add HUD to stage.
        stage.addActor(table);
    }

    public void update() {
        altitude.setText((int) player.getAltitude() + "m ");
        velocity.setText((int) Math.abs(player.getVelocity().y * Vars.PPM) + "m/s ");

        currentHealth = player.getHealth() / player.maxHealth * 100;
        currentFuel = player.getFuel() / player.maxFuel * 100;

        healthbar.setScaleY(currentHealth);
        fuelbar.setScaleY(currentFuel);
    }

    public void render(SpriteBatch sb) {
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
//        Table.drawDebug(stage);
    }
}