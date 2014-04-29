package com.closedorbit.freelander.levelPackLoader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;

import java.util.ArrayList;

public class LevelPackLoader {

    public LevelPackLoader() {

    }

    public ArrayList loadLevelPacks() {
        Json json = new Json();
        json.setElementType(LevelPacks.class, "levelPacks", LevelPack.class);
        LevelPacks levelPacks = json.fromJson(LevelPacks.class, Gdx.files.internal("levels.json"));

        return levelPacks.levelPacks;
    }
}
