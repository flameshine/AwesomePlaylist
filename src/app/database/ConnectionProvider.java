package app.database;

import java.util.Objects;
import java.sql.*;

public class ConnectionProvider {

    private static final String DRIVER = "com.mysql.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost:3306/awesomePlaylist";
    private static final String USER = "root";
    private static final String PASSWORD = "password";

    private static ConnectionProvider instance;
    private static Connection connection;

    private ConnectionProvider() {}

    public static synchronized ConnectionProvider getInstance() {

        if (instance == null) {
            instance = new ConnectionProvider();
        }

        return instance;
    }

    public synchronized Connection getConnection() {

        if (connection == null) {
            try {
                connection =  createConnection();
            } catch (ClassNotFoundException | SQLException e) {
                throw new RuntimeException("Cannot get database connection via JDBC", e);
            }
        }

        return connection;
    }

    public ResultSet createResultSet(String source) throws SQLException {
        return Objects.requireNonNull(getConnection()).prepareStatement(source).executeQuery();
    }

    private static Connection createConnection() throws ClassNotFoundException, SQLException {
        Class.forName(DRIVER);
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}