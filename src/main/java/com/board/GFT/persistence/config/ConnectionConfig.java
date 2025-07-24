package com.board.GFT.persistence.config;


import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConnectionConfig {

    public static Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/board";
        String user = "board";
        String password = "board";
        var connection = DriverManager.getConnection(url, user, password);
        connection.setAutoCommit(false);
        return  connection;
    }
}
