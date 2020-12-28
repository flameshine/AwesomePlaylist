package app.database;

import java.util.*;
import java.util.stream.Collectors;
import java.sql.*;

import app.model.Song;

public class SongDatabase {

    private static final int TITLE = 1;
    private static final int ARTIST = 2;
    private static final int ALBUM = 3;
    private static final int YEAR = 4;

    public List<Song> searchSong(String userLine, Integer userChoice) throws SQLException {

        var resultSet = ConnectionProvider.getInstance().createResultSet(selectAllDataSQL());

        var songs = setSongList(resultSet);

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

    public List<Song> setSongList(ResultSet result) throws SQLException {

        var songs = new ArrayList<Song>();

        while (result.next())
            songs.add(new Song(result.getInt(1), result.getString(2), result.getString(3), result.getString(4), result.getString(5)));

        return songs;
    }

    private String selectAllDataSQL() {
        return "SELECT awesomePlaylist.songs.ID, title, artistName, albumName, year FROM awesomePlaylist.songs LEFT JOIN awesomePlaylist.artists ON (awesomePlaylist.songs.artistID = awesomePlaylist.artists.ID)";
    }
}