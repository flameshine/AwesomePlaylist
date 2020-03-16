package Data;

import java.sql.*;
import java.util.Objects;

public class DataBase {

    public void printDataBaseData() throws SQLException {
        ResultSet result = createResultSet();

        while(result.next()) {
            //System.out.println(result.getInt(1) + ". " + result.getString(3) + ". ");
        }
    }

    private static final String
            DRIVER = "com.mysql.jdbc.Driver",
            URL = "jdbc:mysql://localhost:3306/awesomePlaylist",
            USER = "root",
            PASSWORD = "toortoor";

    private Connection createLinkToDataBase() throws ClassNotFoundException, SQLException {
        Class.forName(DRIVER);
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    private Connection getConnection() {
        try {
            return createLinkToDataBase();
        } catch (ClassNotFoundException | SQLException exception) {
            System.out.println(exception);
        }

        return null;
    }

    private ResultSet createResultSet() throws SQLException {
        Connection connection = getConnection();
        PreparedStatement selectedData = Objects.requireNonNull(connection).prepareStatement(selectAllData());
        return selectedData.executeQuery();
    }

    private String selectAllData() {
        return "SELECT * FROM awesomePlaylist.songs";
    }
}