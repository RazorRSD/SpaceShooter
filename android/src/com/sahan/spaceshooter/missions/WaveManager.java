package com.sahan.spaceshooter.missions;

import com.badlogic.gdx.utils.Array;
import com.sahan.spaceshooter.engine.EntityManager;
import com.sahan.spaceshooter.engine.GameState;
import com.sahan.spaceshooter.sprites.enemies.Enemy;

public class WaveManager {
    private final EntityManager entityManager;
    private boolean waveInProgress;
    private float waveTimer;
    private Array<Enemy> currentWaveEnemies;
    private GameState gameState;

    public WaveManager(EntityManager entityManager, GameState gameState) {
        this.entityManager = entityManager;
        this.waveInProgress = false;
        this.waveTimer = 0;
        this.currentWaveEnemies = new Array<>();
        this.gameState = gameState;
    }

    public void update(float delta) {
        if (!waveInProgress) {
            startNextWave();
        } else {
            updateCurrentWave(delta);
        }
    }

    private void startNextWave() {
        gameState.setCurrentWave(gameState.getCurrentWave() + 1);
        waveInProgress = true;
        waveTimer = 0;
        currentWaveEnemies.clear();

        switch (gameState.getCurrentWave()) {
            case 1:
                WaveCollection.spawnWave1(entityManager);
                break;
            case 2:
                WaveCollection.spawnWave2(entityManager);
                break;
            case 3:
                WaveCollection.spawnWave3(entityManager);
                break;
            default:

                break;
        }
    }

    private void updateCurrentWave(float delta) {
        waveTimer += delta;


        boolean allEnemiesDestroyed = true;
        for (Enemy enemy : currentWaveEnemies) {
            if (!enemy.isDestroyed()) {
                allEnemiesDestroyed = false;
                break;
            }
        }

        if (allEnemiesDestroyed) {
            waveInProgress = false;
        }


        switch (gameState.getCurrentWave()) {
            case 1:
                WaveCollection.updateWave1(currentWaveEnemies);
                break;
            case 2:
                WaveCollection.updateWave2(currentWaveEnemies);
                break;
            case 3:
                WaveCollection.updateWave3(currentWaveEnemies);
                break;
        }
    }

    public boolean isWaveInProgress() {
        return waveInProgress;
    }

}