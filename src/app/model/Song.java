package app.model;

public class Song {

    private final long id;

    private final String title;
    private final String artistName;
    private final String albumName;
    private final String year;

    public Song(long id, String title, String artistName, String albumName, String year) {
        this.id = id;
        this.title = title;
        this.artistName = artistName;
        this.albumName = albumName;
        this.year = year;
    }

    public String getTitle() {
        return title;
    }

    public String getArtistName() {
        return artistName;
    }

    public String getAlbumName() {
        return albumName;
    }

    public String getYear() {
        return year;
    }

    public void printSong() {
        System.out.printf("%-5s %-25s %-25s %-25s %-25s", id, title, artistName, albumName, year);
        System.out.println();
    }
}