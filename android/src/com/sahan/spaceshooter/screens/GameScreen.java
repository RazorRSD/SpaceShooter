package com.sahan.spaceshooter.screens;

import android.util.Log;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.sahan.spaceshooter.SpaceShooterGameClass;
import com.sahan.spaceshooter.data.GameProgress;
import com.sahan.spaceshooter.engine.*;
import com.sahan.spaceshooter.missions.MissionManager;
import com.sahan.spaceshooter.sprites.*;
import com.sahan.spaceshooter.sprites.bullets.BulletEmission;
import com.sahan.spaceshooter.sprites.enemies.*;
import com.sahan.spaceshooter.sprites.powerups.PowerUp;
import com.sahan.spaceshooter.sprites.powerups.PowerUpType;

public class GameScreen implements Screen {
    private final SpaceShooterGameClass game;
    private final OrthographicCamera camera;
    private final Vector3 touchPosition;

    private final EntityManager entityManager;
    private final CollisionHandler collisionHandler;
    private final EnemySpawner enemySpawner;
    private final MissionManager missionManager;

    private final GameState gameState;
    private final ScrollingBackground background;

    public GameScreen(SpaceShooterGameClass game, GameProgress gameProgress) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        entityManager = new EntityManager();
        enemySpawner = new EnemySpawner(entityManager);
        touchPosition = new Vector3();
        collisionHandler = new CollisionHandler(entityManager, game.assetManager);
        gameState = new GameState(game, entityManager);
        missionManager = new MissionManager(entityManager, gameState);
        gameState.setBank(gameProgress.bank);
        gameState.setCurrentMission(gameProgress.completedMissions);
        gameState.setScore(gameProgress.score);
        gameState.setLives(gameProgress.lives);
        gameState.setPowerLevel(gameProgress.powerLevel);
        gameState.setPowerUpPoints(gameProgress.powerUpPoints);
        Log.d("DataLoading","Game Screen : "+ gameState.getBank());
        Texture backgroundTexture = new Texture("ui/bgs/missionBg_01.jpg");
        backgroundTexture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        background = new ScrollingBackground(backgroundTexture, 50);
        entityManager.setPlayer(new Player(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 4f, gameState, gameProgress.upgrades));
        gameState.setPlayer(entityManager.getPlayer());
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        updateGame(delta);

        game.batch.begin();

        background.render(game.batch);
        background.update(delta);


        for (Enemy enemy : entityManager.getEnemies()) {
            enemy.draw(game.batch);
        }
        for (BulletEmission bulletEmission : entityManager.getPlayerBullets()) {
            bulletEmission.draw(game.batch);
        }
        for (BulletEmission bulletEmission : entityManager.getEnemyBulletEmissions()) {
            bulletEmission.draw(game.batch);
        }
        for (Explosion explosion : entityManager.getExplosions()) {
            explosion.draw(game.batch);
        }
        for (PowerUp item : entityManager.getPowerUps()) {
            item.draw(game.batch);
        }

        entityManager.getPlayer().draw(game.batch);
        gameState.render(game.batch);
        if (gameState.isShowingPopup()) {
            gameState.getStage().act(Gdx.graphics.getDeltaTime());
            gameState.getStage().draw();
        }
        game.batch.end();
    }

    private void handleInput() {
        if(!gameState.isShowingPopup()){
            if (Gdx.input.isTouched()) {
                touchPosition.set(Gdx.input.getX(), Gdx.input.getY(), 0);
                camera.unproject(touchPosition);
                Player player = entityManager.getPlayer();
                float spriteWidth = player.getWidth();
                float spriteHeight = player.getHeight();

                float newX = touchPosition.x - spriteWidth;
                float newY = touchPosition.y - spriteHeight;

                newX = Math.max(-spriteWidth/2f, newX);
                newX = Math.min(camera.viewportWidth - (player.getWidth() + 100), newX);

                newY = Math.max(-spriteHeight/2f, Math.min(newY, Gdx.graphics.getHeight()));

                if (player.canShoot()) {
                    entityManager.getPlayerBullets().add(player.shoot());
                }

                player.setPosition(newX, newY);
            }
        }
    }

    private void updateGame(float delta) {
        if (!gameState.isGameOver()) {
            entityManager.update(delta);
            enemySpawner.update(delta);
            collisionHandler.checkCollisions();
            missionManager.update(delta);
            handleInput();
            updateScore();
            updateBank();
            checkGameOver();
        }
    }

    private void updateScore() {
        for (Enemy enemy : entityManager.getEnemies()) {
            if (enemy.isDestroyed()) {
                gameState.addScore(enemy.getScore());
            }
        }
    }

    private void updateBank() {
        for (PowerUp powerUp : entityManager.getPowerUps()) {
            if (powerUp.isCollected() && powerUp.getPowerUpType() == PowerUpType.COIN) {
                gameState.addBank(5);
            }
        }
    }

    private void checkGameOver() {
        Player player = entityManager.getPlayer();
        if (!player.isAlive()) {
            gameState.setLives(gameState.getLives() - 1);
            if (gameState.getLives() > 0) {
                player.resetHealth();
                player.setInvincible(true);
                player.startBlinking();
            } else {
                gameState.setGameOver(true);
            }
        }
    }

    @Override
    public void show() {}

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        entityManager.dispose();
        background.dispose();
        gameState.dispose();
    }
}