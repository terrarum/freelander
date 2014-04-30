package com.closedorbit.freelander;

import box2dLight.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.closedorbit.freelander.entities.EntityManager;
import com.closedorbit.freelander.entities.PlanetEntity;
import com.closedorbit.freelander.entities.RectangleEntity;
import com.closedorbit.freelander.entities.ShipEntity;
import com.closedorbit.freelander.factories.EntityFactory;
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
    BoundedCamera cam;
    OrthographicCamera parCam;

    Boolean debug = false;

    EntityManager entityManager;
    RayHandler rayHandler;
    Light rocketLight;
    Light padLightLeft;
    Light padLightRight;
    Light spotLight;

    float shipVelocity;
    float shakeRate;
    float shakeX;
    float shakeY;

    public GameLoop(Level levelData, World world, ShipEntity player, BoundedCamera cam) {
        this.levelData = levelData;
        this.world = world;
        this.player = player;
        this.cam = cam;

        entityManager = new EntityManager();

        EntityFactory entFact = new EntityFactory(world);

        RectangleEntity ground = entFact.createRectangleEntity(1, 0 - Vars.V_HEIGHT / 4, Vars.V_WIDTH * 4, Vars.V_HEIGHT / 2, levelData.planet.groundImage);

        entityManager.addEntity(ground);
        // Create sprites for each building. They do not need a box2d body.
        for (PlanetEntity.Building building : levelData.planet.buildings) {
            entityManager.addEntity(entFact.createSpriteEntity(building.position.x, building.position.y, building.image));
        }

        int STARS = 500;
        int i = 0;
        float xMin = -480;
        float xMax = 480;
        float yMin = -300;
        float yMax = 3000;
        while (i < STARS) {
            float x = xMin + (int)(Math.random() * ((xMax - xMin) + 1));
            float y = yMin + (int)(Math.random() * ((yMax - yMin) + 1));
            entityManager.addParEntity(entFact.createSpriteEntity(x, y, "star"));
            i++;
        }

        // box2d lights.
//        RayHandler.setGammaCorrection(true);
//        RayHandler.useDiffuseLight(true);
        rayHandler = new RayHandler(world);
//        rayHandler.setCulling(true);
        rayHandler.setAmbientLight(0.1f, 0.1f, 0.1f, 0.5f);
//        rayHandler.setCombinedMatrix(cam.combined);

        rocketLight = new PointLight(rayHandler, 64);
        rocketLight.setDistance(50);
        rocketLight.setColor(Color.ORANGE);
        rocketLight.attachToBody(player.body, 0, -6);

//        padLightLeft = new PointLight(rayHandler, 64);
//        padLightRight = new PointLight(rayHandler, 64);
//        padLightLeft.setColor(Color.YELLOW);
//        padLightRight.setColor(Color.YELLOW);
//        padLightLeft.setDistance(30 / Vars.PPM);
//        padLightRight.setDistance(30 / Vars.PPM);
//        padLightLeft.setPosition(-90 / Vars.PPM, 1 / Vars.PPM);
//        padLightRight.setPosition(65 / Vars.PPM, 1 / Vars.PPM);

                                                            // distance, x, y, dir, cone
        spotLight = new ConeLight(rayHandler, 64, Color.BLUE, 500 / Vars.PPM, 80 / Vars.PPM, 100 / Vars.PPM, -140, 35);

        // Created bounded Box2d camera.
        parCam = new OrthographicCamera();
        parCam.setToOrtho(false, Vars.V_WIDTH * 2, Vars.V_HEIGHT * 2); // Screen Vars.V_WIDTH and Vars.V_HEIGHT
        b2dCam = new BoundedCamera();
        b2dCam.setToOrtho(false, Vars.V_WIDTH / Vars.PPM, Vars.V_HEIGHT / Vars.PPM);
        b2dCam.setBounds(levelData.planet.bounds.xMin / Vars.PPM, levelData.planet.bounds.xMax / Vars.PPM, levelData.planet.bounds.yMin / Vars.PPM, levelData.planet.bounds.yMax / Vars.PPM);
        b2dCam.update();

        // Create Box2d renderer.
        if (debug) {
            b2dr = new Box2DDebugRenderer();
        }
    }

    public void update() {
        rayHandler.setCombinedMatrix(b2dCam.combined);

        shipVelocity = Math.abs(player.getVelocity().y * Vars.PPM);
        shakeRate = shipVelocity / 100;

        // Input.
        if (Gdx.input.isTouched()) {
            player.body.applyForce(player.thrust, player.body.getWorldCenter(), true);
            rocketLight.setActive(true);
            shakeRate *= 1.5;
        }
        else {
            rocketLight.setActive(false);
            if (shipVelocity < 20) {
                shakeRate = 0;
            }
        }

        shakeX = Vars.getRandom(-shakeRate, shakeRate);
        shakeY = Vars.getRandom(-shakeRate, shakeRate);

        cam.setPosition(player.getPosition().x * Vars.PPM + shakeX, player.getPosition().y * Vars.PPM + shakeY);
        cam.update();

        b2dCam.setPosition(player.getPosition().x + shakeX, player.getPosition().y + shakeY);
        b2dCam.update();

        parCam.position.set(cam.position.x, cam.position.y, 0);
        parCam.update();
    }

    public void render(SpriteBatch sb) {

        sb.setProjectionMatrix(parCam.combined);
        entityManager.renderPar(sb);
        sb.setProjectionMatrix(cam.combined);
        entityManager.render(sb);

        if (debug) {
            b2dr.render(world, b2dCam.combined);
            Gdx.graphics.setTitle("Freelander" + " -- FPS: " + Gdx.graphics.getFramesPerSecond());
        }
        rayHandler.updateAndRender();
    }

    public void dispose() {
        rayHandler.dispose();
    }
}
