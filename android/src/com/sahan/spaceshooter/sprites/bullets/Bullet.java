package com.sahan.spaceshooter.sprites.bullets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.sahan.spaceshooter.engine.CircleCollider;

public class Bullet {
    private final Sprite sprite;
    private final Vector2 position;
    private final Vector2 velocity;
    private boolean active;
    private final BulletType type;
    private final CircleCollider collider;

    public Bullet(float x, float y, float velocityY, BulletType type, float scale) {
        this.type = type;
        Texture texture = getTextureForType(type);
        sprite = new Sprite(texture);
        sprite.setScale(scale);
        position = new Vector2(x - sprite.getWidth() * scale / 2, y);
        velocity = new Vector2(0, velocityY);
        active = true;
        float radius = Math.min(sprite.getWidth(), sprite.getHeight()) / 2 * sprite.getScaleX();
        collider = new CircleCollider(x + sprite.getWidth() / 2, y + sprite.getHeight() / 2, radius);
    }

    private Texture getTextureForType(BulletType type) {
        switch (type) {
            case B1:
                return new Texture(Gdx.files.internal("ui/sprites/bullet/b1.png"));
            case BASIC_ENEMY:
                return new Texture(Gdx.files.internal("ui/sprites/bullet/bullet_basic_enemy.png"));
            case SHOOTER_ENEMY:
                return new Texture(Gdx.files.internal("ui/sprites/bullet/bullet_shooter_enemy.png"));
            case HEAVY_SHOOTER_ENEMY:
                return new Texture(Gdx.files.internal("ui/sprites/bullet/bullet_heavy_shooter_enemy.png"));
            case BOSS_ENEMY:
                return new Texture(Gdx.files.internal("ui/sprites/bullet/bullet_boss_enemy.png"));
            case B2_SINGLE:
                return new Texture(Gdx.files.internal("ui/sprites/bullet/b2.png"));
            default:
                return new Texture(Gdx.files.internal("ui/sprites/bullet/bullet_default.png"));
        }
    }

    public void update(float delta) {
        position.add(velocity.x * delta, velocity.y * delta);
        if (position.y > Gdx.graphics.getHeight() || position.y < 0 ||
                position.x < 0 || position.x > Gdx.graphics.getWidth()) {
            active = false;
        }
        collider.setPosition(position.x + sprite.getWidth() / 2, position.y + sprite.getHeight() / 2);
    }

    public void draw(SpriteBatch batch) {
        sprite.setPosition(position.x, position.y);
        sprite.draw(batch);
    }

    public CircleCollider getCollider() {
        return collider;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
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

    public Vector2 getVelocity() {
        return velocity;
    }

    public BulletType getType() {
        return type;
    }

    public void dispose() {
        sprite.getTexture().dispose();
    }
}