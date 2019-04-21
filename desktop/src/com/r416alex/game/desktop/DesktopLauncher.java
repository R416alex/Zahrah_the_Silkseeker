package com.r416alex.game.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.r416alex.game.Zahrah;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1280;
		config.height = 720;
		//config.fullscreen = true;
		config.title  = "Zahrah the Silkseeker";
		config.addIcon("Misc/Icon_32x32.png", Files.FileType.Internal);


		//config.fullscreen = true;
		new LwjglApplication(new Zahrah(), config);
	}
}
