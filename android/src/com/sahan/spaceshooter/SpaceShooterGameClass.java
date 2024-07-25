package com.sahan.spaceshooter;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.assets.AssetManager;
import com.sahan.spaceshooter.screens.LoadingScreen;

public class SpaceShooterGameClass extends Game {
	public SpriteBatch batch;
	public Skin skin;
	public AssetManager assetManager;

	@Override
	public void create() {
		batch = new SpriteBatch();
		skin = new Skin(Gdx.files.internal("ui/skin/uiskin.json"));
		assetManager = new AssetManager();

		assetManager.load("sfx/explosion.wav", Sound.class);

		setScreen(new LoadingScreen(this));
	}

	@Override
	public void render() {
		super.render();
	}

	@Override
	public void dispose() {
		batch.dispose();
		skin.dispose();
		assetManager.dispose();
	}
}