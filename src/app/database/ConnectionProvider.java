package app.database;

import java.util.Objects;
import java.sql.*;

public class ConnectionProvider {

    private static ConnectionProvider instance = null;

    private static Connection connection = null;

    private static final String
            DRIVER = "com.mysql.jdbc.Driver",
            URL = "jdbc:mysql://localhost:3306/awesomePlaylist",
            USER = "root",
            PASSWORD = "password";

    public static synchronized ConnectionProvider getInstance() {

        if (instance == null)
            instance = new ConnectionProvider();

        return instance;
    }

    public synchronized Connection getConnection() {

        if (connection == null) {
            try {
                connection =  createConnection();
            } catch (ClassNotFoundException | SQLException exception) {
                System.out.println(exception);
            }
        }

        return connection;
    }

    public ResultSet createResultSet(String source) throws SQLException {
        PreparedStatement selectedData = Objects.requireNonNull(getConnection()).prepareStatement(source);
        return selectedData.executeQuery();
    }

    private ConnectionProvider() {}

    private static Connection createConnection() throws ClassNotFoundException, SQLException {
        Class.forName(DRIVER);
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}