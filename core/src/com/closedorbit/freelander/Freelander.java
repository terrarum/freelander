package com.closedorbit.freelander;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.closedorbit.freelander.levelPackLoader.LevelPack;
import com.closedorbit.freelander.levelPackLoader.LevelPackLoader;
import com.closedorbit.freelander.screens.GameScreen;
import com.closedorbit.freelander.screens.LevelPacksScreen;
import com.closedorbit.freelander.screens.MainMenu;
import com.closedorbit.freelander.utilities.FontBuilder;
import com.closedorbit.freelander.utilities.ImageCache;

import java.util.ArrayList;

public class Freelander extends Game {

    private Stage stage;
    public Skin skin;
    public ImageCache imageCache;

    @Override
	public void create() {
        imageCache = new ImageCache();
        imageCache.load();

        skin = new Skin();

        FontBuilder fontBuilder = new FontBuilder();
        fontBuilder.addFonts(skin);
        skin.load(Gdx.files.internal("skin.json"));

//        setScreen(new MainMenu(this));
//            setScreen(new LevelPacksScreen(this));
////            Launch game straight to level 1 of levelpack 1.
            LevelPackLoader loader = new LevelPackLoader();
            ArrayList<LevelPack> levelPacks = loader.loadLevelPacks();
            setScreen(new GameScreen(this, levelPacks.get(0).levels.get(0)));
	}
}
