package com.closedorbit.freelander;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.scenes.scene2d.Stage;

import com.closedorbit.freelander.levelPackLoader.LevelPack;
import com.closedorbit.freelander.levelPackLoader.LevelPackLoader;
import com.closedorbit.freelander.screens.GameScreen;
import com.closedorbit.freelander.screens.LevelPacksScreen;
import com.closedorbit.freelander.screens.MainMenu;

import java.util.ArrayList;

public class Freelander extends Game {

    private Stage stage;

    @Override
	public void create() {
        setScreen(new MainMenu(this));
//        setScreen(new LevelPacksScreen(this));

        // Launch game straight to level 1 of levelpack 1.
//        LevelPackLoader loader = new LevelPackLoader();
//        ArrayList<LevelPack> levelPacks = loader.loadLevelPacks();
//        setScreen(new GameScreen(this, levelPacks.get(0).levels.get(0)));
	}
}
