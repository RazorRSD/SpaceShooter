package com.sahan.spaceshooter.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.sahan.spaceshooter.SpaceShooterGameClass;

public class HelpScreen implements Screen {
    private final Stage stage;
    private final SpaceShooterGameClass game;
    private final Common common;
    private Texture backgroundTexture;
    private Texture playerShipTexture;
    private Texture enemyShipTexture;
    private Texture powerUpTexture;

    public HelpScreen(SpaceShooterGameClass game) {
        this.game = game;
        stage = new Stage(new ScreenViewport());
        common = new Common();

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        backgroundTexture = new Texture(Gdx.files.internal("ui/bgs/MenuBG.png"));
        playerShipTexture = new Texture(Gdx.files.internal("ui/sprites/player/player.png"));
        enemyShipTexture = new Texture(Gdx.files.internal("ui/sprites/enemies/enemy_basic.png"));
        powerUpTexture = new Texture(Gdx.files.internal("ui/sprites/powerups/powerup.png"));

        BitmapFont titleFont = common.createBoldFont(54);
        Label.LabelStyle titleStyle = new Label.LabelStyle(titleFont, com.badlogic.gdx.graphics.Color.WHITE);
        Label titleLabel = new Label("How to Play", titleStyle);
        table.add(titleLabel).colspan(2).pad(20).row();

        addInstructionRow(table, playerShipTexture, "This is your ship. Move it by touching the screen.");

        addInstructionRow(table, enemyShipTexture, "These are enemy ships. Avoid or destroy them!");

        addInstructionRow(table, powerUpTexture, "Collect power-ups to increase your strength!");

        TextButton backButton = common.createPlasticButton("Back", game.skin);
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MenuScreen(game));
                dispose();
            }
        });
        table.add(backButton).colspan(2).size(300, 100).pad(20);

        Gdx.input.setInputProcessor(stage);
    }

    private void addInstructionRow(Table table, Texture texture, String instruction) {
        Image image = new Image(texture);
        table.add(image).size(100, 100).pad(10);

        Label.LabelStyle labelStyle = new Label.LabelStyle(common.createBoldFont(24), com.badlogic.gdx.graphics.Color.WHITE);
        Label instructionLabel = new Label(instruction, labelStyle);
        instructionLabel.setWrap(true);
        table.add(instructionLabel).width(Gdx.graphics.getWidth() * 0.6f).pad(10).row();
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();
        game.batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        game.batch.end();

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        stage.dispose();
        backgroundTexture.dispose();
        playerShipTexture.dispose();
        enemyShipTexture.dispose();
        powerUpTexture.dispose();
    }
}