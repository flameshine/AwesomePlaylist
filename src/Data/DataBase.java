package Data;

import java.sql.*;
import java.util.*;

public class DataBase {

    private Connection connection = getConnection();
    private Map<String, String> foundSounds = new HashMap<>();

    public void findSong(String userLine, Integer userChoice) throws SQLException {
        ResultSet result = createResultSet(selectAllData());

        foundSounds.clear();

        switch (userChoice) {
            case 1:
                while (result.next()) {
                    if (result.getString(1).contains(userLine))
                        foundSounds.put(result.getString(1), result.getString(2));
                }
                break;
            case 2:
                while (result.next()) {
                    if (result.getString(2).contains(userLine))
                        foundSounds.put(result.getString(1), result.getString(2));
                }
                break;
            case 3:
                while (result.next()) {
                    if (result.getString(1).contains(userLine) || result.getString(2).contains(userLine))
                        foundSounds.put(result.getString(1), result.getString(2));
                }
                break;
            default:
                System.out.println("Incorrect input!");
                break;
        }
    }

    public Map<String, String> getFoundSounds() {
        return foundSounds;
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

    private String selectAllData() {
        return "SELECT name, artistName, album, year FROM awesomePlaylist.songs LEFT JOIN awesomePlaylist.artists ON (awesomePlaylist.songs.artistID = awesomePlaylist.artists.ID)";
    }
}