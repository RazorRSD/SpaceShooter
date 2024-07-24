package com.sahan.spaceshooter.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.sahan.spaceshooter.SpaceShooterGameClass;

public class AboutScreen implements Screen {
    private final Stage stage;
    private final SpaceShooterGameClass game;
    private Common common;

    public AboutScreen(SpaceShooterGameClass game) {
        this.game = game;
        stage = new Stage(new ScreenViewport());
        common = new Common();

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        // Create labels for about information
        Label titleLabel = new Label("Space Shooter", new Label.LabelStyle(createFont(60), Color.WHITE));
        Label developerLabel = new Label("Sahan Bandara - 23014559", new Label.LabelStyle(createFont(30), Color.WHITE));
        Label versionLabel = new Label("Version: 1.0", new Label.LabelStyle(createFont(40), Color.WHITE));
        Label descriptionLabel = new Label("This game is created for assignment 02 - Advanced Mobile Development - COM640 by Sahan Bandara. Student Number: 23014559",
                new Label.LabelStyle(createFont(30), Color.WHITE));
        descriptionLabel.setWrap(true);

        // Create back button
        TextButton backButton = common.createPlasticButton("Back", game.skin);
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MenuScreen(game));
                dispose();
            }
        });

        // Add elements to table
        table.add(titleLabel).padBottom(40).row();
        table.add(developerLabel).padBottom(20).row();
        table.add(versionLabel).padBottom(40).row();
        table.add(descriptionLabel).width(Gdx.graphics.getWidth() * 0.9f).padBottom(40).row();
        table.add(backButton).size(400, 100).row();

        Gdx.input.setInputProcessor(stage);
    }

    private BitmapFont createFont(int size) {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("ui/fonts/font.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = size;
        parameter.borderWidth = 2;
        parameter.color = Color.WHITE;
        parameter.borderColor = Color.BLACK;
        BitmapFont font = generator.generateFont(parameter);
        generator.dispose();
        return font;
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}