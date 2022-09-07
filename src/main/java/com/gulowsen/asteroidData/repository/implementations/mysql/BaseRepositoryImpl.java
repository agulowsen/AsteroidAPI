package com.gulowsen.asteroidData.repository.implementations.mysql;

import com.gulowsen.asteroidData.repository.interfaces.BaseRepository;
import com.gulowsen.asteroidData.utils.DBCPDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class BaseRepositoryImpl implements BaseRepository {

    public int queryCountOfElement(String schema, String clauseField, String stringMatch, Integer integerMatch) throws SQLException {
        Integer count = null;
        try(Connection connection = DBCPDataSource.getConnection()) {
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
        }
        return count;
    }

}
