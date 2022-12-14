package com.gulowsen.asteroidData.repository.interfaces;

import com.gulowsen.asteroidData.models.CloseApproachData;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public interface CloseApproachDataRepository extends BaseRepository {

    int findAmountForDate(LocalDate localDate) throws SQLException;
    int findAmountForDateAndAsteroidId(LocalDate localDate, String asteroidId) throws SQLException;
    void save(CloseApproachData closeApproachData) throws SQLException;
    List<CloseApproachData> findInDateRange(LocalDate from, LocalDate until) throws SQLException;

}
