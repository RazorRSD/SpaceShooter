package com.sahan.spaceshooter.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Explosion {
    private static final float FRAME_LENGTH = 0.1f;
    private static final int OFFSET = 16;

    private final Animation<TextureRegion> explosionAnimation;
    private float stateTime;
    private final Vector2 position;
    private final float scale;
    private Sound explosionSound;
    private boolean soundPlayed;
    private AssetManager assetManager;

    public Explosion(float x, float y, float scale, AssetManager assetManager) {
        this.position = new Vector2(x, y);
        this.stateTime = 0;
        this.scale = scale;
        this.soundPlayed = false;
        this.assetManager = assetManager;

        Texture explosionTexture = new Texture(Gdx.files.internal("ui/sprites/explosion.png"));
        TextureRegion[][] explosionFrames = TextureRegion.split(explosionTexture, OFFSET, OFFSET);

        TextureRegion[] explosionFramesArray = new TextureRegion[8];
        int index = 0;
        for (int i = 0; i < 1; i++) {
            for (int j = 0; j < 8; j++) {
                explosionFramesArray[index++] = explosionFrames[i][j];
            }
        }

        explosionAnimation = new Animation<>(FRAME_LENGTH, explosionFramesArray);

        if (explosionSound == null) {
            try {
                explosionSound = Gdx.audio.newSound(Gdx.files.internal("sfx/menu_loop.mp3"));
                Gdx.app.log("Explosion", "Explosion MP3 sound loaded successfully");
            } catch (Exception e2) {
                Gdx.app.error("Explosion", "Could not load explosion MP3 sound", e2);
            }
        }

    }

    public void update(float delta) {
        stateTime += delta;

        if (!soundPlayed && assetManager.isLoaded("sfx/explosion.wav")) {
            Sound explosionSound = assetManager.get("sfx/explosion.wav", Sound.class);
            long soundId = explosionSound.play(0.5f);
            Gdx.app.log("Explosion", "Playing explosion sound, ID: " + soundId);
            soundPlayed = true;
        }

    }

    public void draw(SpriteBatch batch) {
        TextureRegion currentFrame = explosionAnimation.getKeyFrame(stateTime, false);
        float width = currentFrame.getRegionWidth() * scale;
        float height = currentFrame.getRegionHeight() * scale;
        batch.draw(currentFrame, position.x - width / 2, position.y - height / 2, width, height);
    }

    public boolean isFinished() {
        return explosionAnimation.isAnimationFinished(stateTime);
    }

    public void dispose() {
        explosionSound.dispose();
        explosionAnimation.getKeyFrame(0).getTexture().dispose();
    }
}