package com.closedorbit.freelander.utilities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.closedorbit.freelander.Freelander;

public class FontBuilder {

    BitmapFont titleFont;
    BitmapFont subtitleFont;
    BitmapFont normalFont;
    Freelander game;

    public FontBuilder(Freelander game) {
        this.game = game;
    }

    // Adds
    public Skin addFonts(Skin skin) {
        // Create SciFly generator.
        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/SciFly-Sans.ttf"));

        // Create title font.
        FreeTypeFontGenerator.FreeTypeFontParameter titleParams = new FreeTypeFontGenerator.FreeTypeFontParameter();
        titleParams.size = 80;
        titleFont = fontGenerator.generateFont(titleParams);

        // Create subtitle font.
        FreeTypeFontGenerator.FreeTypeFontParameter subtitleParams = new FreeTypeFontGenerator.FreeTypeFontParameter();
        subtitleParams.size = 60;
        subtitleFont = fontGenerator.generateFont(subtitleParams);

        // Create button label font.
        FreeTypeFontGenerator.FreeTypeFontParameter labelParams = new FreeTypeFontGenerator.FreeTypeFontParameter();
        labelParams.size = 40;
        normalFont = fontGenerator.generateFont(labelParams);

        // Dispose of font generator.
        fontGenerator.dispose();

        // Import texturepacked spritesheet.
        skin.addRegions(new TextureAtlas(Gdx.files.internal("images/spritesheet.atlas")));

        // Add freetype-generated font to it.
        skin.add("title-font", titleFont);
        skin.add("sub-title-font", subtitleFont);
        skin.add("normal-font", normalFont);

        return skin;
    }

    // Dispose of all fonts when done.
    public void dispose() {
        titleFont.dispose();
        subtitleFont.dispose();
        normalFont.dispose();
    }
}
