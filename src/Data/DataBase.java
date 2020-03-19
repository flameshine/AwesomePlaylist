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

    public void addDataToPlaylist(Integer userChoice) throws SQLException {
        --userChoice;
        Integer counter = 1;

        createStatement().executeUpdate(createPlaylist());
        createStatement().executeUpdate(addSongToUserPlaylist(counter, foundSongTitle.get(userChoice), foundSongArtistName.get(userChoice), foundSongAlbumName.get(userChoice), foundSongYear.get(userChoice)));
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

    private Statement createStatement() throws SQLException {
        return connection.createStatement();
    }

    private String selectAllData() {
        return "SELECT title, artistName, albumName, year FROM awesomePlaylist.songs LEFT JOIN awesomePlaylist.artists ON (awesomePlaylist.songs.artistID = awesomePlaylist.artists.ID)";
    }

    private String createPlaylist() {
        return "CREATE TABLE IF NOT EXISTS `awesomePlaylist`.`userPlaylist` (`ID` INT NOT NULL, `title` VARCHAR(50) NOT NULL, `artistName` VARCHAR(50) NOT NULL, `albumName` VARCHAR(50) NOT NULL, `year` VARCHAR(50) NOT NULL, PRIMARY KEY (`ID`))";
    }

    private String addSongToUserPlaylist(Integer id, String title, String artistName, String albumName, String year) {
        return "INSERT INTO awesomePlaylist.userPlaylist VALUES (" + id + ", '" + title + "', '" + artistName + "', '" + albumName + "', " + year + ")";
    }
}