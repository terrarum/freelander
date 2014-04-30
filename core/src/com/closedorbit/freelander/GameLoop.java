package com.closedorbit.freelander;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.closedorbit.freelander.entities.EntityManager;
import com.closedorbit.freelander.entities.RectangleEntity;
import com.closedorbit.freelander.entities.ShipEntity;
import com.closedorbit.freelander.factories.RectangleFactory;
import com.closedorbit.freelander.levelPackLoader.Level;
import com.closedorbit.freelander.utilities.BoundedCamera;
import com.closedorbit.freelander.utilities.Vars;

public class GameLoop {

    // BOx2d camera. Only used for debug.
    BoundedCamera b2dCam;
    Box2DDebugRenderer b2dr;
    ShipEntity player;

    Level levelData;
    World world;

    EntityManager entityManager;

    public GameLoop(Level levelData, World world, ShipEntity player) {
        this.levelData = levelData;
        this.world = world;
        this.player = player;

        entityManager = new EntityManager();

        RectangleFactory rectFact = new RectangleFactory(world);

        RectangleEntity ground = rectFact.createRectangleEntity(1, 0 - Vars.V_HEIGHT / 4, Vars.V_WIDTH * 4, Vars.V_HEIGHT / 2, levelData.planet.groundImage);

        entityManager.addEntity(ground);
        entityManager.addEntity(rectFact.createRectangleEntity(-30, 0, 10, 10, "images/marker.png"));
        entityManager.addEntity(rectFact.createRectangleEntity(100, 100, 10, 10, "images/marker.png"));

        // Created bounded Box2d camera.
        b2dCam = new BoundedCamera();
        b2dCam.setToOrtho(false, Vars.V_WIDTH / Vars.PPM, Vars.V_HEIGHT / Vars.PPM);
        b2dCam.setBounds(levelData.planet.bounds.xMin / Vars.PPM, levelData.planet.bounds.xMax / Vars.PPM, levelData.planet.bounds.yMin / Vars.PPM, levelData.planet.bounds.yMax / Vars.PPM);
        b2dCam.update();

        // Create Box2d renderer.
        b2dr = new Box2DDebugRenderer();
    }

    public void update() {
        b2dCam.setPosition(player.getPosition().x, player.getPosition().y);
        b2dCam.update();

        // Input.
        if (Gdx.input.isTouched()) {
            player.body.applyForce(player.thrust, player.body.getWorldCenter(), true);
        }
    }

    public void render(SpriteBatch sb) {

        entityManager.render(sb);

        b2dr.render(world, b2dCam.combined);
    }

    public void dispose() {
        b2dr.dispose();
    }
}
