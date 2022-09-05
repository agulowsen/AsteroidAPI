package com.gulowsen.asteroidData.repository.interfaces;

import java.sql.Connection;
import java.sql.SQLException;

public interface BaseRepository {

    int queryCountOfElement(String schema, String clauseField, String stringMatch, Integer integerMatch) throws SQLException;
    default void closeDBConnection(Connection connection) {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
