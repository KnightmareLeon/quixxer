package io.github.knightmareleon.shared.infrastructure.database;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    
    private static final String APP_NAME = "quixxer";
    private static Connection connection;

    private DatabaseManager() {}

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            Path dir = Paths.get(System.getProperty("user.home"),
                    "." + APP_NAME);
            try {
                Files.createDirectories(dir);
            } catch (IOException ignored) {}

            String url = "jdbc:sqlite:" + dir.resolve("app.db");
            connection = DriverManager.getConnection(url);

            try (Statement stmt = connection.createStatement()) {
                stmt.execute("PRAGMA foreign_keys = ON;");
            }
            System.out.println("Connected to database successfully!");
        }
        return connection;
    }
}
