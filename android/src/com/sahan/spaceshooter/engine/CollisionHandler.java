package com.sahan.spaceshooter.engine;

import android.util.Log;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.Rectangle;
import com.sahan.spaceshooter.sprites.Player;
import com.sahan.spaceshooter.sprites.bullets.Bullet;
import com.sahan.spaceshooter.sprites.bullets.BulletEmission;
import com.sahan.spaceshooter.sprites.enemies.Enemy;
import com.sahan.spaceshooter.sprites.Explosion;
import com.sahan.spaceshooter.sprites.powerups.PowerUp;

public class CollisionHandler {
    private final EntityManager entityManager;
    private final AssetManager assetManager;

    public CollisionHandler(EntityManager entityManager, AssetManager assetManager) {
        this.entityManager = entityManager;
        this.assetManager = assetManager;
    }

    public void checkCollisions() {
        checkPlayerBulletsAgainstEnemies();
        checkEnemyBulletsAgainstPlayer();
        checkEnemiesAgainstPlayer();
        checkPowerUpCollision();
    }

    private void checkPlayerBulletsAgainstEnemies() {
       for (BulletEmission bulletEmission: entityManager.getPlayerBullets()) {
           for (Bullet bullet : bulletEmission.getBullets()) {
               for (Enemy enemy : entityManager.getEnemies()) {
                   if (bullet.getCollider().overlaps(enemy.getCollider())) {
                       enemy.takeDamage(1);
                       bullet.setActive(false);
                       if (enemy.isDestroyed()) {
                           entityManager.addPowerUp(enemy.dropItem());
                           entityManager.addExplosion(new Explosion(
                                   enemy.getPosition().x + enemy.getWidth() / 2,
                                   enemy.getPosition().y + enemy.getHeight() / 2,
                                   8.0f,
                                   assetManager
                           ));

                       }
                       break;
                   }
               }
           }
       }
    }

    private void checkEnemyBulletsAgainstPlayer() {
        Player player = entityManager.getPlayer();
        for (BulletEmission bulletEmission : entityManager.getEnemyBulletEmissions()) {
            for (Bullet bullet: bulletEmission.getBullets()) {
                if (bullet.getCollider().overlaps(player.getCollider())) {
                    player.takeDamage(1);
                    bullet.setActive(false);
                    if (!player.isAlive()) {
                        entityManager.setGameOver(true);
                        entityManager.addExplosion(new Explosion(
                                player.getPosition().x + player.getWidth() / 2,
                                player.getPosition().y + player.getHeight() / 2,
                                10.0f,
                                assetManager
                        ));
                    }
                }
            }
        }
    }

    private void checkEnemiesAgainstPlayer() {
        Player player = entityManager.getPlayer();
        for (Enemy enemy : entityManager.getEnemies()) {
            if (enemy.getCollider().overlaps(player.getCollider())) {
                player.takeDamage(1);
                enemy.markForRemoval();
                entityManager.addExplosion(new Explosion(
                        enemy.getPosition().x + enemy.getWidth() / 2,
                        enemy.getPosition().y + enemy.getHeight() / 2,
                        8.0f,
                        assetManager
                ));
                if (!player.isAlive()) {
                    entityManager.setGameOver(true);
                    entityManager.addExplosion(new Explosion(
                            player.getPosition().x + player.getWidth() / 2,
                            player.getPosition().y + player.getHeight() / 2,
                            10.0f,
                            assetManager
                    ));
                }
            }
        }
    }

    private void checkPowerUpCollision() {
        Player player = entityManager.getPlayer();
        for (PowerUp powerUp: entityManager.getPowerUps()) {
            if(powerUp.getCollider().overlaps(player.getCollider())){
                Log.d("Collision", "powerup collid");
                powerUp.setCollected();
            }
        }
    }
}