package app;

import java.util.*;
import java.sql.*;

import app.database.*;
import app.model.Song;

public class Main {

    private static final Scanner SCANNER;
    private static final UserDatabase USER_DATABASE;
    private static final SongDatabase SONG_DATABASE;

    private static List<Song> songs;
    private static String username;
    private static int userChoice;

    static {
        SCANNER = new Scanner(System.in);
        USER_DATABASE = new UserDatabase();
        SONG_DATABASE = new SongDatabase();
        songs = new ArrayList<>();
    }

    public static void main(String... args) {
        new Main().execute();
    }

    private void execute() {

        congratulationMessage();

        printStartMenu();

        System.out.print("Your choice: ");

        enterSomeIntegerValue();

        switch (userChoice) {
            case 1 -> setRegistrationData();
            case 2 -> setLoginData();
            default -> throw new RuntimeException("Incorrect input");
        }

        var play = true;

        while (play) {

            printMainMenu();

            System.out.print("Your choice: ");

            enterSomeIntegerValue();

            switch (userChoice) {

                case 1 -> searchAndAddSong();

                case 2 -> printUserPlaylistData();

                case 3 -> {
                    System.out.println("Your data are saved! Good Luck!");
                    play = false;
                }

                default -> throw new RuntimeException("Incorrect input");
            }
        }
    }

    private void setRegistrationData() {

        while (true) {

            System.out.println("Please, register your account: ");

            System.out.print("Enter your username: ");
            username = SCANNER.nextLine();

            System.out.print("Enter your password: ");
            var firstPasswordAttempt = SCANNER.nextLine();

            System.out.print("Confirm your password: ");
            var secondPasswordAttempt = SCANNER.nextLine();

            try {
                if (secondPasswordAttempt.equals(firstPasswordAttempt)) {
                    addUserToRegistrationTable(username, firstPasswordAttempt);
                    System.out.println("\nCongratulations, your account has been created successfully!\n");
                    break;
                } else {
                    System.out.println("\nSorry, passwords don't match!\n");
                }
            } catch (RuntimeException exception) {
                System.out.println("\nThis username is already exists! Please, choose another one!\n");
            }
        }
    }

    private void addUserToRegistrationTable(String username, String password) {
        try {
            USER_DATABASE.createRegistrationTable();
            USER_DATABASE.addUserToRegistrationTable(username, password);
        } catch (SQLException e) {
            throw new RuntimeException("An unexpected error occurred during the database operations", e);
        }
    }

    private void setLoginData() {

        while (true) {

            System.out.println("Please, log in to your account: ");

            System.out.print("Enter your username: ");
            username = SCANNER.nextLine();

            System.out.print("Enter your password: ");
            var password = SCANNER.nextLine();

            try {
                if (USER_DATABASE.checkUser(username, password)) {
                    songs = SONG_DATABASE.setSongList(USER_DATABASE.restoreUserDataResultSet(username));
                    System.out.println("\nAuthorization completed successfully!\n");
                    break;
                } else {
                    System.out.println("\nIncorrect login or password!\n");
                }
            } catch (SQLException e) {
                throw new RuntimeException("An unexpected error occurred during the database operations", e);
            }
        }
    }

    private void printStartMenu() {
        System.out.println();
        System.out.println("1. Create an account.");
        System.out.println("2. Log into an existing account.");
        System.out.println();
    }

    private void printMainMenu() {
        System.out.println();
        System.out.println("1. Search and add song.");
        System.out.println("2. View my playlist.");
        System.out.println("3. Exit.");
        System.out.println();
    }

    private void printSearchSongsMenu() {
        System.out.println();
        System.out.println("1. Search songs by title.");
        System.out.println("2. Search songs by artist name.");
        System.out.println("3. Search songs by album name.");
        System.out.println("4. Search songs by year of release.");
        System.out.println();
    }

    private void searchAndAddSong() {

        printSearchSongsMenu();

        System.out.print("Your choice: ");

        enterSomeIntegerValue();

        while (true) {

            System.out.print("Please, enter your request: ");
            var userLine = SCANNER.nextLine();

            try {
                songs = SONG_DATABASE.searchSong(userLine, userChoice);
            } catch (SQLException e) {
                throw new RuntimeException("An unexpected error occurred during some database operations", e);
            }

            if (!songs.isEmpty()) {
                printResultData();
                break;
            } else {
                System.out.println("No such song found!");
            }
        }

        addSongToUserPlaylist();
    }

    private void printUserPlaylistData() {

        if (songs.isEmpty()) {
            System.out.println("Your playlist is empty!");
        } else {
            printResultData();
        }
    }

    private void printResultData() {

        var songIterator = songs.iterator();

        System.out.println();
        System.out.printf("%-5s %-25s %-25s %-25s %-25s", "Id:", "Title:", "Artist:", "Album:", "Year:");
        System.out.println();

        while (songIterator.hasNext()) {
            System.out.println(songIterator.next());
        }
    }

    private void addSongToUserPlaylist() {

        System.out.println();

        System.out.print("Please, enter the id of song you want to add: ");

        enterSomeIntegerValue();

        try {
            songs = SONG_DATABASE.setSongList(USER_DATABASE.setListResultSet(userChoice));
        } catch (SQLException e) {
            throw new RuntimeException("An unexpected error occurred during the database operations", e);
        }

        System.out.println("\nThe song has been successfully added to your playlist!");
    }

    private void congratulationMessage() {
        System.out.println("\nWelcome to the awesome playlist! You can easily search and add different songs to your own playlist!");
    }

    private void enterSomeIntegerValue() {
        try {
            userChoice = SCANNER.nextInt();
        } catch (InputMismatchException e) {
            throw new RuntimeException("Incorrect input", e);
        }
    }
}