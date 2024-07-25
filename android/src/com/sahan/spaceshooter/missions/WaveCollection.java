package com.sahan.spaceshooter.missions;

import android.util.Log;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.sahan.spaceshooter.engine.EntityManager;
import com.sahan.spaceshooter.sprites.enemies.Enemy;
import com.sahan.spaceshooter.sprites.enemies.EnemyTypes;

public class WaveCollection {
    private static float waveTimer = 0;
    private static boolean isPassed;

    public static void spawnWave1(EntityManager entityManager) {
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();
        float startY = screenHeight + 50;

        // Top row (5 enemies)
        for (int i = 0; i < 5; i++) {
            float x = (i + 1) * (screenWidth / 6f);
            Enemy enemy = new EnemyTypes.BasicEnemy(x, startY);
            entityManager.addEnemy(enemy);
        }

        // Middle row (4 enemies)
        for (int i = 0; i < 4; i++) {
            float x = (i + 1.5f) * (screenWidth / 6f);
            Enemy enemy = new EnemyTypes.BasicEnemy(x, startY + 50);
            entityManager.addEnemy(enemy);
        }

        // Bottom row (5 enemies)
        for (int i = 0; i < 5; i++) {
            float x = (i + 1) * (screenWidth / 6f);
            Enemy enemy = new EnemyTypes.BasicEnemy(x, startY + 100);
            entityManager.addEnemy(enemy);
        }
    }

    public static void updateWave1(Array<Enemy> enemies) {
        float screenHeight = Gdx.graphics.getHeight();
        float topRowY = screenHeight * 0.8f;
        float middleRowY = screenHeight * 0.7f;
        float bottomRowY = screenHeight * 0.6f;

        for (int i = 0; i < enemies.size; i++) {
            Enemy enemy = enemies.get(i);
            float targetY;

            if (i < 5) {
                targetY = topRowY;
            } else if (i < 9) {
                targetY = middleRowY;
            } else {
                targetY = bottomRowY;
            }

            if (enemy.getPosition().y > targetY) {
                enemy.getPosition().y -= 100 * Gdx.graphics.getDeltaTime();
                if (enemy.getPosition().y < targetY) {
                    enemy.getPosition().y = targetY;
                }
            }
        }
    }

    public static void spawnWave2(EntityManager entityManager) {
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();
        float startX = screenWidth + 50;
        float startY = screenHeight - 50;

        for (int i = 0; i < 5; i++) {
            Enemy enemy = new EnemyTypes.ShooterEnemy(startX + i * 100, startY);
            entityManager.addEnemy(enemy);
        }

        isPassed = false;
    }

    public static void updateWave2(Array<Enemy> enemies) {
        waveTimer += Gdx.graphics.getDeltaTime();
        float centerX = Gdx.graphics.getWidth() / 2f;
        float centerY = Gdx.graphics.getHeight() / 2f;
        float radius = 150f;
        float angularSpeed = MathUtils.PI / 2;

        for (int i = 0; i < enemies.size; i++) {
            Enemy enemy = enemies.get(i);
            if(!isPassed && enemy.getPosition().y < centerY + 200f ) {
                isPassed = true;
            }

            if (!isPassed) {
                enemy.getPosition().x -= 100 * Gdx.graphics.getDeltaTime();
                enemy.getPosition().y -= 100 * Gdx.graphics.getDeltaTime();
            } else {
                float angle = waveTimer * angularSpeed + (i * MathUtils.PI2 / 5);
                float x = centerX + MathUtils.cos(angle) * radius;
                float y = centerY + MathUtils.sin(angle) * radius;
                enemy.getPosition().set(x, y);
            }
        }
    }

    public static void spawnWave3(EntityManager entityManager) {
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();
        Enemy boss = new EnemyTypes.BossEnemy(screenWidth / 2f, screenHeight + 100);
        entityManager.addEnemy(boss);
    }

    public static void updateWave3(Array<Enemy> enemies) {
        if (!enemies.isEmpty()) {
            Enemy boss = enemies.first();
            float targetY = Gdx.graphics.getHeight() * 0.8f;

            if (boss.getPosition().y > targetY) {
                boss.getPosition().y -= 50 * Gdx.graphics.getDeltaTime();
            } else {
                float moveSpeed = 100f;
                boss.getPosition().x += moveSpeed * Gdx.graphics.getDeltaTime() * (boss.getPosition().x < Gdx.graphics.getWidth() / 2f ? 1 : -1);

                if (boss.getPosition().x < 50 || boss.getPosition().x > Gdx.graphics.getWidth() - 50) {
                    moveSpeed *= -1;
                }
            }
        }
    }
}