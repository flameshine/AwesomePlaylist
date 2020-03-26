package main.Database;

import org.jetbrains.annotations.NotNull;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;
import main.DataClasses.Song;

public class SongDatabase {

    private List<Song> songs = new ArrayList<>();
    private static int userId = 0;

    private final int TITLE = 1;
    private final int ARTIST = 2;
    private final int ALBUM = 3;
    private final int YEAR = 4;

    public void createRegistrationTable() throws SQLException {
        createStatement().executeUpdate(createRegistrationTableSQL());
    }

    public void addUserToRegistrationTable(String username, String password) throws SQLException {
        ResultSet resultSet = ProjectConnectionPool.getInstance().createResultSet(selectDataFromRegistrationTable());

        while(resultSet.next()) {
            if(resultSet.getString(2).equals(username))
                throw new RuntimeException();
        }

        createStatement().executeUpdate(addUserToRegistrationTableSQL(username, password));
        createUserPlaylist(username);
    }

    public void createUserPlaylist(String username) throws SQLException {
        setUser(username);
        createStatement().executeUpdate(createUserPlaylistSQL());
    }

    public boolean checkUser(String username, String password) throws SQLException {
        createRegistrationTable();
        ResultSet resultSet = ProjectConnectionPool.getInstance().createResultSet(checkUserSQL(username, password));
        resultSet.next();
        return resultSet.getBoolean(1);
    }

    public List<Song> restoreUserData(String username) throws SQLException {
        setUser(username);
        ResultSet resultSet = ProjectConnectionPool.getInstance().createResultSet(selectDataFromUserPlaylistSQL());

        songs.clear();
        songs = setSongList(resultSet);

        return songs;
    }

    public List<Song> searchSong(String userLine, @NotNull Integer userChoice) throws SQLException {
        ResultSet resultSet = ProjectConnectionPool.getInstance().createResultSet(selectAllDataSQL());

        songs.clear();
        songs = setSongList(resultSet);

        switch(userChoice) {
            case TITLE:
                return songs.stream().filter(s -> s.getTitle().contains(userLine)).collect(Collectors.toList());
            case ARTIST:
                return songs.stream().filter(s -> s.getArtistName().contains(userLine)).collect(Collectors.toList());
            case ALBUM:
                return songs.stream().filter(s -> s.getAlbumName().contains(userLine)).collect(Collectors.toList());
            case YEAR:
                return songs.stream().filter(s -> s.getYear().contains(userLine)).collect(Collectors.toList());
            default:
                throw new RuntimeException();
        }
    }

    public List<Song> addSongToPlaylist(Integer userChoice) throws SQLException {
        createStatement().executeUpdate(addSongToUserPlaylistSQL(userChoice));
        putResultToList();
        return songs;
    }

    private void setUser(String username) throws SQLException {
        ResultSet resultSet = ProjectConnectionPool.getInstance().createResultSet(selectDataFromRegistrationTable());

        while(resultSet.next()) {
            if(resultSet.getString(2).equals(username))
                userId = resultSet.getInt(1);
        }
    }

    private void putResultToList() throws SQLException {
        ResultSet resultSet = ProjectConnectionPool.getInstance().createResultSet(selectDataFromUserPlaylistSQL());

        songs.clear();

        while(resultSet.next()) {
            Song song = new Song(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4), resultSet.getString(5));
            songs.add(song);
        }
    }

    private Statement createStatement() throws SQLException {
        return ProjectConnectionPool.getInstance().getConnection().createStatement();
    }

    @NotNull
    private List<Song> setSongList(@NotNull ResultSet result) throws SQLException {

        List<Song> songs = new ArrayList<>();

        while(result.next()) {
            Song song = new Song(result.getInt(1), result.getString(2), result.getString(3), result.getString(4), result.getString(5));
            songs.add(song);
        }

        return songs;
    }

    @NotNull
    private String createRegistrationTableSQL() {
        return "CREATE TABLE IF NOT EXISTS `awesomePlaylist`.`registrationTable` (`usernameID` INT NOT NULL AUTO_INCREMENT, `username` VARCHAR(50) NOT NULL, `password` VARCHAR(50) NOT NULL, PRIMARY KEY (`usernameID`), UNIQUE INDEX `userID_UNIQUE` (`usernameID` ASC) VISIBLE)";
    }

    @NotNull
    private String addUserToRegistrationTableSQL(String username, String password) {
        return "INSERT INTO awesomePlaylist.registrationTable (usernameID, username, password) SELECT NULL, '"  + username + "', '" + password + "' FROM DUAL WHERE NOT EXISTS (SELECT * FROM awesomePlaylist.registrationTable WHERE usernameID = NULL AND username = '" + username + "' AND password = '" + password + "')";
    }

    @NotNull
    private String checkUserSQL(String username, String password) {
        return "SELECT EXISTS (SELECT * FROM awesomePlaylist.registrationTable WHERE username = '" + username + "' AND password = '" + password + "') LIMIT 1";
    }

    @NotNull
    private String createUserPlaylistSQL() {
        return "CREATE TABLE IF NOT EXISTS `awesomePlaylist`.`" + userId + "` (`ID` INT NOT NULL AUTO_INCREMENT, `" + userId + "SongID` INT NOT NULL, INDEX `songID_idx` (`" + userId + "SongID` ASC) VISIBLE, INDEX `ID` (`ID` ASC) VISIBLE, CONSTRAINT `" + userId + "SongID` FOREIGN KEY (`" + userId + "SongID`) REFERENCES `awesomePlaylist`.`songs` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE)";
    }

    @NotNull
    private String addSongToUserPlaylistSQL(Integer userChoice) {
        return "INSERT INTO awesomePlaylist." + userId + " VALUES (NULL, " + userChoice + ")";
    }

    @NotNull
    private String selectDataFromUserPlaylistSQL() {
        return "SELECT awesomePlaylist." + userId + ".ID, title, artistName, albumName, year FROM awesomePlaylist." + userId + " LEFT JOIN awesomePlaylist.songs ON (awesomePlaylist." + userId + "." + userId + "songID = awesomePlaylist.songs.ID) LEFT JOIN awesomePlaylist.artists ON (awesomePlaylist.songs.artistID = awesomePlaylist.artists.ID)";
    }

    @NotNull
    private String selectDataFromRegistrationTable() {
        return "SELECT * FROM awesomePlaylist.registrationTable";
    }

    @NotNull
    private String selectAllDataSQL() {
        return "SELECT awesomePlaylist.songs.ID, title, artistName, albumName, year FROM awesomePlaylist.songs LEFT JOIN awesomePlaylist.artists ON (awesomePlaylist.songs.artistID = awesomePlaylist.artists.ID)";
    }
}