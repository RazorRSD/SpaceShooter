package com.sahan.spaceshooter.data;

public class GameProgress {
    public String completedMissions;
    public int bank;
    public int score;
    public int lives;
    public int powerLevel;
    public int powerUpPoints;
    public PlayerUpgrades upgrades;

    public GameProgress(String completedMissions, int bank, int score, int lives, int powerLevel, int powerUpPoints, PlayerUpgrades upgrades) {
        this.completedMissions = completedMissions;
        this.bank = bank;
        this.score = score;
        this.lives = lives;
        this.powerLevel = powerLevel;
        this.powerUpPoints = powerUpPoints;
        this.upgrades = upgrades;
    }
}
