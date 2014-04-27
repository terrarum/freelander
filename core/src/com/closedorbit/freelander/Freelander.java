package com.closedorbit.freelander;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.scenes.scene2d.Stage;

import com.closedorbit.freelander.screens.LevelPacksScreen;
import com.closedorbit.freelander.screens.MainMenu;

public class Freelander extends Game {

    private Stage stage;

    @Override
	public void create() {
//        setScreen(new MainMenu(this));
        setScreen(new LevelPacksScreen(this));
	}
}
