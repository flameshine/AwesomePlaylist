package main.Database;

import org.jetbrains.annotations.NotNull;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;
import main.DataClasses.Song;

public class SongDatabase {

    private static List<Song> songs = new ArrayList<>();
    private UserDatabase userDatabase = new UserDatabase();

    private final int TITLE = 1;
    private final int ARTIST = 2;
    private final int ALBUM = 3;
    private final int YEAR = 4;

    public List<Song> restoreUserData(String username) throws SQLException {
        ResultSet resultSet = userDatabase.restoreUserDataResultSet(username);

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

    protected List<Song> getSongs() {
        return songs;
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
    private String selectAllDataSQL() {
        return "SELECT awesomePlaylist.songs.ID, title, artistName, albumName, year FROM awesomePlaylist.songs LEFT JOIN awesomePlaylist.artists ON (awesomePlaylist.songs.artistID = awesomePlaylist.artists.ID)";
    }
}