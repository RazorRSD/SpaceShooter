package com.sahan.spaceshooter.engine;

import android.util.Log;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.sahan.spaceshooter.SpaceShooterGameClass;
import com.sahan.spaceshooter.data.GameDataManager;
import com.sahan.spaceshooter.data.PlayerUpgrades;
import com.sahan.spaceshooter.missions.MissionManager;
import com.sahan.spaceshooter.screens.Common;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.sahan.spaceshooter.screens.MenuScreen;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.sahan.spaceshooter.sprites.Player;

public class GameState {
    private int playerHealth;
    private final EntityManager entityManager;
    private int lives;
    private int score;
    private int bank;
    private int powerLevel;
    private int powerUpPoints;
    private int currentWave;
    private String currentMission;
    private boolean gameOver;
    private final BitmapFont font;
    private final Stage stage;
    private boolean showingPopup;
    private final SpaceShooterGameClass game;
    private Dialog currentDialog;
    private InputProcessor previousInputProcessor;
    private MissionManager missionManager;
    private final Common common;
    private final Texture healthBarTexture;
    private final Texture healthDotTexture;
    private final Texture powerUpBarTexture;
    private final Texture powerUpDotTexture;
    private final Texture statBarTexture;
    private final Texture heartIconTexture;
    private final Texture bankIconTexture;
    private final Texture scoreIconTexture;
    private final TextureRegion statBarRegion;
    private final Texture waveNumberTexture;
    private final GameDataManager gameDataManager;
    private Player player;

    public GameState(SpaceShooterGameClass game, EntityManager entityManager) {
        common = new Common();
        this.font = common.createBoldFont(30);
        this.game = game;
        this.stage = new Stage(new ScreenViewport());
        this.showingPopup = false;
        this.entityManager = entityManager;
        this.gameDataManager = new GameDataManager();

        TextButton startButton = common.createLargeButton("Menu", game.skin);
        TextButton.TextButtonStyle textButtonStyle = startButton.getStyle();
        game.skin.add("large", textButtonStyle);

        Dialog.WindowStyle windowStyle = new Dialog.WindowStyle(game.skin.get(Dialog.WindowStyle.class));
        windowStyle.titleFont = font;
        windowStyle.titleFontColor = Color.WHITE;
        game.skin.add("large", windowStyle);

        healthBarTexture = new Texture(Gdx.files.internal("ui/components/healthAndPower/Health_Bar_Table.png"));
        healthDotTexture = new Texture(Gdx.files.internal("ui/components/healthAndPower/Health_Dot.png"));
        powerUpBarTexture = new Texture(Gdx.files.internal("ui/components/healthAndPower/PowerUp_Bar_Table.png"));
        powerUpDotTexture = new Texture(Gdx.files.internal("ui/components/healthAndPower/PowerUp_Bar_Dot.png"));

        statBarTexture = new Texture(Gdx.files.internal("ui/components/statsbar/statbar.png"));
        heartIconTexture = new Texture(Gdx.files.internal("ui/components/statsbar/heart.png"));
        bankIconTexture = new Texture(Gdx.files.internal("ui/components/statsbar/bank.png"));
        scoreIconTexture = new Texture(Gdx.files.internal("ui/components/statsbar/score.png"));

        waveNumberTexture = new Texture(Gdx.files.internal("ui/components/statsbar/wavenumber.png"));

        statBarRegion = new TextureRegion(statBarTexture, 6, 6, 1068, 68);
    }

    public void setMissionManager(MissionManager missionManager) {
        this.missionManager = missionManager;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void showMissionCompletePopup(boolean isLastMission) {
        showingPopup = true;
        PlayerUpgrades playerUpgrades = new PlayerUpgrades();

        currentDialog = new Dialog("", game.skin);

        currentDialog.pad(0);

        Texture backgroundTexture = new Texture(Gdx.files.internal("ui/menuS_img/bg.jpg"));
        currentDialog.setBackground(new TextureRegionDrawable(new TextureRegion(backgroundTexture)));

        currentDialog.setFillParent(true);

        Table contentTable = new Table();
        contentTable.setFillParent(true);

        Label titleLabel = new Label(isLastMission ? "Game Complete!" : "Mission Complete!", new Label.LabelStyle(common.createBoldFont(72), Color.WHITE));
        contentTable.add(titleLabel).pad(50).expandY().top().row();

        Table buttonTable = new Table();
        if (!isLastMission) {
            TextButton nextButton = common.createLargeButton("Next", game.skin);
            nextButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    Log.d("POPUP", "Popup button clicked");
                    missionManager.proceedToNextMission();
                }
            });
            buttonTable.add(nextButton).size(400, 100).pad(20);
        }

        if(isLastMission) {
            gameDataManager.resetGameProgress(bank, new PlayerUpgrades());
        }else {
            gameDataManager.saveGameProgress(missionManager.getCurrentMission(), bank, score, lives, powerLevel, powerUpPoints, playerUpgrades);
        }

        TextButton menuButton = common.createLargeButton("Menu", game.skin);
        menuButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MenuScreen(game));
            }
        });
        buttonTable.add(menuButton).size(400, 100).pad(20);

        contentTable.add(buttonTable).expandY().bottom().pad(50);

        currentDialog.getContentTable().add(contentTable).expand().fill();

        currentDialog.setFillParent(true);

        stage.addActor(currentDialog);

        previousInputProcessor = Gdx.input.getInputProcessor();
        Gdx.input.setInputProcessor(stage);
    }


    public void hideMissionCompletePopup() {
        showingPopup = false;
        if (currentDialog != null) {
            currentDialog.hide();
        }
        if (previousInputProcessor != null) {
            Gdx.input.setInputProcessor(previousInputProcessor);
        }
    }

    public Stage getStage() {
        return stage;
    }

    public void update(float delta) {
        if (showingPopup) {
            stage.act(delta);
        }
    }

    public void render(SpriteBatch batch) {

    if (showingPopup) {
        batch.end();
        stage.draw();
        batch.begin();
    }
        renderStatBar(batch);
        renderHealthBar(batch);
        renderPowerUpBar(batch);
        renderWaveNumber(batch);
    }

    private void renderStatBar(SpriteBatch batch) {
        if (statBarRegion == null || heartIconTexture == null ||
                bankIconTexture == null || scoreIconTexture == null) {
            return;
        }

        float barWidth = Gdx.graphics.getWidth() - 20;
        float barHeight = 80;
        float barY = Gdx.graphics.getHeight() - barHeight - 10;
        float sectionWidth = barWidth / 3;

        batch.draw(statBarRegion, 10, barY, barWidth, barHeight);

        float iconSize = 40;
        float iconY = barY + (barHeight - iconSize) / 2;

        float heartX = 20;
        batch.draw(heartIconTexture, heartX, iconY, iconSize, iconSize);
        font.draw(batch, "x " + lives, heartX + iconSize + 10, barY + barHeight / 2 + font.getCapHeight() / 2);

        float scoreX = 20 + sectionWidth;
        batch.draw(scoreIconTexture, scoreX, iconY, iconSize, iconSize);
        font.draw(batch, ": " + score, scoreX + iconSize + 10, barY + barHeight / 2 + font.getCapHeight() / 2);

        float bankX = 20 + 2 * sectionWidth;
        batch.draw(bankIconTexture, bankX, iconY, iconSize, iconSize);
        font.draw(batch, ": " + bank, bankX + iconSize + 10, barY + barHeight / 2 + font.getCapHeight() / 2);
    }

    private void renderWaveNumber(SpriteBatch batch) {
        if (waveNumberTexture == null) return;
        float waveNumberWidth = 150;
        float waveNumberHeight = 150;
        float padding = 10;
        float waveNumberX = Gdx.graphics.getWidth() - waveNumberWidth - padding;
        float waveNumberY = Gdx.graphics.getHeight() - 80 - waveNumberHeight - padding * 2;

        batch.draw(waveNumberTexture, waveNumberX, waveNumberY, waveNumberWidth, waveNumberHeight);

        String waveText = String.valueOf(currentWave);
        float textWidth = font.getScaleX();
        float textX = waveNumberX + (waveNumberWidth - textWidth) / 2;
        float textY = waveNumberY + waveNumberHeight / 2 + font.getCapHeight() / 2;

        font.draw(batch, waveText, textX, textY);
    }

    private void renderPowerUpBar(SpriteBatch batch) {
        batch.draw(powerUpBarTexture, 10, Gdx.graphics.getHeight() - 245);
        int numDots = powerUpPoints / 12;
        float dotWidth = powerUpDotTexture.getWidth();
        for (int i = 0; i < numDots; i++) {
            batch.draw(powerUpDotTexture, 16 + i * dotWidth, Gdx.graphics.getHeight() - 239);
        }
        font.draw(batch, "X" + powerLevel, 280, Gdx.graphics.getHeight() - 234);
    }

    private void renderHealthBar(SpriteBatch batch) {
        batch.draw(healthBarTexture, 10, Gdx.graphics.getHeight() - 165);
        int maxHealth = player.getMaxHealth();
        float healthPercentage =  ((float)playerHealth / (float)maxHealth) * 10f;
        int numDots = (int) healthPercentage;
        Log.d("HealthBar","numdot"+numDots + "f: "+ healthPercentage + " playerH: "+ playerHealth + " maxHealth: "+ maxHealth);
        float dotWidth = healthDotTexture.getWidth() + 3;
        for (int i = 0; i < numDots; i++) {
            batch.draw(healthDotTexture, 16 + i * dotWidth, Gdx.graphics.getHeight() - 159);
        }
    }

    public boolean isShowingPopup() {
        return showingPopup;
    }

    public int getPlayerHealth() { return playerHealth; }
    public void setPlayerHealth(int playerHealth) { this.playerHealth = playerHealth; }

    public int getLives() { return lives; }
    public void setLives(int lives) { this.lives = lives; }

    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }

    public int getBank() { return bank; }
    public void setBank(int bank) { this.bank = bank; }

    public int getPowerLevel() { return powerLevel; }
    public void setPowerLevel(int powerLevel) { this.powerLevel = powerLevel; }

    public int getPowerUpPoints() { return  powerUpPoints;}
    public void setPowerUpPoints(int powerUpPoints) { this.powerUpPoints = powerUpPoints;}

    public int getCurrentWave() { return currentWave; }
    public void setCurrentWave(int currentWave) {
        this.currentWave = currentWave;
        gameDataManager.updateBank(bank);
    }

    public String getCurrentMission() { return currentMission; }
    public void setCurrentMission(String currentMission) {
        this.currentMission = currentMission;
    }

    public boolean isGameOver() { return gameOver; }
    public void setGameOver(boolean gameOver) { this.gameOver = gameOver; }

    public void addScore(int points) { this.score += points; }
    public void addBank(int amount) { this.bank += amount; }

    public void collectedPowerUp(int points) {
        powerUpPoints += points;
        if (powerUpPoints >= 100) {
            powerUpPoints -= 100;
            Player player = entityManager.getPlayer();
            player.increasePowerLevel();
            powerLevel++;
        }
    }

    public void dispose () {
        font.dispose();
        healthBarTexture.dispose();
        healthDotTexture.dispose();
        powerUpBarTexture.dispose();
        powerUpDotTexture.dispose();
        statBarTexture.dispose();
        heartIconTexture.dispose();
        bankIconTexture.dispose();
        scoreIconTexture.dispose();
        if (waveNumberTexture != null) waveNumberTexture.dispose();
    }
}