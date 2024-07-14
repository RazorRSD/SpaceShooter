package com.sahan.spaceshooter.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector3;
import com.sahan.spaceshooter.SpaceShooterGameClass;
import com.sahan.spaceshooter.engine.*;
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

    private final BitmapFont font;
    private int score;
    private int lives;
    private int bank;
    private boolean gameOver;

    public GameScreen(SpaceShooterGameClass game) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        entityManager = new EntityManager();
        entityManager.setPlayer(new Player(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 4f));

        collisionHandler = new CollisionHandler(entityManager, game.assetManager);
        enemySpawner = new EnemySpawner(entityManager);

        touchPosition = new Vector3();

        Common common = new Common();
        font = common.createBoldFont(24);
        score = 0;
        lives = 3;
        bank = 0;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        updateGame(delta);

        game.batch.begin();
        entityManager.getPlayer().draw(game.batch);
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

        font.draw(game.batch, "Health: " + entityManager.getPlayer().getHealth(), 10, Gdx.graphics.getHeight() - 10);
        font.draw(game.batch, "Lives: " + lives, (float) Gdx.graphics.getWidth() / 2 - 50, Gdx.graphics.getHeight() - 10);
        font.draw(game.batch, "Score: " + score, Gdx.graphics.getWidth() - 500, Gdx.graphics.getHeight() - 10);
        font.draw(game.batch, "Bank: " + bank, 10, Gdx.graphics.getHeight() - 50);

        if (gameOver) {
            font.draw(game.batch, "GAME OVER", (float) Gdx.graphics.getWidth() / 2 - 50, (float) Gdx.graphics.getHeight() / 2);
        }

        game.batch.end();
    }

    private void handleInput() {
        if (Gdx.input.isTouched()) {
            touchPosition.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPosition);
            Player player = entityManager.getPlayer();
            float spriteWidth = player.getWidth();
            float spriteHeight = player.getHeight();

            // Calculate new position
            float newX = touchPosition.x - spriteWidth;
            float newY = touchPosition.y - spriteHeight;

            // Restrict X position
            newX = Math.max(-spriteWidth/2f, newX);
            newX = Math.min(camera.viewportWidth - (player.getWidth() + 100), newX);

            // Restrict Y position
            newY = Math.max(-spriteHeight/2f, Math.min(newY, Gdx.graphics.getHeight()));

            if (player.canShoot()) {
                entityManager.getPlayerBullets().add(player.shoot());
            }

            player.setPosition(newX, newY);
        }
    }

    private void updateGame(float delta) {
        if (!gameOver) {
            entityManager.update(delta);
            enemySpawner.update(delta);
            handleInput();
            collisionHandler.checkCollisions();
            updateScore();
            updateBank();
            checkGameOver();
        }
    }

    private void updateScore() {
        for (Enemy enemy : entityManager.getEnemies()) {
            if (enemy.isDestroyed()) {
                score += enemy.getScore();
            }
        }
    }

    private void updateBank() {
        for (PowerUp powerUp : entityManager.getPowerUps()) {
            if (powerUp.isCollected() && powerUp.getPowerUpType() == PowerUpType.COIN) {
                bank += 5;
            }
        }
    }

    private void checkGameOver() {
        Player player = entityManager.getPlayer();
        if (player.getHealth() <= 0) {
            lives--;
            if (lives > 0) {
                player.resetHealth();
                player.setInvincible(true);
                player.startBlinking();
            } else {
                gameOver = true;
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
        font.dispose();
    }
}