package com.closedorbit.freelander;

import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.closedorbit.freelander.entities.ShipEntity;
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

    public GameLoop(Level levelData, World world, ShipEntity player) {
        this.levelData = levelData;
        this.world = world;
        this.player = player;

        // Created bounded Box2d camera.
        b2dCam = new BoundedCamera();
        b2dCam.setToOrtho(false, Vars.V_WIDTH / Vars.PPM, Vars.V_HEIGHT / Vars.PPM);
        b2dCam.setBounds(levelData.planet.bounds.xMin / Vars.PPM, levelData.planet.bounds.xMax / Vars.PPM, levelData.planet.bounds.yMin / Vars.PPM, levelData.planet.bounds.yMax / Vars.PPM);
        b2dCam.setPosition(0 / Vars.PPM, 0 / Vars.PPM);
        b2dCam.update();

        // Create Box2d renderer.
        b2dr = new Box2DDebugRenderer();
    }

    public void update() {
        b2dCam.setPosition(player.getPosition().x, player.getPosition().y);
        b2dCam.update();
    }

    public void render() {
        b2dr.render(world, b2dCam.combined);
    }

    public void dispose() {
        b2dr.dispose();
    }
}
