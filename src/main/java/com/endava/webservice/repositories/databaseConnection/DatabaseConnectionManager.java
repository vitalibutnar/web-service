package com.endava.webservice.repositories.databaseConnection;

import com.endava.webservice.exeption.DataBaseConnectionException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Component
public class DatabaseConnectionManager {
    @Value("${spring.datasource.driver-class-name}")
    private String driver;
    @Value("${spring.datasource.url}")
    private String url;
    @Value("${spring.datasource.username}")
    private String username;
    @Value("${spring.datasource.password}")
    private String password;
    private Connection connection;

    private Connection getConnection() {
        if (connection == null)
            try {
                Class.forName(driver);
                connection = DriverManager.getConnection(url, username, password);
            } catch (SQLException | ClassNotFoundException exception) {
                throw new DataBaseConnectionException(String.format("Could not connect to DataBase by {%s} reason", exception.getMessage()));
            }
        return connection;
    }

    public PreparedStatement getPreparedStatement(String query) throws SQLException {
        return getConnection().prepareStatement(query);
    }

    public PreparedStatement getPreparedStatement(String query, String[] columnNames) throws SQLException {
        return getConnection().prepareStatement(query, columnNames);
    }
}
