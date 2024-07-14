package com.sahan.spaceshooter.sprites.bullets;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

public class BulletEmission {
    private Array<Bullet> bullets;

    public BulletEmission(Bullet bullet) {
        bullets = new Array<>();
        bullets.add(bullet);
    }

    public BulletEmission(Array<Bullet> bullets) {
        this.bullets = bullets;
    }

    public void update(float delta) {
        for (Bullet bullet : bullets) {
            bullet.update(delta);
        }
        Array<Bullet> toRemoveBullets = new Array<>();
        for(Bullet bullet: bullets){
            if(bullet == null || !bullet.isActive()){
                toRemoveBullets.add(bullet);
            }
        }
        bullets.removeAll(toRemoveBullets, true);
    }

    public void draw(SpriteBatch batch) {
        for (Bullet bullet : bullets) {
            bullet.draw(batch);
        }
    }

    public Array<Bullet> getBullets() {
        return bullets;
    }

    public boolean isActive() {
        return bullets.size > 0;
    }

    public void dispose() {
        for (Bullet bullet : bullets) {
            bullet.dispose();
        }
        bullets.clear();
    }
}