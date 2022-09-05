package com.gulowsen.asteroidData.repository.interfaces;

import com.gulowsen.asteroidData.models.AsteroidData;

import java.sql.SQLException;

public interface AsteroidDataMySQLRepository extends BaseMySQLRepository {

    void save(AsteroidData asteroidData) throws SQLException;
    AsteroidData findById(String asteroid_id) throws SQLException;


}
