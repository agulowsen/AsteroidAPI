package com.gulowsen.asteroidData.repository.implementations.mysql;

import com.gulowsen.asteroidData.models.AsteroidData;
import com.gulowsen.asteroidData.repository.interfaces.AsteroidDataRepository;
import com.gulowsen.asteroidData.repository.BaseRepository;
import com.gulowsen.asteroidData.utils.DBCPDataSource;
import com.gulowsen.asteroidData.utils.DateAndTimeHelper;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

@Repository
public class AsteroidDataRepositoryImpl extends BaseRepository implements AsteroidDataRepository {

    public void save(AsteroidData asteroidData) throws SQLException {
        Connection connection = DBCPDataSource.getConnection();
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
        closeDBConnection(connection);
    }


    public AsteroidData findById(String asteroid_id) throws SQLException {
        Connection connection = DBCPDataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement("select * from " + ASTEROID_DATA_SCHEMA +
                " where " + ASTEROID_DATA_FIELD_ID + " = ? ");
        statement.setString(1, asteroid_id);
        ResultSet resultSet = statement.executeQuery();
        AsteroidData asteroidData = new AsteroidData();
        while (resultSet.next()) {
            asteroidData.setName(resultSet.getString(ASTEROID_DATA_FIELD_NAME));
            asteroidData.setId(resultSet.getString(ASTEROID_DATA_FIELD_ID));
            asteroidData.setDiamMax(resultSet.getDouble(ASTEROID_DATA_FIELD_MAX_DIAM));
            asteroidData.setDiamMin(resultSet.getDouble(ASTEROID_DATA_FIELD_MIN_DIAM));
            asteroidData.setCreated(DateAndTimeHelper.parseTimestampToLocaleDateTime(resultSet.getString(CREATED_DATETIME)));
        }
        closeDBConnection(connection);
        return asteroidData;
    }
}
