package com.sahan.spaceshooter.engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.sahan.spaceshooter.sprites.enemies.Enemy;
import com.sahan.spaceshooter.sprites.enemies.EnemyTypes;

public class EnemySpawner {
    private static final float ENEMY_SPAWN_INTERVAL = 5f;
    private float enemySpawnTimer;
    private final EntityManager entityManager;

    public EnemySpawner(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.enemySpawnTimer = 0;
    }

    public void update(float delta) {
        enemySpawnTimer += delta;
        if (enemySpawnTimer >= ENEMY_SPAWN_INTERVAL) {
//            spawnEnemy();
            enemySpawnTimer = 0;
        }
    }

    private void spawnEnemy() {
        float x = MathUtils.random(0, Gdx.graphics.getWidth());
        float y = Gdx.graphics.getHeight() + 50;
        int type = MathUtils.random(0, 4);

        Enemy enemy;
        switch (type) {
            case 1:
                enemy = new EnemyTypes.ShooterEnemy(x, y);
                break;
            case 2:
                enemy = new EnemyTypes.HeavyEnemy(x, y);
                break;
            case 3:
                enemy = new EnemyTypes.HeavyShooterEnemy(x, y);
                break;
            case 4:
                enemy = new EnemyTypes.BossEnemy(x, y);
                break;
            default:
                enemy = new EnemyTypes.BasicEnemy(x, y);
        }
        entityManager.addEnemy(enemy);
    }
}