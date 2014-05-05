package com.closedorbit.freelander.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.closedorbit.freelander.Freelander;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

        config.width = 480;
        config.height = 800;

        config.vSyncEnabled = false; // Setting to false disables vertical sync

//        config.foregroundFPS = 200; // Setting to 0 disables foreground fps throttling
        config.foregroundFPS = 200; // Setting to 0 disables foreground fps throttling

        config.backgroundFPS = 60; // Setting to 0 disables background fps throttling

        // Pack images for development.
        TexturePacker.Settings settings = new TexturePacker.Settings();
        settings.maxWidth = 4096;
        settings.maxHeight = 4096;
        TexturePacker.process(settings, "images", "images", "spritesheet");

        new LwjglApplication(new Freelander(), config);
	}
}
