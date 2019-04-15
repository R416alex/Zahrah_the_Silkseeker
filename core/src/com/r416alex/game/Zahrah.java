package com.r416alex.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import Screens.MainMenu;

public class Zahrah extends Game {
	public boolean DEV_MODE = false;
	public SpriteBatch batch;
	public int G_WIDTH = 416, G_HEIGHT = 234;
	private Player player;
	private FPSLogger logger;
	@Override
	public void create () {
		batch = new SpriteBatch();
		this.setScreen(new MainMenu(this));
		logger = new FPSLogger();
		player = new Player();
	}

	@Override
	public void render () {
		logger.log();
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
