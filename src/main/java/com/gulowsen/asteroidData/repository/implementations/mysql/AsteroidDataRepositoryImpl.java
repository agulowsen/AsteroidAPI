package com.gulowsen.asteroidData.repository.implementations.mysql;

import com.gulowsen.asteroidData.models.AsteroidData;
import com.gulowsen.asteroidData.repository.interfaces.AsteroidDataRepository;
import com.gulowsen.asteroidData.utils.DBCPDataSource;
import com.gulowsen.asteroidData.utils.DateAndTimeHelper;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import static com.gulowsen.asteroidData.constants.mysql.DatabaseConstants.ASTEROID_DATA_FIELD_ID;
import static com.gulowsen.asteroidData.constants.mysql.DatabaseConstants.ASTEROID_DATA_FIELD_MAX_DIAM;
import static com.gulowsen.asteroidData.constants.mysql.DatabaseConstants.ASTEROID_DATA_FIELD_MIN_DIAM;
import static com.gulowsen.asteroidData.constants.mysql.DatabaseConstants.ASTEROID_DATA_FIELD_NAME;
import static com.gulowsen.asteroidData.constants.mysql.DatabaseConstants.ASTEROID_DATA_SCHEMA;
import static com.gulowsen.asteroidData.constants.mysql.DatabaseConstants.CREATED_DATETIME;

@Repository
public class AsteroidDataRepositoryImpl extends BaseRepositoryImpl implements AsteroidDataRepository {

    public void save(AsteroidData asteroidData) throws SQLException {
        try(Connection connection = DBCPDataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO " + ASTEROID_DATA_SCHEMA +
                            " (" + ASTEROID_DATA_FIELD_ID + ", " +
                            ASTEROID_DATA_FIELD_NAME + ", " +
                            ASTEROID_DATA_FIELD_MAX_DIAM + ", " +
                            ASTEROID_DATA_FIELD_MIN_DIAM + "," +
                            CREATED_DATETIME + ") " +
                            " values (?,?,?,?,?)");
            statement.setString(1, asteroidData.getId());
            statement.setString(2, asteroidData.getName());
            statement.setDouble(3, asteroidData.getDiamMax());
            statement.setDouble(4, asteroidData.getDiamMin());
            statement.setTimestamp(5, new Timestamp(System.currentTimeMillis()));

            statement.executeUpdate();
        }

    }


    public AsteroidData findById(String asteroid_id) throws SQLException {
        AsteroidData asteroidData = new AsteroidData();
        try (Connection connection = DBCPDataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("select * from " + ASTEROID_DATA_SCHEMA +
                    " where " + ASTEROID_DATA_FIELD_ID + " = ? ");
            statement.setString(1, asteroid_id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                asteroidData.setName(resultSet.getString(ASTEROID_DATA_FIELD_NAME));
                asteroidData.setId(resultSet.getString(ASTEROID_DATA_FIELD_ID));
                asteroidData.setDiamMax(resultSet.getDouble(ASTEROID_DATA_FIELD_MAX_DIAM));
                asteroidData.setDiamMin(resultSet.getDouble(ASTEROID_DATA_FIELD_MIN_DIAM));
                asteroidData.setCreated(DateAndTimeHelper.parseTimestampToLocaleDateTime(resultSet.getString(CREATED_DATETIME)));
            }
        }
        return asteroidData;
    }
}
