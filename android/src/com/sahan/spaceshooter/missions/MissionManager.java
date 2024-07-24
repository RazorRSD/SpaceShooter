package com.sahan.spaceshooter.missions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.sahan.spaceshooter.engine.EntityManager;
import com.sahan.spaceshooter.engine.GameState;

public class MissionManager {
    private final Array<MissionCollection.Mission> missions;
    private int currentMissionIndex;
    private int currentWaveIndex;
    private boolean waveInProgress;
    private final EntityManager entityManager;
    private final GameState gameState;
    private boolean missionCompleteSequence;
    private float playerExitTimer;
    private static final float PLAYER_EXIT_DURATION = 2f;

    public MissionManager(EntityManager entityManager, GameState gameState) {
        this.entityManager = entityManager;
        this.gameState = gameState;
        this.missions = MissionCollection.getMissions(entityManager);
        this.currentMissionIndex = 0;
        this.currentWaveIndex = -1;
        this.waveInProgress = false;
        this.missionCompleteSequence = false;
        this.playerExitTimer = 0f;
        gameState.setMissionManager(this);
    }

    public void update(float delta) {
        if (missionCompleteSequence) {
            updateMissionCompleteSequence(delta);
        } else if (!waveInProgress) {
            startNextWave();
        } else {
            updateCurrentWave(delta);
        }
    }

    private void startNextWave() {
        currentWaveIndex++;
        MissionCollection.Mission currentMission = missions.get(currentMissionIndex);

        if (currentWaveIndex >= currentMission.waves.size) {
            // Mission complete, move to next mission
            currentMissionIndex++;
            currentWaveIndex = -1;

            if (currentMissionIndex >= missions.size) {
                // All missions complete
                gameState.setGameOver(true);
                return;
            }

            currentMission = missions.get(currentMissionIndex);
            gameState.setCurrentMission(currentMission.name);
        }

        MissionCollection.Wave wave = currentMission.waves.get(currentWaveIndex);
        wave.spawnMethod.run();
        waveInProgress = true;
        gameState.setCurrentWave(currentWaveIndex + 1);
    }

    private void updateCurrentWave(float delta) {
        MissionCollection.Mission currentMission = missions.get(currentMissionIndex);
        MissionCollection.Wave currentWave = currentMission.waves.get(currentWaveIndex);

        currentWave.updateMethod.run();

        // Check if all enemies are destroyed
        if (entityManager.getEnemies().size == 0) {
            if (currentWaveIndex == missions.get(currentMissionIndex).waves.size - 1) {
                // Last wave of the mission
                missionCompleteSequence = true;
            } else {
                waveInProgress = false;
            }
        }
    }

    private void updateMissionCompleteSequence(float delta) {
        if (entityManager.getPowerUps().size == 0) {
            playerExitTimer += delta;
            movePlayerOffScreen();

            if (playerExitTimer >= PLAYER_EXIT_DURATION) {
                entityManager.clearBullets();
                showMissionCompletePopup();
            }
        }
    }

    private void showMissionCompletePopup() {
        boolean isLastMission = currentMissionIndex == missions.size - 1;
        gameState.showMissionCompletePopup(isLastMission);
    }

    public void proceedToNextMission() {
        // Clear current mission data
        entityManager.clearBullets();

        // Increment mission index or handle game completion
        currentMissionIndex++;
        if (currentMissionIndex >= missions.size) {
            gameState.setGameOver(true);
        } else {
            currentWaveIndex = -1;
            missionCompleteSequence = false;
            waveInProgress = false;
            gameState.hideMissionCompletePopup();
            gameState.setCurrentMission(missions.get(currentMissionIndex).name);
            // Reset player position or perform any other necessary setup
            entityManager.getPlayer().setPosition(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 4f);
        }
    }

    private void movePlayerOffScreen() {
        // Move player towards the top of the screen
        float currentY = entityManager.getPlayer().getPosition().y;
        float newY = currentY + 500 * Gdx.graphics.getDeltaTime();
        entityManager.getPlayer().setPosition(entityManager.getPlayer().getPosition().x, newY);
    }

    public boolean isWaveInProgress() {
        return waveInProgress;
    }

    public int getCurrentMissionIndex() {
        return currentMissionIndex;
    }

    public String getCurrentMission () {
        return missions.get(currentMissionIndex).name;
    }

    public String getNextMission () {
        return missions.get(currentMissionIndex+1).name;
    }

    public int getCurrentWaveIndex() {
        return currentWaveIndex;
    }
}