package com.gulowsen.asteroidData.models;

import java.util.List;

public class NearbyResponse {

    private List<AsteroidData> asteroids;

    public NearbyResponse(List<AsteroidData> asteroidData) {
        this.asteroids = asteroidData;
    }

    public List<AsteroidData> getAsteroids() {
        return asteroids;
    }
}

