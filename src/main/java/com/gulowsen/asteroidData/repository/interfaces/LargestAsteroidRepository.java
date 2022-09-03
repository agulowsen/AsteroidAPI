package com.gulowsen.asteroidData.repository.interfaces;

import com.gulowsen.asteroidData.models.AsteroidData;

import java.sql.SQLException;

public interface LargestAsteroidRepository {
    AsteroidData getLargestAsteroidByYear(int year) throws SQLException;

    void save(AsteroidData currentLargest, int year) throws SQLException;
}
