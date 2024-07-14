package com.sahan.spaceshooter.sprites.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.sahan.spaceshooter.engine.CircleCollider;
import com.sahan.spaceshooter.sprites.powerups.Coin;
import com.sahan.spaceshooter.sprites.powerups.PowerBall;
import com.sahan.spaceshooter.sprites.powerups.PowerUp;
import com.sahan.spaceshooter.sprites.bullets.BulletEmission;
import com.sahan.spaceshooter.sprites.bullets.BulletType;

public abstract class Enemy {
    protected Sprite sprite;
    protected Vector2 position;
    protected Vector2 velocity;
    protected int health;
    protected int score;
    protected boolean markedForRemoval;
    protected float shootTimer;
    protected float shootInterval;
    protected BulletType bulletType;
    protected float bulletScale;
    protected CircleCollider collider;
    protected float dropChance = 1f;

    public Enemy(Texture texture, float x, float y, int health, int score, BulletType bulletType, float bulletScale) {
        this.sprite = new Sprite(texture);
        this.position = new Vector2(x, y);
        this.velocity = new Vector2();
        this.health = health;
        this.score = score;
        this.markedForRemoval = false;
        this.shootTimer = 0;
        this.bulletType = bulletType;
        this.bulletScale = bulletScale;
        this.shootInterval = Float.MAX_VALUE;
        float radius = Math.min(sprite.getWidth(), sprite.getHeight()) / 2 * sprite.getScaleX();
        collider = new CircleCollider(x + sprite.getWidth() / 2, y + sprite.getHeight() / 2, radius);
    }

    public abstract void update(float delta);

    public void draw(SpriteBatch batch) {
        sprite.setPosition(position.x, position.y);
        sprite.draw(batch);
    }

    public void takeDamage(int damage) {
        health -= damage;
        if (health <= 0) {
            markForRemoval();
        }
    }

    public PowerUp dropItem() {
        if (Math.random() < dropChance) {
            if (Math.random() < 0.8) {
                return new Coin(position.x, position.y);
            } else {
                return new PowerBall(position.x, position.y);
            }
        }
        return null;
    }

    public void markForRemoval() {
        markedForRemoval = true;
        Gdx.app.log("Enemy", this.getClass().getSimpleName() + " marked for removal");
    }

    public boolean isDestroyed() {
        return markedForRemoval;
    }

    public int getScore() {
        return score;
    }

    public Vector2 getPosition() {
        return position;
    }

    public float getWidth() {
        return sprite.getWidth() * sprite.getScaleX();
    }

    public float getHeight() {
        return sprite.getHeight() * sprite.getScaleY();
    }

    public void setScale(float scale) {
        sprite.setScale(scale);
    }

    public boolean canShoot() {
        shootTimer += Gdx.graphics.getDeltaTime();
        if (shootTimer >= shootInterval) {
            shootTimer = 0;
            return true;
        }
        return false;
    }

    public abstract BulletEmission shoot();

    public CircleCollider getCollider() {
        return collider;
    }

    public void dispose() {
        sprite.getTexture().dispose();
    }
}