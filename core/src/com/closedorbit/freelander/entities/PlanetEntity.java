package com.closedorbit.freelander.entities;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

/**
 * Created by terrarum on 29/04/14.
 */
public class PlanetEntity extends Entity {
    
    public Vector2 gravity;
    public Bounds bounds;
    public String groundImage;
    public ArrayList<Building> buildings;

    public static class Bounds {
        public int xMin;
        public int xMax;
        public int yMin;
        public int yMax;
    }

    public static class Building {
        public String name;
        public String image;
        public Vector2 position;
    }
}
