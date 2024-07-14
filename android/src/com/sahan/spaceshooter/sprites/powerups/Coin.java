package com.sahan.spaceshooter.sprites.powerups;

import com.badlogic.gdx.graphics.Texture;
import com.sahan.spaceshooter.engine.CircleCollider;

public class Coin extends PowerUp {
    public Coin(float x, float y) {
        super(x, y, new Texture("ui/sprites/powerups/coin.png"), 4f);
        powerUpType = PowerUpType.COIN;
        float radius = Math.min(sprite.getWidth(), sprite.getHeight()) / 2 * sprite.getScaleX();
        collider = new CircleCollider(x + sprite.getWidth() / 2, y + sprite.getHeight() / 2, radius);
    }
}
