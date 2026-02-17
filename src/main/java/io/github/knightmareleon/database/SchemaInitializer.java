package io.github.knightmareleon.database;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class SchemaInitializer {
    
    private SchemaInitializer() {}

    public static void initialize() throws SQLException, IOException {
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement()) {

            String sql = loadSqlFile("/io/github/knightmareleon/database/schema.sql");

            for (String statement : sql.split(";")) {
                if (!statement.isBlank()) {
                    stmt.execute(statement);
                }
            }

            System.out.println("Schema initialized.");
        }
    }

    private static String loadSqlFile(String path) throws IOException {
        try (InputStream is = SchemaInitializer.class.getResourceAsStream(path)) {
            if (is == null) {
                throw new IllegalArgumentException("SQL file not found: " + path);
            }
            return new String(is.readAllBytes());
        }
    }
}
