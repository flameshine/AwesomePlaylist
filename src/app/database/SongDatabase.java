package app.database;

import java.util.*;
import java.sql.*;

import app.model.Song;

public class SongDatabase {

    private static final int TITLE = 1;
    private static final int ARTIST = 2;
    private static final int ALBUM = 3;
    private static final int YEAR = 4;

    public List<Song> searchSong(String userLine, Integer userChoice) throws SQLException {

        var songs = setSongList(ConnectionProvider.getInstance().createResultSet(selectAllDataSQL()));

        return switch (userChoice) {
            case TITLE -> songs.stream().filter(s -> s.getTitle().contains(userLine)).toList();
            case ARTIST -> songs.stream().filter(s -> s.getArtistName().contains(userLine)).toList();
            case ALBUM -> songs.stream().filter(s -> s.getAlbumName().contains(userLine)).toList();
            case YEAR -> songs.stream().filter(s -> s.getYear().contains(userLine)).toList();
            default -> throw new RuntimeException("Cannot find song by your criterion");
        };
    }

    public List<Song> setSongList(ResultSet result) throws SQLException {

        List<Song> songs = new ArrayList<>();

        while (result.next()) {
            songs.add(new Song(result.getInt(1), result.getString(2), result.getString(3), result.getString(4), result.getString(5)));
        }

        return songs;
    }

    private String selectAllDataSQL() {
        return "SELECT awesomePlaylist.songs.ID, title, artistName, albumName, year FROM awesomePlaylist.songs LEFT JOIN awesomePlaylist.artists ON (awesomePlaylist.songs.artistID = awesomePlaylist.artists.ID)";
    }
}