package com.closedorbit.freelander;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class Freelander implements ApplicationListener {
	SpriteBatch batch;
	Texture img;
    BitmapFont scifly25;

    @Override
	public void create () {
        // Create dropship image.
		batch = new SpriteBatch();
		img = new Texture("images/dropship.png");

        // Create text.
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/SciFly-Sans.ttf"));
        scifly25 = generator.generateFont(25); // font size 25 pixels
        generator.dispose();

        scifly25.setColor(Color.RED);
	}

    @Override
    public void dispose() {
        batch.dispose();
    }

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(img, 20, 20);
        scifly25.draw(batch, "Freelander", 100, 100);
		batch.end();
	}

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }
}
