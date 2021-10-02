package app.model;

public record Song(long id, String title, String artistName, String albumName, String year) {

    @Override
    public String toString() {
        return String.format("%-5s %-25s %-25s %-25s %-25s", id, title, artistName, albumName, year);
    }
}