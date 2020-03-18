package Data;

import java.sql.*;
import java.util.*;

public class DataBase {

    private Connection connection = getConnection();
    private List<String> foundSongTitle = new ArrayList<>();
    private List<String> foundSongArtistName = new ArrayList<>();
    private List<String> foundSongAlbumName = new ArrayList<>();
    private List<String> foundSongYear = new ArrayList<>();

    public void findSong(String userLine, Integer userChoice) throws SQLException {
        ResultSet result = createResultSet(selectAllData());

        clearAllListData();

        switch (userChoice) {
            case 1:
                while (result.next()) {
                    if (result.getString(1).contains(userLine))
                        putDataToLists(result.getString(1), result.getString(2), result.getString(3), result.getString(4));
                }
                break;
            case 2:
                while (result.next()) {
                    if (result.getString(2).contains(userLine))
                        putDataToLists(result.getString(1), result.getString(2), result.getString(3), result.getString(4));
                }
                break;
            case 3:
                while (result.next()) {
                    if (result.getString(1).contains(userLine) || result.getString(2).contains(userLine))
                        putDataToLists(result.getString(1), result.getString(2), result.getString(3), result.getString(4));
                }
                break;
            default:
                System.out.println("Incorrect input!");
                break;
        }
    }

    public List<String> getFoundSongTitle() {
        return foundSongTitle;
    }

    public List<String> getFoundSongArtistName() {
        return foundSongArtistName;
    }

    public List<String> getFoundSongAlbumName() {
        return foundSongAlbumName;
    }

    public List<String> getFoundSongYear() {
        return foundSongYear;
    }

    private void clearAllListData() {
        foundSongTitle.clear();
        foundSongArtistName.clear();
        foundSongAlbumName.clear();
        foundSongYear.clear();
    }

    private void putDataToLists(String title, String artistName, String albumName, String year) {
        foundSongTitle.add(title);
        foundSongArtistName.add(artistName);
        foundSongAlbumName.add(albumName);
        foundSongYear.add(year);
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
        return "SELECT title, artistName, albumName, year FROM awesomePlaylist.songs LEFT JOIN awesomePlaylist.artists ON (awesomePlaylist.songs.artistID = awesomePlaylist.artists.ID)";
    }
}