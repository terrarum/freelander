package com.closedorbit.freelander.entities;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by terrarum on 29/04/14.
 */
public class PlanetEntity extends Entity {
    
    public Vector2 gravity;
    public Bounds bounds;
    public String groundImage;

    public static class Bounds {
        public int xMin;
        public int xMax;
        public int yMin;
        public int yMax;
    }
}
