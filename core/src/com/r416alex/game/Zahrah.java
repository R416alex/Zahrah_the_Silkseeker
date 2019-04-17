package com.r416alex.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import Screens.MainMenu;
import Utils.Transitions;

public class Zahrah extends Game {
	public boolean DEV_MODE = false;
	public SpriteBatch batch;
	public int G_WIDTH = 416, G_HEIGHT = 234;
	public Player player;
	private FPSLogger logger;
	public Transitions transitions;
	@Override
	public void create () {
		batch = new SpriteBatch();
		this.setScreen(new MainMenu(this));
		logger = new FPSLogger();
		player = new Player();
		transitions = new Transitions(this);
	}

	@Override
	public void render () {
		if(DEV_MODE) {
			logger.log();
		}
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
