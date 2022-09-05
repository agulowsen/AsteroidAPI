package com.gulowsen.asteroidData.repository.implementations.mysql;

import com.gulowsen.asteroidData.models.AsteroidData;
import com.gulowsen.asteroidData.repository.interfaces.LargestAsteroidRepository;
import com.gulowsen.asteroidData.utils.DBCPDataSource;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import static com.gulowsen.asteroidData.constants.mysql.DatabaseConstants.*;

@Repository
public class LargestDataRepositoryImpl extends BaseRepositoryImpl implements LargestAsteroidRepository {

    public AsteroidData getLargestAsteroidByYear(int year) throws SQLException {
        AsteroidData asteroidData = new AsteroidData();
        Connection connection = DBCPDataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + LARGEST_ASTEROID_SCHEMA + " where " +
                LARGEST_ASTEROID_YEAR + " = ?"
        );
        statement.setInt(1, year);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            asteroidData.setId(resultSet.getString(LARGEST_ASTEROID_ASTEROID_ID));
        }
        closeDBConnection(connection);
        return asteroidData;
    }

    public void save(AsteroidData currentLargest, int year) throws SQLException {
        Connection connection = DBCPDataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO " + LARGEST_ASTEROID_SCHEMA +
                        " (" + LARGEST_ASTEROID_YEAR + ", " +
                        LARGEST_ASTEROID_ASTEROID_ID + ", " +
                        CREATED_DATETIME +
                         ") " +
                        " values (?,?,?)" +
                        " ON DUPLICATE KEY UPDATE " +
                        LARGEST_ASTEROID_ASTEROID_ID + " = " + "values("+ LARGEST_ASTEROID_ASTEROID_ID +")," +
                        CREATED_DATETIME + " = " + "values(" + CREATED_DATETIME + ")"
        );
        statement.setInt(1, year);
        statement.setString(2, currentLargest.getId());
        statement.setTimestamp(3, new Timestamp(System.currentTimeMillis()));

        statement.executeUpdate();
        closeDBConnection(connection);
    }
}
