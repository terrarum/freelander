package com.closedorbit.freelander.utilities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class FontBuilder {

    BitmapFont titleFont;
    BitmapFont normalFont;

    public FontBuilder() {

    }

    // Adds
    public Skin addFonts(Skin skin) {
        // Create SciFly generator.
        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/SciFly-Sans.ttf"));

        // Create title font.
        FreeTypeFontGenerator.FreeTypeFontParameter titleParams = new FreeTypeFontGenerator.FreeTypeFontParameter();
        titleParams.size = 80;
        titleParams.characters = "Felandr";
        titleFont = fontGenerator.generateFont(titleParams);

        // Create button label font.
        FreeTypeFontGenerator.FreeTypeFontParameter labelParams = new FreeTypeFontGenerator.FreeTypeFontParameter();
        labelParams.size = 40;
        normalFont = fontGenerator.generateFont(labelParams);
        fontGenerator.dispose();

        // Import texturepacked spritesheet.
        skin.addRegions(new TextureAtlas(Gdx.files.internal("images/spritesheet.atlas")));

        // Add freetype-generated font to it.
        skin.add("title-font", titleFont);
        skin.add("normal-font", normalFont);

        return skin;
    }

    // Dispose of all fonts when done.
    public void dispose() {
        titleFont.dispose();
        normalFont.dispose();
    }
}
