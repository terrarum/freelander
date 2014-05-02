package com.closedorbit.freelander;

import box2dLight.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.closedorbit.freelander.entities.*;
import com.closedorbit.freelander.factories.EntityFactory;
import com.closedorbit.freelander.levelPackLoader.Level;
import com.closedorbit.freelander.screens.LevelCompleteScreen;
import com.closedorbit.freelander.utilities.BoundedCamera;
import com.closedorbit.freelander.utilities.Vars;

public class GameLoop {

    // BOx2d camera. Only used for debug.
    BoundedCamera b2dCam;
    Box2DDebugRenderer b2dr;
    PlayerEntity player;

    Level levelData;
    Freelander game;
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

    ContactListener contactListener;
    Boolean canDamage = false;

    public GameLoop(Freelander game, Level levelData, World world, final PlayerEntity player, BoundedCamera cam) {
        this.levelData = levelData;
        this.game = game;
        this.world = world;
        this.player = player;
        this.cam = cam;

        entityManager = new EntityManager();

        EntityFactory entFact = new EntityFactory(game, world);

        final RectangleEntity ground = entFact.createRectangleEntity(1, 0 - Vars.V_HEIGHT / 4, Vars.V_WIDTH * 4, Vars.V_HEIGHT / 2, levelData.planet.groundImage);

        entityManager.addEntity(ground);
        // Create sprites for each building. They do not need a box2d body.
        for (PlanetEntity.Building building : levelData.planet.buildings) {
            entityManager.addEntity(entFact.createSpriteEntity(building.position.x, building.position.y, building.image));
        }

        int STARS = 500;
        int i = 0;
        float xMin = levelData.planet.bounds.xMin;
        float xMax = levelData.planet.bounds.xMax;
        float yMin = levelData.planet.bounds.yMin;
        float yMax = levelData.planet.bounds.yMax + 400;
        while (i < STARS) {
            float x = xMin + (int)(Math.random() * ((xMax - xMin) + 1));
            float y = yMin + (int)(Math.random() * ((yMax - yMin) + 1));
            entityManager.addParEntity(entFact.createSpriteEntity(x, y, "star"));
            i++;
        }

        // box2d lights.
        rayHandler = new RayHandler(world);
        rayHandler.setAmbientLight(0.1f, 0.1f, 0.1f, 0.5f);

        rocketLight = new PointLight(rayHandler, 64);
        rocketLight.setDistance(50);
        rocketLight.setColor(Color.ORANGE);
        rocketLight.attachToBody(player.body, 0, -6);

                                                            // distance, x, y, dir, cone
        spotLight = new ConeLight(rayHandler, 64, Color.BLUE, 500 / Vars.PPM, 80 / Vars.PPM, 100 / Vars.PPM, -140, 35);

        // Created bounded Box2d camera.
        parCam = new OrthographicCamera();
        parCam.setToOrtho(false, Vars.V_WIDTH * 2, Vars.V_HEIGHT * 2); // Screen Vars.V_WIDTH and Vars.V_HEIGHT
        b2dCam = new BoundedCamera();
        b2dCam.setToOrtho(false, Vars.V_WIDTH / Vars.PPM, Vars.V_HEIGHT / Vars.PPM);
        b2dCam.setBounds(levelData.planet.bounds.xMin / Vars.PPM, levelData.planet.bounds.xMax / Vars.PPM, levelData.planet.bounds.yMin / Vars.PPM, levelData.planet.bounds.yMax / Vars.PPM);
        b2dCam.update();

        contactListener = new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                final Fixture c1 = contact.getFixtureA();
                final Fixture c2 = contact.getFixtureB();
                if (c1 != null && c2 != null) {
                    if ((c1.getBody() == player.body || c2.getBody() == player.body) &&
                            (c1.getBody() == ground.body || c2.getBody() == ground.body)) {
                        canDamage = true;
                    }
                }
            }

            @Override
            public void endContact(Contact contact) {

            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {
                if (canDamage) {
                    System.out.println(impulse.getNormalImpulses()[0]);
                    float impact = impulse.getNormalImpulses()[0];
                    if (impact < 200) {
                        System.out.println("Safe");
                        player.landed = true;
                    }
                    else if (impact < 1500) {
                        System.out.println("Damage");
                        float newHealth = player.reduceHealth(impact / 1500 * 100);
                        if (newHealth == 0) {
                            System.out.println("Destroyed!");
                        }
                        else {
                            player.landed = true;
                        }
                    }
                    else {
                        player.setHealth(0);
                    }
                    canDamage = false;

                    float currentHealth = player.getHealth();

                    if (currentHealth < 100 && currentHealth >= 40) {
                        player.sprite = player.spriteDamageLight;
                    }
                    else if (currentHealth < 40) {
                        player.sprite = player.spriteDamageHeavy;
                    }
                }
            }
        };

        world.setContactListener(contactListener);

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
            int touchX = Gdx.input.getX();
            int touchY = Gdx.input.getY();

            float screenH = Gdx.graphics.getHeight();
            float screenW = Gdx.graphics.getWidth();

            // 0/0 is the top left, so flip the Y axis.
            float touchW = touchX / screenW * 100;
            float touchH = (screenH - touchY) / screenH * 100;

//            System.out.println(touchH + "/" + touchY);

            float thrustX;
            float thrustY; // heehee, thrusty

            // Percentage of the screen from the bottom that should allow fine control of thrust.
            int touchBoxTop = 33;

            // Calculate horizontal thrust force.
            int deadGap = 10; // Middle percentage that gives no horizontal thrust. @TODO might need some friction to reduce horizontal motion.
            // If between 0% and (50% - half of the deadgap), go left;
            if (touchW < (50 - deadGap / 2)) {
                thrustX = -player.thrust.x;
            }
            // If between (50% + half of the deadgap) and 100%, go right:
            else if (touchW > (50 + deadGap / 2)) {
                thrustX = player.thrust.x;
            }
            // Otherwise, no horizontal motion.
            else {
                thrustX = 0;
            }

            // Calculate vertical thrust force.
            // If the player touches above this point, use full thrust.
            if (touchH >= touchBoxTop) {
                thrustY = player.thrust.y;
            }
            // If they are within the control area
            else {
                // Get percentage of touch in bottom third area.
                float subH = touchH / touchBoxTop * 100;
                // Apply that percentage to the ship's max thrust.
                thrustY = player.thrust.y * (subH / 100);
            }

            player.body.applyForce(new Vector2(thrustX, thrustY), player.body.getWorldCenter(), true);
            rocketLight.setActive(true);
            shakeRate *= 1.5;
            shakeX = Vars.getRandom(-shakeRate, shakeRate);
            shakeY = Vars.getRandom(-shakeRate, shakeRate);
        }
        else {
            rocketLight.setActive(false);
            if (shipVelocity < 20) {
                shakeRate = 0;
            }
        }

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

        Gdx.graphics.setTitle("Freelander" + " -- FPS: " + Gdx.graphics.getFramesPerSecond());
        if (debug) {
            b2dr.render(world, b2dCam.combined);
        }
        rayHandler.updateAndRender();
    }

    public void dispose() {
        rayHandler.dispose();
    }
}
