package app.database;

import java.sql.*;

public class UserDatabase {

    private static int userId = 0;

    public void createRegistrationTable() throws SQLException {
        createStatement().executeUpdate(createRegistrationTableSQL());
    }

    public void addUserToRegistrationTable(String username, String password) throws SQLException {

        var resultSet = ConnectionProvider.getInstance().createResultSet(selectDataFromRegistrationTable());

        while (resultSet.next()) {

            if (resultSet.getString(2).equals(username)) {
                throw new RuntimeException();
            }
        }

        createStatement().executeUpdate(addUserToRegistrationTableSQL(username, password));

        createUserPlaylist(username);
    }

    public boolean checkUser(String username, String password) throws SQLException {
        createRegistrationTable();
        var resultSet = ConnectionProvider.getInstance().createResultSet(checkUserSQL(username, password));
        resultSet.next();
        return resultSet.getBoolean(1);
    }

    public ResultSet restoreUserDataResultSet(String username) throws SQLException {
        setUser(username);
        return ConnectionProvider.getInstance().createResultSet(selectDataFromUserPlaylistSQL());
    }

    public void createUserPlaylist(String username) throws SQLException {
        setUser(username);
        createStatement().executeUpdate(createUserPlaylistSQL());
    }

    public ResultSet setListResultSet(int userChoice) throws SQLException{
        createStatement().executeUpdate(addSongToUserPlaylistSQL(userChoice));
        return ConnectionProvider.getInstance().createResultSet(selectDataFromUserPlaylistSQL());
    }

    private void setUser(String username) throws SQLException {

        var resultSet = ConnectionProvider.getInstance().createResultSet(selectDataFromRegistrationTable());

        while (resultSet.next()) {

            if (resultSet.getString(2).equals(username)) {
                userId = resultSet.getInt(1);
            }
        }
    }

    private Statement createStatement() throws SQLException {
        return ConnectionProvider.getInstance().getConnection().createStatement();
    }

    private String createRegistrationTableSQL() {
        return "CREATE TABLE IF NOT EXISTS `awesomePlaylist`.`registrationTable` (`usernameID` INT NOT NULL AUTO_INCREMENT, `username` VARCHAR(50) NOT NULL, `password` VARCHAR(50) NOT NULL, PRIMARY KEY (`usernameID`), UNIQUE INDEX `userID_UNIQUE` (`usernameID` ASC) VISIBLE)";
    }

    private String addUserToRegistrationTableSQL(String username, String password) {
        return String.format("INSERT INTO awesomePlaylist.registrationTable (usernameID, username, password) SELECT NULL, '%s', '%s' FROM DUAL WHERE NOT EXISTS (SELECT * FROM awesomePlaylist.registrationTable WHERE usernameID = NULL AND username = '%s' AND password = '%s')", username, password, username, password);
    }

    private String checkUserSQL(String username, String password) {
        return String.format("SELECT EXISTS (SELECT * FROM awesomePlaylist.registrationTable WHERE username = '%s' AND password = '%s') LIMIT 1", username, password);
    }

    private String createUserPlaylistSQL() {
        return String.format("CREATE TABLE IF NOT EXISTS `awesomePlaylist`.`%d` (`ID` INT NOT NULL AUTO_INCREMENT, `%dSongID` INT NOT NULL, INDEX `songID_idx` (`%dSongID` ASC) VISIBLE, INDEX `ID` (`ID` ASC) VISIBLE, CONSTRAINT `%dSongID` FOREIGN KEY (`%dSongID`) REFERENCES `awesomePlaylist`.`songs` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE)", userId, userId, userId, userId, userId);
    }

    private String addSongToUserPlaylistSQL(Integer userChoice) {
        return String.format("INSERT INTO awesomePlaylist.%d VALUES (NULL, %d)", userId, userChoice);
    }

    private String selectDataFromUserPlaylistSQL() {
        return String.format("SELECT awesomePlaylist.%d.ID, title, artistName, albumName, year FROM awesomePlaylist.%d LEFT JOIN awesomePlaylist.songs ON (awesomePlaylist.%d.%dsongID = awesomePlaylist.songs.ID) LEFT JOIN awesomePlaylist.artists ON (awesomePlaylist.songs.artistID = awesomePlaylist.artists.ID)", userId, userId, userId, userId);
    }

    private String selectDataFromRegistrationTable() {
        return "SELECT * FROM awesomePlaylist.registrationTable";
    }
}