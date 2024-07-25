package com.sahan.spaceshooter.screens;

import android.util.Log;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.sahan.spaceshooter.SpaceShooterGameClass;
import com.sahan.spaceshooter.data.GameDataManager;
import com.sahan.spaceshooter.data.GameProgress;
import com.sahan.spaceshooter.data.PlayerUpgrades;

public class MenuScreen implements Screen {

    private final Stage stage;
    private Music backgroundMusic;
    private Texture backgroundTexture;
    private final Sprite backgroundSprite;
    private final SpaceShooterGameClass game;
    private Texture muteTexture;
    private Texture unmuteTexture;
    private final ImageButton muteButton;
    private boolean isMuted = false;
    private final GameDataManager gameDataManager;

    public MenuScreen(SpaceShooterGameClass game) {
        this.game = game;
        stage = new Stage(new ScreenViewport());
        Common common = new Common();
        gameDataManager = new GameDataManager();
        try {
            backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("sfx/menu_loop.mp3"));
            backgroundTexture = new Texture(Gdx.files.internal("ui/bgs/MenuBG.png"));
            muteTexture = new Texture(Gdx.files.internal("ui/components/button/mute_btn/unmute.png"));
            unmuteTexture = new Texture(Gdx.files.internal("ui/components/button/mute_btn/mute.png"));
        } catch (Exception e) {
            Gdx.app.error("MenuScreen", "Error loading assets", e);
        }

        ImageButton.ImageButtonStyle muteButtonStyle = new ImageButton.ImageButtonStyle();
        muteButtonStyle.imageUp = new TextureRegionDrawable(new TextureRegion(unmuteTexture));
        muteButton = new ImageButton(muteButtonStyle);
        muteButton.setHeight(100);
        muteButton.setWidth(100);
        muteButton.setPosition(Gdx.graphics.getWidth() - muteButton.getWidth() - 50,
                Gdx.graphics.getHeight() - muteButton.getHeight() - 10);

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        BitmapFont titleFont = common.createBoldFont(54);
        Label.LabelStyle titleStyle = new Label.LabelStyle(titleFont, Color.WHITE);
        Label titleLabel = new Label("Space Shooter", titleStyle);
        table.add(titleLabel).padBottom(50).padBottom(400).row();

        TextButton startButton;
        TextButton continueButton;

        if(gameDataManager.hasSavedGame()) {
            continueButton = common.createPlasticButton("Continue", game.skin);
            TextButton newGameButton = common.createPlasticButton("New Game", game.skin);
            table.add(continueButton).size(600, 200).padBottom(20).row();
            table.add(newGameButton).size(600, 200).padBottom(20).row();

            newGameButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    restartNewGame();
                    dispose();
                }
            });

            continueButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    loadSavedGame();
                    dispose();
                }
            });
        }
        else {
            startButton = common.createPlasticButton("Start", game.skin);
            table.add(startButton).size(600, 200).padBottom(20).row();
            startButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    startNewGame();
                    dispose();
                }
            });
        }

        TextButton workshopButton = common.createPlasticButton("workshop", game.skin);
        TextButton aboutButton = common.createPlasticButton("About", game.skin);
        TextButton helpButton = common.createPlasticButton("Help", game.skin);

        table.add(workshopButton).size(600, 200).padBottom(20).row();
        table.add(helpButton).size(600, 200).padBottom(20).row();
        table.add(aboutButton).size(600, 200);

        backgroundSprite = new Sprite(backgroundTexture);
        backgroundSprite.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        backgroundMusic.setLooping(true);
        backgroundMusic.setVolume(0.5f);  // Adjust volume as needed
        backgroundMusic.play();

        muteButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                isMuted = !isMuted;
                if (isMuted) {
                    backgroundMusic.setVolume(0f);
                    muteButton.getStyle().imageUp = new TextureRegionDrawable(new TextureRegion(muteTexture));
                } else {
                    backgroundMusic.setVolume(0.5f);  // Or whatever volume you prefer
                    muteButton.getStyle().imageUp = new TextureRegionDrawable(new TextureRegion(unmuteTexture));
                }
            }
        });

        workshopButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new WorkshopScreen(game));
                dispose();
            }
        });

        helpButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new HelpScreen(game));
                dispose();
            }
        });

        aboutButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new AboutScreen(game));
                dispose();
            }
        });

        stage.addActor(muteButton);
        Gdx.input.setInputProcessor(stage);
    }

    public void loadSavedGame() {
        GameProgress progress = gameDataManager.loadGameProgress();
        Log.d("DataLoading","Data load" + progress.bank);
        game.setScreen(new GameScreen(game, progress));
    }

    // Method to start a new game
    public void startNewGame() {
        GameProgress progress = new GameProgress("Asteroid Field", 0,0,3, 0, 0, new PlayerUpgrades());
        game.setScreen(new GameScreen(game, progress));
    }

    public void restartNewGame () {
        GameProgress gameProgress = gameDataManager.loadGameProgress();
        gameDataManager.resetGameProgress(gameProgress.bank, gameProgress.upgrades);
    }

    @Override
    public void dispose() {
        stage.dispose();
        backgroundMusic.dispose();
        muteTexture.dispose();
        unmuteTexture.dispose();

        for (Actor actor : stage.getActors()) {
            if (actor instanceof Table) {
                for (Actor child : ((Table) actor).getChildren()) {
                    if (child instanceof TextButton) {
                        ((TextButton) child).getStyle().font.dispose();
                    } else if (child instanceof Label) {
                        ((Label) child).getStyle().font.dispose();
                    }
                }
            }
        }
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();
        backgroundSprite.draw(game.batch);
        game.batch.end();

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
}