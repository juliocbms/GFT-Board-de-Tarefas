package com.board.GFT.ui;

import com.board.GFT.persistence.migration.MigrationStrategy;

import java.sql.SQLException;

import static com.board.GFT.persistence.config.ConnectionConfig.getConnection;


public class Main {
    public static void main(String[] args) throws SQLException {
        try( var connection = getConnection()){
            new MigrationStrategy(connection).executeMigration();
        }
    }
}
