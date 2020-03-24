package main.Database;

import org.jetbrains.annotations.NotNull;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;
import main.DataClasses.Song;

public class SongDatabase {

    private List<Song> songs = new ArrayList<>();

    private final int TITLE = 1;
    private final int ARTIST = 2;
    private final int ALBUM = 3;
    private final int YEAR = 4;

    public void createRegistrationTable() throws SQLException {
        createStatement().executeUpdate(createRegistrationTableSQL());
    }

    public void addUserToRegistrationTable(String username, String password) throws SQLException {
        ResultSet checkResultSet = ProjectConnectionPool.getInstance().createResultSet(selectDataFromRegistrationTable());

        while(checkResultSet.next()) {
            if(checkResultSet.getString(2).equals(username))
                throw new RuntimeException();
        }

        createStatement().executeUpdate(addUserToRegistrationTableSQL(username, password));
        createStatement().executeUpdate(createUserPlaylistSQL(username));
    }

    public boolean checkUser(String username, String password) throws SQLException {
        createRegistrationTable();
        ResultSet resultSet = ProjectConnectionPool.getInstance().createResultSet(checkUserSQL(username, password));
        resultSet.next();
        return resultSet.getBoolean(1);
    }

    public List<Song> restoreUserData(String username) throws SQLException {
        ResultSet resultSet = ProjectConnectionPool.getInstance().createResultSet(selectDataFromUserPlaylistSQL(username));

        songs.clear();
        songs = setSongList(resultSet);

        return songs;
    }

    public List<Song> searchSong(String userLine, Integer userChoice) throws SQLException {
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

    public List<Song> addSongToPlaylist(String username, Integer userChoice) throws SQLException {
        createStatement().executeUpdate(addSongToUserPlaylistSQL(username, userChoice));
        putResultToList(username);
        return songs;
    }

    private void putResultToList(String username) throws SQLException {
        ResultSet resultSet = ProjectConnectionPool.getInstance().createResultSet(selectDataFromUserPlaylistSQL(username));

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
    private List<Song> setSongList(ResultSet result) throws SQLException {

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
    private String createUserPlaylistSQL(String username) {
        return "CREATE TABLE IF NOT EXISTS `awesomePlaylist`.`" + username + "` (`ID` INT NOT NULL AUTO_INCREMENT, `" + username + "SongID` INT NOT NULL, INDEX `songID_idx` (`" + username + "SongID` ASC) VISIBLE, INDEX `ID` (`ID` ASC) VISIBLE, CONSTRAINT `" + username + "SongID` FOREIGN KEY (`" + username + "SongID`) REFERENCES `awesomePlaylist`.`songs` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE)";
    }

    @NotNull
    private String addSongToUserPlaylistSQL(String username, Integer userChoice) {
        return "INSERT INTO awesomePlaylist." + username + " VALUES (NULL, " + userChoice + ")";
    }

    @NotNull
    private String selectDataFromUserPlaylistSQL(String username) {
        return "SELECT awesomePlaylist." + username + ".ID, title, artistName, albumName, year FROM awesomePlaylist." + username + " LEFT JOIN awesomePlaylist.songs ON (awesomePlaylist." + username + "." + username + "songID = awesomePlaylist.songs.ID) LEFT JOIN awesomePlaylist.artists ON (awesomePlaylist.songs.artistID = awesomePlaylist.artists.ID)";
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