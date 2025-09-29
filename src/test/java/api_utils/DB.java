package api_utils;

import java.sql.*;

public class DB {

    // private static final String DB_URL = "jdbc:h2:mem:testdb"; // change if
    // file/server mode
    private static final String DB_URL = "jdbc:h2:file:../MobileBank/mydb;AUTO_SERVER=TRUE";
    private static final String DB_USER = "sa";
    private static final String DB_PASS = "password";

    // Get DB connection
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
    }

    // Run SELECT query
    public static ResultSet executeQuery(String query, Object... params) throws SQLException {
        Connection conn = getConnection();
        PreparedStatement stmt = conn.prepareStatement(query);

        // set params dynamically
        for (int i = 0; i < params.length; i++) {
            stmt.setObject(i + 1, params[i]);
        }

        return stmt.executeQuery();
    }

    // Run INSERT/UPDATE/DELETE query
    public static int executeUpdate(String query, Object... params) throws SQLException {
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }

            return stmt.executeUpdate(); // returns number of rows affected
        }
    }

    public static boolean compareResultSets(ResultSet rs1, ResultSet rs2) throws SQLException {
        while (rs1.next()) {
            rs2.next();
            int count = rs1.getMetaData().getColumnCount();
            for (int i = 1; i <= count; i++) {
                if (!java.util.Objects.equals(rs1.getString(i), rs2.getString(i))) {
                    return false;
                }
            }
        }
        return true;
    }
}
