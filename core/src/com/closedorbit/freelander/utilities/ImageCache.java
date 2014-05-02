package com.closedorbit.freelander.utilities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class ImageCache {
    public static TextureAtlas atlas;

    public static void load() {
        String textureFile = "images/spritesheet.atlas";
        atlas = new TextureAtlas(Gdx.files.internal(textureFile));
    }

    public static TextureRegion getTexture(String name) {
        return atlas.findRegion(name);
    }

    public static TextureRegion getFrame (String name, int index) {
        return atlas.findRegion(name, index);
    }
}
