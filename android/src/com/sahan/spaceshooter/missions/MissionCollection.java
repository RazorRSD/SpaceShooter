package com.sahan.spaceshooter.missions;

import com.badlogic.gdx.utils.Array;
import com.sahan.spaceshooter.engine.EntityManager;
import com.sahan.spaceshooter.sprites.enemies.Enemy;

public class MissionCollection {
    public static class Mission {
        public String name;
        public Array<Wave> waves;

        public Mission(String name) {
            this.name = name;
            this.waves = new Array<>();
        }
    }

    public static class Wave {
        public Runnable spawnMethod;
        public Runnable updateMethod;

        public Wave(Runnable spawnMethod, Runnable updateMethod) {
            this.spawnMethod = spawnMethod;
            this.updateMethod = updateMethod;
        }
    }

    public static Array<Mission> getMissions(EntityManager entityManager) {
        Array<Mission> missions = new Array<>();

        // Mission 1
        Mission mission1 = new Mission("Asteroid Field");
        mission1.waves.add(new Wave(
                () -> WaveCollection.spawnWave1(entityManager),
                () -> WaveCollection.updateWave1(entityManager.getEnemies())
        ));
        mission1.waves.add(new Wave(
                () -> WaveCollection.spawnWave2(entityManager),
                () -> WaveCollection.updateWave2(entityManager.getEnemies())
        ));
        mission1.waves.add(new Wave(
                () -> WaveCollection.spawnWave3(entityManager),
                () -> WaveCollection.updateWave3(entityManager.getEnemies())
        ));
        missions.add(mission1);

        // Add more missions here...
        Mission mission2 = new Mission("Invasion");
        mission2.waves.add(new Wave(
                () -> WaveCollection.spawnWave1(entityManager),
                () -> WaveCollection.updateWave1(entityManager.getEnemies())
        ));
        mission2.waves.add(new Wave(
                () -> WaveCollection.spawnWave2(entityManager),
                () -> WaveCollection.updateWave2(entityManager.getEnemies())
        ));
        mission2.waves.add(new Wave(
                () -> WaveCollection.spawnWave3(entityManager),
                () -> WaveCollection.updateWave3(entityManager.getEnemies())
        ));
        missions.add(mission2);

        return missions;
    }
}