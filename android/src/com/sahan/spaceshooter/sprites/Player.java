package com.sahan.spaceshooter.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.sahan.spaceshooter.engine.CircleCollider;
import com.sahan.spaceshooter.sprites.bullets.Bullet;
import com.sahan.spaceshooter.sprites.bullets.BulletEmission;
import com.sahan.spaceshooter.sprites.bullets.BulletType;

public class Player {
    private final Sprite sprite;
    private Animation<TextureRegion> thrusterAnimation;
    private float thrusterStateTime;
    private final Vector2 position;
    private final Vector2 lastPosition;
    private float shootTimer;
    private float SHOOT_INTERVAL = 0.5f;
    private static final float MOVEMENT_THRESHOLD = 0.1f;
    private int health;
    private static final int MAX_HEALTH = 100;
    private static final float THRUSTER_FRAME_DURATION = 0.1f;
    private static final float THRUSTER_SCALE = 8.0f;
    private static final float THRUSTER_FADE_DELAY = 0.3f; // 300ms delay
    private float thrusterFadeTimer;
    private boolean isMoving;
    private final CircleCollider collider;
    private boolean invincible = false;
    private float invincibilityTimer = 0;
    private static final float INVINCIBILITY_DURATION = 2f;
    private boolean blinking = false;
    private float blinkTimer = 0;
    private static final float BLINK_INTERVAL = 0.1f;
    private int powerLevel;
    private int powerBallCount;
    private BulletType currentBulletType;
    private Animation<TextureRegion> powerBarAnimation;
    private float powerBarStateTime;

    public Player(float x, float y) {
        Texture texture;
        try {
            texture = new Texture(Gdx.files.internal("ui/sprites/player/player.png"));
        } catch (Exception e) {
            Gdx.app.error("Player", "Could not load player.png, using fallback texture", e);
            Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
            pixmap.setColor(1, 1, 1, 1);
            pixmap.fill();
            texture = new Texture(pixmap);
            pixmap.dispose();
        }
        sprite = new Sprite(texture);
        sprite.setScale(0.5f);
        health = MAX_HEALTH;

        position = new Vector2(x, y);
        lastPosition = new Vector2(x, y);
        shootTimer = 0;
        thrusterStateTime = 0;
        thrusterFadeTimer = 0;
        isMoving = false;

        float radius = Math.min(sprite.getWidth(), sprite.getHeight()) / 2 * sprite.getScaleX();
        collider = new CircleCollider(x + sprite.getWidth() / 2, y + sprite.getHeight() / 2, radius);

        initializeThrusterAnimation();

        powerLevel = 0;
        powerBallCount = 0;
        currentBulletType = BulletType.B1;
        Texture powerBarTexture = new Texture("ui/sprites/powerups/powerupbar.png");
        TextureRegion[][] tmp = TextureRegion.split(powerBarTexture, 48, 48);
        powerBarAnimation = new Animation<>(0.1f, tmp[0]);
        powerBarStateTime = 0;
    }

    private void initializeThrusterAnimation() {
        try {
            Texture thrusterTexture = new Texture(Gdx.files.internal("ui/sprites/player/thruster.png"));
            TextureRegion[][] tmp = TextureRegion.split(thrusterTexture,
                    thrusterTexture.getWidth() / 4,
                    thrusterTexture.getHeight());

            TextureRegion[] thrusterFrames = new TextureRegion[4];
            int index = 0;
            for (int i = 0; i < 1; i++) {
                for (int j = 0; j < 4; j++) {
                    thrusterFrames[index++] = tmp[i][j];
                }
            }
            thrusterAnimation = new Animation<>(THRUSTER_FRAME_DURATION, thrusterFrames);
        } catch (Exception e) {
            Gdx.app.error("Player", "Could not load thruster.png", e);
            thrusterAnimation = null;
        }
    }

    public void takeDamage(int damage) {
        if (!invincible) {
            health -= damage;
            if (health < 0) {
                health = 0;
            }
        }
    }

    public void update(float delta) {
        shootTimer += delta;
        thrusterStateTime += delta;

        boolean currentlyMoving = isCurrentlyMoving();
        if (currentlyMoving) {
            isMoving = true;
            thrusterFadeTimer = THRUSTER_FADE_DELAY;
        } else if (isMoving) {
            thrusterFadeTimer -= delta;
            if (thrusterFadeTimer <= 0) {
                isMoving = false;
            }
        }

        if (invincible) {
            invincibilityTimer += delta;
            if (invincibilityTimer >= INVINCIBILITY_DURATION) {
                invincible = false;
                blinking = false;
                invincibilityTimer = 0;
            }

            if (blinking) {
                blinkTimer += delta;
                if (blinkTimer >= BLINK_INTERVAL) {
                    blinkTimer = 0;
                    sprite.setAlpha(sprite.getColor().a == 1 ? 0.5f : 1f);
                }
            }
        }
        powerBarStateTime += delta;

        lastPosition.set(position);

        collider.setPosition(position.x + sprite.getWidth() / 2, position.y + sprite.getHeight() / 2);
    }

    public boolean isAlive() {
        return health > 0;
    }

    public int getHealth() {
        return health;
    }

    public void resetHealth() {
        health = MAX_HEALTH;
    }

    public void setInvincible(boolean invincible) {
        this.invincible = invincible;
        this.invincibilityTimer = 0;
    }

    public void startBlinking() {
        blinking = true;
        blinkTimer = 0;
    }

    public void draw(SpriteBatch batch) {
        sprite.setPosition(position.x, position.y);
        sprite.draw(batch);

        if (isMoving && thrusterAnimation != null) {
            TextureRegion currentFrame = thrusterAnimation.getKeyFrame(thrusterStateTime, true);
            float thrusterWidth = currentFrame.getRegionWidth() * THRUSTER_SCALE;
            float thrusterHeight = currentFrame.getRegionHeight() * THRUSTER_SCALE;
            float thrusterX = position.x + sprite.getWidth() / 2 - thrusterWidth / 2;
            float thrusterY = position.y;
            batch.draw(currentFrame, thrusterX, thrusterY, thrusterWidth, thrusterHeight);
        }

        if (!invincible || (invincible && sprite.getColor().a == 1)) {
            sprite.draw(batch);
        }

        batch.draw(powerBarAnimation.getKeyFrame(powerBarStateTime, true), 10, 10);
    }

    public void increasePowerLevel() {
        powerBallCount++;
        if (powerBallCount >= 5) {
            powerBallCount = 0;
            powerLevel++;
            if (powerLevel > 3) {
                powerLevel = 0;
                upgradeBulletType();
            }
            updateFireRate();
        }
    }

    private void updateFireRate() {
        SHOOT_INTERVAL = 0.5f - (powerLevel * 0.1f);
    }

    public void setPosition(float x, float y) {
        position.set(x, y);

        position.set(x, y);
        collider.setPosition(x + sprite.getWidth() / 2, y + sprite.getHeight() / 2);
    }

    public CircleCollider getCollider() {
        return collider;
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

    private void upgradeBulletType() {
        switch (currentBulletType) {
            case B1:
                currentBulletType = BulletType.B2_SINGLE;
                break;
            case B2_SINGLE:
                currentBulletType = BulletType.B2_DOUBLE;
                break;
            case B2_DOUBLE:
                currentBulletType = BulletType.B2_ANGLE;
                break;
            case B2_ANGLE:
                currentBulletType = BulletType.B2_FULL;
                break;
            case B2_FULL:
                currentBulletType = BulletType.B3_SINGLE;
                break;
            // ... (continue for B3 types)
        }
    }

    public BulletEmission shoot() {
        Array<Bullet> bullets = new Array<>();
        switch (currentBulletType) {
            case B1:
                bullets.add(new Bullet(position.x + getWidth() / 2, position.y + getHeight(), 300, BulletType.B1, 1f));
                break;
            case B2_DOUBLE:
                bullets.add(new Bullet(position.x + getWidth() / 4, position.y + getHeight(), 300, BulletType.B1, 1f));
                bullets.add(new Bullet(position.x + getWidth() * 3 / 4, position.y + getHeight(), 300, BulletType.B1, 1f));
                break;
            case B2_ANGLE:
                bullets.add(new Bullet(position.x + getWidth() / 2, position.y + getHeight(), 300, BulletType.B1, 1f));
                Bullet leftBullet = new Bullet(position.x, position.y + getHeight(), 300, BulletType.B1, 1f);
                leftBullet.getVelocity().rotate(15);
                bullets.add(leftBullet);
                Bullet rightBullet = new Bullet(position.x + getWidth(), position.y + getHeight(), 300, BulletType.B1, 1f);
                rightBullet.getVelocity().rotate(-15);
                bullets.add(rightBullet);
                break;
            // ... (continue for other bullet types)
        }
        return new BulletEmission(bullets);
    }

    public boolean canShoot() {
        if (shootTimer >= SHOOT_INTERVAL) {
            shootTimer = 0;
            return true;
        }
        return false;
    }

    private boolean isCurrentlyMoving() {
        return position.dst(lastPosition) > MOVEMENT_THRESHOLD;
    }

    public void dispose() {
        sprite.getTexture().dispose();
        if (thrusterAnimation != null) {
            thrusterAnimation.getKeyFrame(0).getTexture().dispose();
        }
    }
}