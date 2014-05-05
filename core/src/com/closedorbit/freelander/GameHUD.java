package com.closedorbit.freelander;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.closedorbit.freelander.entities.PlayerEntity;
import com.closedorbit.freelander.utilities.Vars;

public class GameHUD {

    private Freelander game;

    private Stage stage;
    private Skin skin;
    private Table table;

    private Label altitude;
    private Label thrustY;
    private Label thrustX;
    private Label unknown;
    private Image healthbar;
    private Image fuelbar;
    private Image targetPointer;

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
        thrustY = new Label("Freelander", skin, "hud-font");
        thrustX = new Label(" 10%", skin, "hud-font");
        unknown = new Label(" 10%", skin, "hud-font");

        // Aligns the label text to the right.
        altitude.setAlignment(Align.right);
        thrustY.setAlignment(Align.right);

        int labelPad = 17;

        // Create the left HUD elements.
        Table leftTable = new Table();
        leftTable.debug();
        // The padding here is to make the top and bottom of the text align with
        // the top and bottom of the table.
        leftTable.add(unknown).fillX().expandX().padBottom(labelPad);
        leftTable.row();
        leftTable.add(thrustX).fillX().expandX().padTop(labelPad);

        // Create the right HUD elements.
        Table rightTable = new Table();
        rightTable.debug();
        rightTable.add(altitude).fillX().expandX().padBottom(labelPad);
        rightTable.row();
        rightTable.add(thrustY).fillX().expandX().padTop(labelPad);

        // Create the health and fuel bars. These are single pixel high bars that are scaled up.
        TextureRegionDrawable healthbarDrawable = new TextureRegionDrawable(new TextureRegion(game.imageCache.getTexture("slider-health")));
        TextureRegionDrawable fuelbarDrawable = new TextureRegionDrawable(new TextureRegion(game.imageCache.getTexture("slider-fuel")));
        TextureRegionDrawable targetPointerDrawable = new TextureRegionDrawable(new TextureRegion(game.imageCache.getTexture("pointer1")));

        healthbar = new Image(healthbarDrawable);
        fuelbar = new Image(fuelbarDrawable);
        targetPointer = new Image(targetPointerDrawable);
        targetPointer.setOrigin(targetPointer.getWidth() / 2, targetPointer.getHeight() / 2);

        // Containers for the bars.
        Container fuelBarContainer = new Container(fuelbar).align(Align.bottom);
        Container shipSpace = new Container(targetPointer);
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
        // Calculate angle from ship to target (0, 0).
        float degrees = (float) ((Math.atan2 (0 - player.getPosition().x, -(0 - player.getPosition().y)) * 180.0d / Math.PI) + 180.0f);
        targetPointer.setRotation(degrees);
        // Set
        thrustY.setText(" " + (int) Math.abs(player.getVelocity().y * Vars.PPM) + "m/s ");
        thrustX.setText(" " + (int) Math.abs(player.getVelocity().x * Vars.PPM) + "m/s ");

        altitude.setText((int) player.getAltitude() + "m ");

        currentHealth = player.getHealth() / player.maxHealth * 100;
        currentFuel = player.getFuel() / player.maxFuel * 100;

        healthbar.setScaleY(currentHealth);
        fuelbar.setScaleY(currentFuel);

        unknown.setText(" " + Gdx.graphics.getFramesPerSecond() + "fps");

    }

    public void render(SpriteBatch sb) {
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
//        Table.drawDebug(stage);
    }
}