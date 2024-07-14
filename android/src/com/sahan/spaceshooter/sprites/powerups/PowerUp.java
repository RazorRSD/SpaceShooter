package com.sahan.spaceshooter.sprites.powerups;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.sahan.spaceshooter.engine.CircleCollider;

public abstract class PowerUp {
    protected Vector2 position;
    protected Sprite sprite;
    protected boolean active;
    protected CircleCollider collider;
    protected PowerUpType powerUpType;
    private boolean collected;

    public PowerUp(float x, float y, Texture texture, float scale) {
        position = new Vector2(x, y);
        sprite = new Sprite(texture);
        sprite.setScale(scale);
        active = true;
        float radius = Math.min(sprite.getWidth(), sprite.getHeight()) / 2 * sprite.getScaleX();
        collider = new CircleCollider(x + sprite.getWidth() / 2, y + sprite.getHeight() / 2, radius);
    }

    public void update(float delta) {
        position.y -= 100 * delta; // Move downwards
        if (position.y < -sprite.getHeight()) {
            active = false;
        }
        collider.setPosition(position.x + sprite.getWidth() / 2, position.y + sprite.getHeight() / 2);
    }

    public CircleCollider getCollider(){
        return collider;
    }

    public PowerUpType getPowerUpType() {
        return powerUpType;
    }

    public void draw(SpriteBatch batch) {
        sprite.setPosition(position.x, position.y);
        sprite.draw(batch);
    }

    public boolean isActive() {
        return active;
    }

    public boolean isCollected () {
        return collected;
    }

    public void setCollected () {
        active =  false;
        collected = true;
    }

    public void dispose() {
        sprite.getTexture().dispose();
    }
}

