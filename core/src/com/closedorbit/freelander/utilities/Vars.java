package com.closedorbit.freelander.utilities;

/**
 * Created by terrarum on 28/04/14.
 */
public class Vars {
    // Four pixels equals one meter.
    public static final float PPM = 4f;

    // Game viewport width and height.
    public static final float V_WIDTH = 480;
    public static final float V_HEIGHT = 800;

    public static float getRandom(float min, float max) {
        return min + (int)(Math.random() * ((max - min) + 1));
    }

    public static float getRandomFloat(float min, float max) {
        return min + (float)Math.random() * ((max - min) + 1);
    }
}
