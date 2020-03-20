package Data;

import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.*;

public class DataBase {

    private Connection connection = getConnection();

    private List<String> songIds = new ArrayList<>();
    private List<String> songTitles = new ArrayList<>();
    private List<String> songArtistNames = new ArrayList<>();
    private List<String> songAlbumNames = new ArrayList<>();
    private List<String> songYears = new ArrayList<>();

    public void findSong(String userLine, Integer userChoice) throws SQLException {
        ResultSet result = createResultSet(selectAllData());

        switch (userChoice) {
            case 1:
                while (result.next()) {
                    if (result.getString(2).contains(userLine))
                        putDataToLists(result.getString(1), result.getString(2), result.getString(3), result.getString(4), result.getString(5));
                }
                break;
            case 2:
                while (result.next()) {
                    if (result.getString(3).contains(userLine))
                        putDataToLists(result.getString(1), result.getString(2), result.getString(3), result.getString(4), result.getString(5));
                }
                break;
            case 3:
                while (result.next()) {
                    if (result.getString(2).contains(userLine) || result.getString(3).contains(userLine))
                        putDataToLists(result.getString(1), result.getString(2), result.getString(3), result.getString(4), result.getString(5));
                }
                break;
            default:
                System.out.println("Incorrect input!");
                break;
        }
    }

    public void addSongToPlaylist(Integer userChoice) throws SQLException {
        createStatement().executeUpdate(createPlaylist());
        createStatement().executeUpdate(addSongToUserPlaylist(userChoice));
        putUserPlaylistDataToLists();
    }

    private void putUserPlaylistDataToLists() throws SQLException {
        ResultSet result = createResultSet(selectDataFromUserPlaylist());

        while(result.next()) {
            putDataToLists(result.getString(1), result.getString(2), result.getString(3), result.getString(4), result.getString(5));
        }
    }

    public List<String> getSongIds() {
        return songIds;
    }

    public List<String> getSongTitles() {
        return songTitles;
    }

    public List<String> getSongArtistNames() {
        return songArtistNames;
    }

    public List<String> getSongAlbumNames() {
        return songAlbumNames;
    }

    public List<String> getSongYears() {
        return songYears;
    }

    public void clearAllListData() {
        songIds.clear();
        songTitles.clear();
        songArtistNames.clear();
        songAlbumNames.clear();
        songYears.clear();
    }

    private void putDataToLists(String id, String title, String artistName, String albumName, String year) {
        songIds.add(id);
        songTitles.add(title);
        songArtistNames.add(artistName);
        songAlbumNames.add(albumName);
        songYears.add(year);
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

    @NotNull
    private String selectAllData() {
        return "SELECT awesomePlaylist.songs.ID, title, artistName, albumName, year FROM awesomePlaylist.songs LEFT JOIN awesomePlaylist.artists ON (awesomePlaylist.songs.artistID = awesomePlaylist.artists.ID)";
    }

    @NotNull
    private String createPlaylist() {
        return "CREATE TABLE IF NOT EXISTS `awesomePlaylist`.`userPlaylist` (`ID` INT NOT NULL AUTO_INCREMENT, `songID` INT NOT NULL, INDEX `songID_idx` (`songID` ASC) VISIBLE, INDEX `ID` (`ID` ASC) VISIBLE, CONSTRAINT `songID` FOREIGN KEY (`songID`) REFERENCES `awesomePlaylist`.`songs` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE)";
    }

    @NotNull
    private String addSongToUserPlaylist(Integer userChoice) {
        return "INSERT INTO awesomePlaylist.userPlaylist VALUES (NULL, " + userChoice + ")";
    }

    @NotNull
    private String selectDataFromUserPlaylist() {
        return "SELECT awesomePlaylist.userPlaylist.ID, title, artistName, albumName, year FROM awesomePlaylist.userPlaylist LEFT JOIN awesomePlaylist.songs ON (awesomePlaylist.userPlaylist.songID = awesomePlaylist.songs.ID) LEFT JOIN awesomePlaylist.artists ON (awesomePlaylist.songs.artistID = awesomePlaylist.artists.ID)";
    }
}