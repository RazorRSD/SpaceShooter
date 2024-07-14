package com.sahan.spaceshooter.engine;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

public class CircleCollider {
    private final Circle circle;

    public CircleCollider(float x, float y, float radius) {
        circle = new Circle(x, y, radius);
    }

    public void setPosition(float x, float y) {
        circle.setPosition(x, y);
    }

    public boolean overlaps(CircleCollider other) {
        return circle.overlaps(other.getCircle());
    }

    public Circle getCircle() {
        return circle;
    }

    public Vector2 getCenter() {
        return new Vector2(circle.x, circle.y);
    }

    public float getRadius() {
        return circle.radius;
    }

    public void drawDebug(ShapeRenderer shapeRenderer) {
        shapeRenderer.circle(circle.x, circle.y, circle.radius);
    }
}