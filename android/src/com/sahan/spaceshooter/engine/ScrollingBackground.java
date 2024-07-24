package com.sahan.spaceshooter.engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

public class ScrollingBackground {
    private final TextureRegion backgroundRegion;
    private float yScroll;
    private final float scrollSpeed;
    private final int screenWidth;
    private final int screenHeight;
    private final Array<Planet> planets;
    private final float planetScrollSpeed;

    private static class Planet {
        TextureRegion region;
        float x, y;
        float scale;
        boolean isLeftSide;
        float visibleHeight;

        Planet(TextureRegion region, float x, float y, float scale, boolean isLeftSide) {
            this.region = region;
            this.x = x;
            this.y = y;
            this.scale = scale;
            this.isLeftSide = isLeftSide;
            this.visibleHeight = region.getRegionHeight() * scale;
        }
    }

    public ScrollingBackground(Texture backgroundTexture, float scrollSpeed) {
        this.scrollSpeed = scrollSpeed;
        this.planetScrollSpeed = scrollSpeed * 0.5f;
        this.screenWidth = Gdx.graphics.getWidth();
        this.screenHeight = Gdx.graphics.getHeight();
        this.yScroll = 0;
        this.planets = new Array<>();

        // Setup background
        TextureRegion tempRegion = new TextureRegion(backgroundTexture);
        backgroundRegion = new TextureRegion(backgroundTexture);
        backgroundRegion.setRegion(tempRegion);
        backgroundRegion.getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        // Adjust background region to fit screen aspect ratio
        float regionAspectRatio = (float)backgroundRegion.getRegionWidth() / (float)backgroundRegion.getRegionHeight();
        float screenAspectRatio = (float)screenWidth / (float)screenHeight;

        if (regionAspectRatio > screenAspectRatio) {
            float newWidth = (float)backgroundRegion.getRegionHeight() * screenAspectRatio;
            float widthDifference = backgroundRegion.getRegionWidth() - newWidth;
            backgroundRegion.setRegion(
                    (int)(widthDifference / 2), 0,
                    (int)newWidth, backgroundRegion.getRegionHeight()
            );
        } else {
            float newHeight = (float)backgroundRegion.getRegionWidth() / screenAspectRatio;
            float heightDifference = backgroundRegion.getRegionHeight() - newHeight;
            backgroundRegion.setRegion(
                    0, (int)(heightDifference / 2),
                    backgroundRegion.getRegionWidth(), (int)newHeight
            );
        }

        // Initialize planets
        initializePlanets();
    }

    private void initializePlanets() {
        String[] planetPaths = {
                "ui/bgs/bgObjects/planet_1.png",
                "ui/bgs/bgObjects/planet_2.png",
                "ui/bgs/bgObjects/planet_6.png"
        };

        for (String path : planetPaths) {
            try {
                Texture planetTexture = new Texture(Gdx.files.internal(path));
                TextureRegion planetRegion = new TextureRegion(planetTexture);
                addPlanet(planetRegion);
            } catch (Exception e) {
                Gdx.app.error("ScrollingBackground", "Error loading planet texture: " + path, e);
            }
        }

        // Ensure we have at least one planet
        if (planets.size == 0) {
            Gdx.app.error("ScrollingBackground", "No planet textures could be loaded. Using fallback texture.");
            Texture fallbackTexture = createFallbackTexture();
            TextureRegion fallbackRegion = new TextureRegion(fallbackTexture);
            addPlanet(fallbackRegion);
        }
    }

    private Texture createFallbackTexture() {
        Pixmap pixmap = new Pixmap(64, 64, Pixmap.Format.RGB888);
        pixmap.setColor(Color.PURPLE); // You can change this color as needed
        pixmap.fill();
        Texture fallbackTexture = new Texture(pixmap);
        pixmap.dispose();
        return fallbackTexture;
    }

    private void addPlanet(TextureRegion planetRegion) {
        float scale = MathUtils.random(10f, 20f);
        boolean isLeftSide = MathUtils.randomBoolean();
        float x, y;

        if (isLeftSide) {
            x = -planetRegion.getRegionWidth() * scale * MathUtils.random(0.3f, 0.7f);
        } else {
            x = screenWidth - planetRegion.getRegionWidth() * scale * MathUtils.random(0.3f, 0.7f);
        }

        // Find a non-overlapping y position
        y = findNonOverlappingYPosition(planetRegion, scale, isLeftSide);

        planets.add(new Planet(planetRegion, x, y, scale, isLeftSide));
    }

    private float findNonOverlappingYPosition(TextureRegion planetRegion, float scale, boolean isLeftSide) {
        float planetHeight = planetRegion.getRegionHeight() * scale;
        float y;
        boolean overlaps;
        int attempts = 0;
        final int MAX_ATTEMPTS = 100;

        do {
            y = MathUtils.random(-planetHeight * 0.5f, screenHeight);
            overlaps = false;

            for (Planet existingPlanet : planets) {
                if (existingPlanet.isLeftSide == isLeftSide) {
                    if (Math.abs(y - existingPlanet.y) < Math.max(planetHeight, existingPlanet.visibleHeight)) {
                        overlaps = true;
                        break;
                    }
                }
            }

            attempts++;
            if (attempts >= MAX_ATTEMPTS) {
                Gdx.app.log("ScrollingBackground", "Could not find non-overlapping position after " + MAX_ATTEMPTS + " attempts");
                return screenHeight + planetHeight; // Place it above the screen as a last resort
            }
        } while (overlaps);

        return y;
    }

    public void update(float deltaTime) {
        yScroll += scrollSpeed * deltaTime;
        if (yScroll > screenHeight) {
            yScroll = 0;
        }

        Array<Planet> planetsToReset = new Array<>();

        // Update planets
        for (Planet planet : planets) {
            planet.y -= planetScrollSpeed * deltaTime;
            if (planet.y + planet.visibleHeight < 0) {
                planetsToReset.add(planet);
            }
        }

        // Reset planets that have gone off screen
        for (Planet planet : planetsToReset) {
            resetPlanet(planet);
        }

        // Ensure we always have at least one planet
        if (planets.size < 1) {
            addPlanet(planets.first().region);
        }
    }

    private void resetPlanet(Planet planet) {
        planet.y = findNonOverlappingYPosition(planet.region, planet.scale, planet.isLeftSide);
        planet.isLeftSide = MathUtils.randomBoolean();
        if (planet.isLeftSide) {
            planet.x = -planet.region.getRegionWidth() * planet.scale * MathUtils.random(0.3f, 0.7f);
        } else {
            planet.x = screenWidth - planet.region.getRegionWidth() * planet.scale * MathUtils.random(0.3f, 0.7f);
        }
    }


    public void render(SpriteBatch batch) {
        batch.draw(backgroundRegion, 0, -yScroll, screenWidth, screenHeight);
        batch.draw(backgroundRegion, 0, -yScroll + screenHeight, screenWidth, screenHeight);

        // Draw planets
        for (Planet planet : planets) {
            batch.draw(planet.region, planet.x, planet.y,
                    planet.region.getRegionWidth() * planet.scale,
                    planet.region.getRegionHeight() * planet.scale);
        }
    }

    public void dispose() {
        backgroundRegion.getTexture().dispose();
        for (Planet planet : planets) {
            planet.region.getTexture().dispose();
        }
    }
}