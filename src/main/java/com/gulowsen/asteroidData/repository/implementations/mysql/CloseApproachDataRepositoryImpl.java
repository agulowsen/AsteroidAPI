package com.gulowsen.asteroidData.repository.implementations.mysql;

import com.gulowsen.asteroidData.models.CloseApproachData;
import com.gulowsen.asteroidData.repository.interfaces.CloseApproachDataRepository;
import com.gulowsen.asteroidData.utils.DBCPDataSource;
import com.gulowsen.asteroidData.utils.DateAndTimeHelper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.gulowsen.asteroidData.constants.mysql.DatabaseConstants.CLOSE_APPROACH_DATA_FIELD_ASTEROID_ID;
import static com.gulowsen.asteroidData.constants.mysql.DatabaseConstants.CLOSE_APPROACH_DATA_FIELD_DATE;
import static com.gulowsen.asteroidData.constants.mysql.DatabaseConstants.CLOSE_APPROACH_DATA_FIELD_ID;
import static com.gulowsen.asteroidData.constants.mysql.DatabaseConstants.CLOSE_APPROACH_DATA_FIELD_MISS_DISTANCE;
import static com.gulowsen.asteroidData.constants.mysql.DatabaseConstants.CLOSE_APPROACH_DATA_SCHEMA;
import static com.gulowsen.asteroidData.constants.mysql.DatabaseConstants.CREATED_DATETIME;

@Repository
public class CloseApproachDataRepositoryImpl extends BaseRepositoryImpl implements CloseApproachDataRepository {

    public int findAmountForDate(LocalDate localDate) throws SQLException {
        Integer count = null;
        try(Connection connection = DBCPDataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM " + CLOSE_APPROACH_DATA_SCHEMA + " where " +
                    CLOSE_APPROACH_DATA_FIELD_DATE + " = ?"
            );
            statement.setDate(1, java.sql.Date.valueOf(localDate));
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                count = resultSet.getInt(1);
            }
        }
        return count;
    }

    public int findAmountForDateAndAsteroidId(LocalDate localDate, String asteroidId) throws SQLException {
        Integer count = null;
        try(Connection connection = DBCPDataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM " + CLOSE_APPROACH_DATA_SCHEMA + " where " +
                    CLOSE_APPROACH_DATA_FIELD_DATE + " = ? and " +
                    CLOSE_APPROACH_DATA_FIELD_ASTEROID_ID + " = ?"
            );
            statement.setDate(1, java.sql.Date.valueOf(localDate));
            statement.setString(2, asteroidId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                count = resultSet.getInt(1);
            }
        }
        return count;
    }

    public void save(CloseApproachData closeApproachData) throws SQLException {
        try(Connection connection = DBCPDataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO " + CLOSE_APPROACH_DATA_SCHEMA +
                            " (" + CLOSE_APPROACH_DATA_FIELD_ASTEROID_ID + ", " +
                            CLOSE_APPROACH_DATA_FIELD_MISS_DISTANCE + ", " +
                            CLOSE_APPROACH_DATA_FIELD_DATE + ", " +
                            CREATED_DATETIME +
                            ") " +
                            " values (?,?,?,?)");
            statement.setString(1, closeApproachData.getAsteroid_id());
            statement.setString(2, String.valueOf(closeApproachData.getMissDistance()));
            LocalDate missDate = closeApproachData.getDate();
            statement.setDate(3, java.sql.Date.valueOf(missDate));
            statement.setTimestamp(4, new Timestamp(System.currentTimeMillis()));

            statement.executeUpdate();
        }
    }

    public List<CloseApproachData> findInDateRange(LocalDate from, LocalDate until) throws SQLException {
        List<CloseApproachData> closeApproachDataList = new ArrayList<>();
        try(Connection connection = DBCPDataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("select * from " + CLOSE_APPROACH_DATA_SCHEMA +
                    " where " + CLOSE_APPROACH_DATA_FIELD_DATE + " between '" + from + "' and '" + until + "'");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                CloseApproachData closeApproachData = new CloseApproachData();
                closeApproachData.setMissDistance(new BigDecimal(resultSet.getString(CLOSE_APPROACH_DATA_FIELD_MISS_DISTANCE)));
                closeApproachData.setAsteroid_id(resultSet.getString(CLOSE_APPROACH_DATA_FIELD_ASTEROID_ID));
                closeApproachData.setId(resultSet.getInt(CLOSE_APPROACH_DATA_FIELD_ID));
                closeApproachData.setDate(resultSet.getDate(CLOSE_APPROACH_DATA_FIELD_DATE).toLocalDate());
                closeApproachData.setCreated(DateAndTimeHelper.parseTimestampToLocaleDateTime(resultSet.getString(CREATED_DATETIME)));
                closeApproachDataList.add(closeApproachData);
            }
        }
        return closeApproachDataList;
    }
}
