package Data;

import java.sql.*;
import java.util.Objects;

public class DataBase {

    Connection connection = getConnection();

    public void printBaseData(String source) throws SQLException {
        ResultSet result = createResultSet(source);

        while(result.next()) {
            System.out.println(result.getInt(1) + ". " + result.getString(2) + ". ");
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

    private ResultSet createResultSet(String source) throws SQLException {
        PreparedStatement selectedData = Objects.requireNonNull(connection).prepareStatement(source);
        return selectedData.executeQuery();
    }

    public String selectAllArtistsData() {
        return "SELECT * FROM awesomePlaylist.artists";
    }

    public String selectAllSongsData() {
        return "SELECT * FROM awesomePlaylist.songs";
    }
}