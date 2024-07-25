package com.sahan.spaceshooter.engine;

import com.badlogic.gdx.utils.Array;
import com.sahan.spaceshooter.sprites.Player;
import com.sahan.spaceshooter.sprites.bullets.BulletEmission;
import com.sahan.spaceshooter.sprites.enemies.Enemy;
import com.sahan.spaceshooter.sprites.Explosion;
import com.sahan.spaceshooter.sprites.powerups.PowerUp;

public class EntityManager {
    private Player player;
    private final Array<BulletEmission> playerBullets;
    private final Array<Enemy> enemies;
    private final Array<BulletEmission> enemyBulletEmissions;
    private final Array<PowerUp> powerUps;
    private final Array<Explosion> explosions;
    private boolean gameOver;

    public EntityManager() {
        playerBullets = new Array<>();
        enemies = new Array<>();
        enemyBulletEmissions = new Array<>();
        powerUps = new Array<>();
        explosions = new Array<>();
        gameOver = false;
    }

    public void update(float delta) {
        updatePlayer(delta);
        updateBullets(delta);
        updateEnemies(delta);
        updateEnemyBulletEmissions(delta);
        updateExplosions(delta);
        updatePowerUps(delta);
    }

    private void updatePlayer(float delta) {
        if (player != null) {
            player.update(delta);
        }
    }

    private void updateEnemyBulletEmissions(float delta) {
        for (BulletEmission emission : enemyBulletEmissions) {
            emission.update(delta);
        }
        Array<BulletEmission> toRemovedEBullets = new Array<>();
        for (BulletEmission eBullet: enemyBulletEmissions) {
            if(eBullet == null || !eBullet.isActive()) {
                toRemovedEBullets.add(eBullet);
            }
        }
        enemyBulletEmissions.removeAll(toRemovedEBullets, true);
    }

    private void updatePowerUps(float delta) {
        for (PowerUp item : powerUps) {
            item.update(delta);
        }
        Array<PowerUp> toRemovedPowerUps = new Array<>();
        for (PowerUp powerUp: powerUps) {
            if(powerUp == null || !powerUp.isActive()) {
                toRemovedPowerUps.add(powerUp);
            }
        }
        powerUps.removeAll(toRemovedPowerUps, true);
    }

    private void updateBullets(float delta) {
        for (BulletEmission bulletEmission : playerBullets) {
            bulletEmission.update(delta);
        }
        Array<BulletEmission> toRemovedBullets = new Array<>();
        for (BulletEmission bulletEmission: playerBullets) {
            if(bulletEmission == null || !bulletEmission.isActive()) {
                toRemovedBullets.add(bulletEmission);
            }
        }
        playerBullets.removeAll(toRemovedBullets, true);
    }

    private void updateEnemies(float delta) {
        for (Enemy enemy : enemies) {
            if (enemy.canShoot()) {
                enemyBulletEmissions.add(enemy.shoot());

            }
            enemy.update(delta);
        }


        Array<Enemy> toRemovedEnemies = new Array<>();

        for (Enemy enemy: enemies) {
            if(enemy.isDestroyed()) {
                toRemovedEnemies.add(enemy);
            }
        }
        enemies.removeAll(toRemovedEnemies, true);
    }

    private void updateExplosions(float delta) {
        for (Explosion explosion : explosions) {
            explosion.update(delta);
        }
        Array<Explosion> toRemoveExplosions = new Array<>();
        for (Explosion explosion: explosions) {
            if(explosion == null || explosion.isFinished()){
                toRemoveExplosions.add(explosion);
            }
        }
        explosions.removeAll(toRemoveExplosions, true);
    }

    public void clearBullets() {
        for (BulletEmission emission : playerBullets) {
            emission.dispose();
        }
        playerBullets.clear();
        for (BulletEmission emission : enemyBulletEmissions) {
            emission.dispose();
        }
        enemyBulletEmissions.clear();
    }

    public Player getPlayer() { return player; }
    public void setPlayer(Player player) { this.player = player; }
    public Array<BulletEmission> getPlayerBullets() { return playerBullets; }
    public Array<Enemy> getEnemies() { return enemies; }
    public Array<BulletEmission> getEnemyBulletEmissions() {
        return enemyBulletEmissions;
    }
    public Array<PowerUp> getPowerUps() {return powerUps;}
    public Array<Explosion> getExplosions() { return explosions; }
    public boolean isGameOver() { return gameOver; }
    public void setGameOver(boolean gameOver) { this.gameOver = gameOver; }

    public void addExplosion(Explosion explosion) {
        explosions.add(explosion);
    }

    public void addPowerUp(PowerUp powerUp) {powerUps.add(powerUp);}

    public void addEnemy(Enemy enemy) {
        enemies.add(enemy);
    }

    public void dispose() {
        player.dispose();
        for (BulletEmission bulletEmission  : playerBullets) bulletEmission.dispose();
        for (Enemy enemy : enemies) enemy.dispose();
        for (BulletEmission bulletEmission : enemyBulletEmissions) bulletEmission.dispose();
        for (Explosion explosion : explosions) explosion.dispose();
    }
}
