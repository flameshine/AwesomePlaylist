package main.Database;

import java.sql.*;
import java.util.Objects;

public class ProjectConnectionPool {

    private static ProjectConnectionPool instance = null;
    private static Connection connection = null;

    private static final String
            DRIVER = "com.mysql.jdbc.Driver",
            URL = "jdbc:mysql://localhost:3306/awesomePlaylist",
            USER = "root",
            PASSWORD = "toortoor";

    public static synchronized ProjectConnectionPool getInstance() {
        if(instance == null)
            instance = new ProjectConnectionPool();
        return instance;
    }

    public Connection getConnection() {

        if(connection == null) {
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

    private ProjectConnectionPool() {

    }

    private static Connection createConnection() throws ClassNotFoundException, SQLException {
        Class.forName(DRIVER);
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}