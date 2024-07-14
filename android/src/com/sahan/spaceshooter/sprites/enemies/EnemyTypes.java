package com.sahan.spaceshooter.sprites.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.sahan.spaceshooter.engine.CircleCollider;
import com.sahan.spaceshooter.sprites.bullets.Bullet;
import com.sahan.spaceshooter.sprites.bullets.BulletEmission;
import com.sahan.spaceshooter.sprites.bullets.BulletType;

public class EnemyTypes {

    public static class BasicEnemy extends Enemy {
        public BasicEnemy(float x, float y) {
            super(new Texture("ui/sprites/enemies/enemy_basic.png"), x, y, 1, 10, BulletType.BASIC_ENEMY, 0.5f);
            setScale(2f);
            velocity.set(MathUtils.random(-50, 50), MathUtils.random(-150, -100));

            float radius = Math.min(sprite.getWidth(), sprite.getHeight()) / 2 * sprite.getScaleX();
            collider = new CircleCollider(x + sprite.getWidth() / 2, y + sprite.getHeight() / 2, radius);
        }

        @Override
        public void update(float delta) {
            position.add(velocity.x * delta, velocity.y * delta);
            if (position.x < 0 || position.x > Gdx.graphics.getWidth() - getWidth()) {
                velocity.x = -velocity.x;
            }
            if (position.y < -getHeight()) {
                markForRemoval();
            }
            collider.setPosition(position.x + sprite.getWidth() / 2, position.y + sprite.getHeight() / 2);
        }

        @Override
        public BulletEmission shoot() {
            Array<Bullet> bullets = new Array<>();

            return new BulletEmission(bullets);
        }
    }

    public static class ShooterEnemy extends Enemy {
        public ShooterEnemy(float x, float y) {
            super(new Texture("ui/sprites/enemies/enemy_shooter.png"), x, y, 2, 20, BulletType.SHOOTER_ENEMY, 0.5f);
            setScale(2f);
            velocity.set(MathUtils.random(-30, 30), MathUtils.random(-120, -80));
            shootInterval = 2f;
            float radius = Math.min(sprite.getWidth(), sprite.getHeight()) / 2 * sprite.getScaleX();
            collider = new CircleCollider(x + sprite.getWidth() / 2, y + sprite.getHeight() / 2, radius);
        }

        @Override
        public void update(float delta) {
            position.add(velocity.x * delta, velocity.y * delta);
            if (position.x < 0 || position.x > Gdx.graphics.getWidth() - getWidth()) {
                velocity.x = -velocity.x;
            }
            if (position.y < -getHeight()) {
                markForRemoval();
            }
            collider.setPosition(position.x + sprite.getWidth() / 2, position.y + sprite.getHeight() / 2);
        }

        @Override
        public BulletEmission shoot() {
            Bullet bullet =  new Bullet(position.x, position.y - getHeight(), -250, BulletType.SHOOTER_ENEMY, 0.5f);
            return new BulletEmission(bullet);
        }
    }

    public static class HeavyEnemy extends Enemy {
        public HeavyEnemy(float x, float y) {
            super(new Texture("ui/sprites/enemies/enemy_heavy.png"), x, y, 5, 50, BulletType.BASIC_ENEMY, 0.5f);
            setScale(3f);
            velocity.set(MathUtils.random(-20, 20), MathUtils.random(-100, -60));
            float radius = Math.min(sprite.getWidth(), sprite.getHeight()) / 2 * sprite.getScaleX();
            collider = new CircleCollider(x + sprite.getWidth() / 2, y + sprite.getHeight() / 2, radius);
        }

        @Override
        public void update(float delta) {
            position.add(velocity.x * delta, velocity.y * delta);
            if (position.x < 0 || position.x > Gdx.graphics.getWidth() - getWidth()) {
                velocity.x = -velocity.x;
            }
            if (position.y < -getHeight()) {
                markForRemoval();
            }
            collider.setPosition(position.x + sprite.getWidth() / 2, position.y + sprite.getHeight() / 2);
        }

        @Override
        public BulletEmission shoot() {
            Array<Bullet> bullets = new Array<>();

            return new BulletEmission(bullets);
        }
    }

    public static class HeavyShooterEnemy extends Enemy {
        public HeavyShooterEnemy(float x, float y) {
            super(new Texture("ui/sprites/enemies/enemy_heavy_shooter.png"), x, y, 7, 70, BulletType.HEAVY_SHOOTER_ENEMY, 0.5f);
            setScale(3f);
            velocity.set(MathUtils.random(-10, 10), MathUtils.random(-80, -40));
            shootInterval = 3f;
            float radius = Math.min(sprite.getWidth(), sprite.getHeight()) / 2 * sprite.getScaleX();
            collider = new CircleCollider(x + sprite.getWidth() / 2, y + sprite.getHeight() / 2, radius);
        }

        @Override
        public void update(float delta) {
            position.add(velocity.x * delta, velocity.y * delta);
            if (position.x < 0 || position.x > Gdx.graphics.getWidth() - getWidth()) {
                velocity.x = -velocity.x;
            }
            if (position.y < -getHeight()) {
                markForRemoval();
            }
            collider.setPosition(position.x + sprite.getWidth() / 2, position.y + sprite.getHeight() / 2);
        }

        @Override
        public BulletEmission shoot() {
            Bullet bullet = new Bullet(position.x, position.y - getHeight(), -200, BulletType.HEAVY_SHOOTER_ENEMY, 0.5f);
            return new BulletEmission(bullet);
        }
    }

    public static class BossEnemy extends Enemy {
        private final Vector2 centerPoint;
        private float movementTimer;
        private static final float MOVEMENT_INTERVAL = 3f;

        public BossEnemy(float x, float y) {
            super(new Texture("ui/sprites/enemies/enemy_boss.png"), x, y, 20, 200, BulletType.BOSS_ENEMY, 0.5f);
            setScale(6f);
            centerPoint = new Vector2(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() * 0.75f);
            velocity.set(centerPoint.x - x, centerPoint.y - y).nor().scl(50);
            shootInterval = 1f;
            movementTimer = 0;
            float radius = Math.min(sprite.getWidth(), sprite.getHeight()) / 2 * sprite.getScaleX();
            collider = new CircleCollider(x + sprite.getWidth() / 2, y + sprite.getHeight() / 2, radius);
        }

        @Override
        public void update(float delta) {
            if (position.dst(centerPoint) > 10) {
                position.add(velocity.x * delta, velocity.y * delta);
            } else {
                movementTimer += delta;
                if (movementTimer >= MOVEMENT_INTERVAL) {
                    velocity.set(MathUtils.random(-30, 30), MathUtils.random(-30, 30));
                    movementTimer = 0;
                }
                position.add(velocity.x * delta, velocity.y * delta);

                // Keep the boss on screen
                position.x = MathUtils.clamp(position.x, 0, Gdx.graphics.getWidth() - getWidth());
                position.y = MathUtils.clamp(position.y, (float) Gdx.graphics.getHeight() / 2, Gdx.graphics.getHeight() - getHeight());
            }
            collider.setPosition(position.x + sprite.getWidth() / 2, position.y + sprite.getHeight() / 2);
        }

        @Override
        public BulletEmission shoot() {
            Array<Bullet> bullets = new Array<>();

            // Center bullet
            bullets.add(new Bullet(position.x, position.y - getHeight(), -300, BulletType.BOSS_ENEMY, 0.5f));

            // Left bullet
            Bullet leftBullet = new Bullet(position.x, position.y - getHeight(), -300, BulletType.BOSS_ENEMY, 0.5f);
            leftBullet.getVelocity().rotate(15);  // Rotate 15 degrees left
            bullets.add(leftBullet);

            // Right bullet
            Bullet rightBullet = new Bullet(position.x, position.y - getHeight(), -300, BulletType.BOSS_ENEMY, 0.5f);
            rightBullet.getVelocity().rotate(-15);  // Rotate 15 degrees right
            bullets.add(rightBullet);

            return new BulletEmission(bullets);
        }

//        @Override
//        public PowerUp dropItem() {
//            Array<PowerUp> items = new Array<>();
//            for (int i = 0; i < 5; i++) {
//                items.add(new Coin(position.x + i * 20, position.y));
//            }
//            items.add(new PowerBall(position.x, position.y));
//            return new MultiItem(items);
//        }
    }
}