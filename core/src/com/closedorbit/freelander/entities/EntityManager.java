package com.closedorbit.freelander.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;

import static com.closedorbit.freelander.utilities.Vars.PPM;

/*
    Entity Manager. Maintains a list of Entities and renders them.
 */

public class EntityManager {
    private ArrayList<Entity> entities;
    private ArrayList<Entity> parEntities;

    // Create new ArrayList.
    public EntityManager() {
        entities = new ArrayList<Entity>();
        parEntities = new ArrayList<Entity>();
    }

    // Add an entity to the manager.
    public void addEntity(Entity entity) {
        entities.add(entity);
    }

    // Add an entity to the manager.
    public void addParEntity(Entity entity) {
        parEntities.add(entity);
    }

    // Render all of the managed entities.
    public void render(SpriteBatch sb) {
        for (Entity entity : entities) {
            sb.begin();
            // Draw sprite-only entity.
            if (entity.body == null) {
                sb.draw(entity.sprite, entity.sprite.getX(), entity.sprite.getY());
            }
            // Draw box2d entity.
            else {
                sb.draw(entity.sprite, entity.body.getPosition().x * PPM - entity.sprite.getWidth() / 2, entity.body.getPosition().y * PPM - entity.sprite.getHeight() / 2);
            }
            sb.end();
        }
    }

    // Render all parallax entities.
    public void renderPar(SpriteBatch sb) {
        sb.begin();
        for (Entity entity : parEntities) {
            sb.draw(entity.sprite, entity.sprite.getX(), entity.sprite.getY());
        }
        sb.end();
    }

}
