package com.r416alex.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import Screens.MainMenu;

public class Zahrah extends Game {
	public SpriteBatch batch;
	public int G_WIDTH = 416, G_HEIGHT = 234;
	@Override
	public void create () {
		batch = new SpriteBatch();
		this.setScreen(new MainMenu(this));
	}

	@Override
	public void render () {

		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
