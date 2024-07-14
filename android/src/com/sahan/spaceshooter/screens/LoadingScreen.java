package com.sahan.spaceshooter.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.sahan.spaceshooter.SpaceShooterGameClass;

public class LoadingScreen implements Screen {
    private SpaceShooterGameClass game;
    private BitmapFont font;

    public LoadingScreen(SpaceShooterGameClass game) {
        this.game = game;
        font = new BitmapFont();
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.assetManager.update();
        float progress = game.assetManager.getProgress();

        game.batch.begin();
        font.draw(game.batch, "Loading... " + (int)(progress * 100) + "%", 100, 150);
        game.batch.end();

        if (game.assetManager.isFinished()) {
            game.setScreen(new MenuScreen(game));
        }
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        font.dispose();
    }
}