package com.sahan.spaceshooter.sprites.powerups;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.sahan.spaceshooter.engine.CircleCollider;
import com.sahan.spaceshooter.sprites.Player;

public class PowerBall extends PowerUp {
    private final Animation<TextureRegion> animation;
    private float stateTime;
    private final float scale;

    public PowerBall(float x, float y) {
        super(x, y, new Texture("ui/sprites/powerups/powerup.png"), 4f);
        TextureRegion[][] tmp = TextureRegion.split(sprite.getTexture(), 16, 16);
        animation = new Animation<>(0.1f, tmp[0]);
        stateTime = 0;
        scale = 4f;
        powerUpType = PowerUpType.POWER_UP;
        float radius = Math.min(sprite.getWidth(), sprite.getHeight()) / 2 * sprite.getScaleX();
        collider = new CircleCollider(x + sprite.getWidth() / 2, y + sprite.getHeight() / 2, radius);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        stateTime += delta;
    }

    @Override
    public void draw(SpriteBatch batch) {
        TextureRegion currentFrame = animation.getKeyFrame(stateTime, true);
        float width = currentFrame.getRegionWidth() * scale;
        float height = currentFrame.getRegionHeight() * scale;
        batch.draw(currentFrame, position.x, position.y, width, height);
    }

//    @Override
//    public void applyEffect(Player player) {
//        player.increasePowerLevel();
//    }
}
