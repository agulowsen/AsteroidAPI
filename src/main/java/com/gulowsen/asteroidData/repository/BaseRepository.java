package com.gulowsen.asteroidData.repository;

import com.gulowsen.asteroidData.utils.DBCPDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class BaseRepository {

    // Schemas
    public static final String ASTEROID_DATA_SCHEMA = "AsteroidData";
    public static final String CLOSE_APPROACH_DATA_SCHEMA = "CloseApproachData";
    public static final String LARGEST_ASTEROID_SCHEMA = "LargestAsteroid";

    // FIELDS
    protected static final String CREATED_DATETIME = "created";
    public static final String ASTEROID_DATA_FIELD_ID = "id";
    public static final String ASTEROID_DATA_FIELD_NAME = "name";
    public static final String ASTEROID_DATA_FIELD_MAX_DIAM = "diam_max";
    public static final String ASTEROID_DATA_FIELD_MIN_DIAM = "diam_min";
    public static final String CLOSE_APPROACH_DATA_FIELD_ID = "id";
    public static final String CLOSE_APPROACH_DATA_FIELD_ASTEROID_ID = "asteroid_id";
    public static final String CLOSE_APPROACH_DATA_FIELD_DATE = "date";
    public static final String CLOSE_APPROACH_DATA_FIELD_MISS_DISTANCE = "miss_distance";
    public static final String LARGEST_ASTEROID_YEAR = "year";
    public static final String LARGEST_ASTEROID_ASTEROID_ID = "asteroid_id";


    protected void closeDBConnection(Connection connection) {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public int queryCountOfElement(String schema, String clauseField, String stringMatch, Integer integerMatch) throws SQLException {
        Integer count = null;
        Connection connection = DBCPDataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM " + schema + " where " +
                clauseField + " = ?"
        );
        if(integerMatch != null) {
            statement.setInt(1, integerMatch);
        } else {
            statement.setString(1, stringMatch);
        }
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            count = resultSet.getInt(1);
        }
        closeDBConnection(connection);
        return count;
    }

}
