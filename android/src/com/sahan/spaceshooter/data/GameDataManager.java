package com.sahan.spaceshooter.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Json;

public class GameDataManager {
    private static final String PREF_NAME = "SpaceShooterGameData";
    private static final String KEY_COMPLETED_MISSIONS = "completedMissions";
    private static final String KEY_BANK = "bank";
    private static final String KEY_SCORE = "score";
    private static final String KEY_LIVES = "lives";
    private static final String KEY_POWER_LEVEL = "powerLevel";
    private static final String KEY_POWER_UP_POINTS = "powerUpPoints";
    private static final String KEY_PLAYER_UPGRADES = "playerUpgrades";

    private final Preferences prefs;
    private final Json json;

    public GameDataManager() {
        prefs = Gdx.app.getPreferences(PREF_NAME);
        json = new Json();
    }

    public void saveGameProgress(String completedMissions, int bank, int score, int lives, int powerLevel,int powerUpPoints,  PlayerUpgrades upgrades) {
        prefs.putString(KEY_COMPLETED_MISSIONS, completedMissions);
        prefs.putInteger(KEY_BANK, bank);
        prefs.putInteger(KEY_SCORE, score);
        prefs.putInteger(KEY_LIVES, lives);
        prefs.putInteger(KEY_POWER_LEVEL,powerLevel);
        prefs.putInteger(KEY_POWER_UP_POINTS, powerUpPoints);
        prefs.putString(KEY_PLAYER_UPGRADES, json.toJson(upgrades));
        prefs.flush();
    }

    public void updateBank(int bank) {
        prefs.putInteger(KEY_BANK, bank);
        prefs.flush();
    }

    public GameProgress loadGameProgress() {
        String completedMissions = prefs.getString(KEY_COMPLETED_MISSIONS, "Asteroid Field");
        int bank = prefs.getInteger(KEY_BANK, 0);
        int score = prefs.getInteger(KEY_SCORE, 0);
        int lives =  prefs.getInteger(KEY_LIVES, 0);
        int powerLevel =  prefs.getInteger(KEY_POWER_LEVEL, 0);
        int powerUpPoints =  prefs.getInteger(KEY_POWER_UP_POINTS, 0);
        PlayerUpgrades upgrades = json.fromJson(PlayerUpgrades.class, prefs.getString(KEY_PLAYER_UPGRADES, "{}"));
        return new GameProgress(completedMissions, bank, score, lives,powerLevel,powerUpPoints, upgrades);
    }

    public boolean hasSavedGame() {
        return prefs.contains(KEY_COMPLETED_MISSIONS);
    }

    public void resetGameProgress(int bank, PlayerUpgrades upgrades) {
        prefs.putString(KEY_COMPLETED_MISSIONS, "Asteroid Field");
        prefs.putInteger(KEY_BANK, bank);
        prefs.putInteger(KEY_SCORE, 0);
        prefs.putInteger(KEY_LIVES, 3);
        prefs.putInteger(KEY_POWER_LEVEL,0);
        prefs.putInteger(KEY_POWER_UP_POINTS, 0);
        prefs.putString(KEY_PLAYER_UPGRADES, json.toJson(upgrades));
        prefs.flush();
    }
}
