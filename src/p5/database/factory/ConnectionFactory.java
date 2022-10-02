package p5.database.factory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
    private static Connection conn;

    public static Connection startConnection() {
        String url = "jdbc:postgresql://localhost/ovchip?user=postgres&password=123";
        try {
            conn =  DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e);
        }
        return conn;
    }

    public static void closeConnection() throws SQLException {
        conn.close();
    }

    public static Connection getConnection() {
        return conn;
    }
}
