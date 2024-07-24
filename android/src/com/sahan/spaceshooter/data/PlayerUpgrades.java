package com.sahan.spaceshooter.data;

import com.badlogic.gdx.utils.Array;

public class PlayerUpgrades {
    public int weaponLevel;
    public int shieldLevel;
    public int engineLevel;
    public Array<String> ownedShips;

    public PlayerUpgrades() {
        weaponLevel = 1;
        shieldLevel = 1;
        engineLevel = 1;
        ownedShips = new Array<>();
        ownedShips.add("Basic");
    }
}
