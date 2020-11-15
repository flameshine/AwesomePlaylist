package app;

import java.util.*;
import java.sql.*;

import app.database.*;
import app.model.Song;

public class Main {

    private static final Scanner in = new Scanner(System.in);

    private static List<Song> songs = new ArrayList<>();

    private static final UserDatabase userDatabase = new UserDatabase();
    private static final SongDatabase songDatabase = new SongDatabase();

    private static String username;
    private static String password;

    private static int userChoice;

    public static void main(String[] args) {
        Main main = new Main();
        main.printMainData();
    }

    private void printMainData() {

        congratulationMessage();

        printStartMenu();

        System.out.print("Your choice: ");

        enterSomeIntegerValue();

        switch (userChoice) {

            case 1:
                setRegistrationData();
                break;
            case 2:
                setLoginData();
                break;
            default:
                System.out.println("Incorrect input!");
                break;
        }

        boolean play = true;

        while (play) {

            printMainMenu();

            System.out.print("Your choice: ");

            enterSomeIntegerValue();

            switch (userChoice) {

                case 1:
                    searchAndAddSong();
                    break;
                case 2:
                    printUserPlaylistData();
                    break;
                case 3:
                    System.out.println("Your data are saved! Good Luck!");
                    play = false;
                    break;
                default:
                    System.out.println("Incorrect input!");
                    break;
            }
        }
    }

    private void setRegistrationData() {

        while (true) {

            System.out.println("Please, register your account: ");

            System.out.print("Enter your username: ");
            username = in.nextLine();
            System.out.print("Enter your password: ");
            String firstPasswordAttempt = in.nextLine();
            System.out.print("Confirm your password: ");
            String secondPasswordAttempt = in.nextLine();

            try {
                if (secondPasswordAttempt.equals(firstPasswordAttempt)) {
                    addUserToRegistrationTable(username, firstPasswordAttempt);
                    System.out.println("\nCongratulations, your account has been created successfully!\n");
                    break;
                } else
                    System.out.println("\nSorry, passwords don't match!\n");
            } catch (RuntimeException exception) {
                System.out.println("\nThis username is already exists! Please, choose another one!\n");
            }
        }
    }

    private void addUserToRegistrationTable(String username, String password) {
        try {
            userDatabase.createRegistrationTable();
            userDatabase.addUserToRegistrationTable(username, password);
        } catch (SQLException exception) {
            exception.printStackTrace(System.out);
        }
    }

    private void setLoginData() {

        while (true) {

            System.out.println("Please, log in to your account: ");

            System.out.print("Enter your username: ");
            username = in.nextLine();
            System.out.print("Enter your password: ");
            password = in.nextLine();

            try {
                if(userDatabase.checkUser(username, password)) {
                    songs = songDatabase.setSongList(userDatabase.restoreUserDataResultSet(username));
                    System.out.println("\nAuthorization completed successfully!\n");
                    break;
                }
                else
                    System.out.println("\nIncorrect login or password!\n");
            } catch (SQLException exception) {
                exception.printStackTrace(System.out);
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
            String userLine = in.nextLine();

            try {
                songs = songDatabase.searchSong(userLine, userChoice);
            } catch (SQLException exception) {
                exception.printStackTrace(System.out);
            } catch (RuntimeException exception) {
                System.out.println("Incorrect input!");
            }

            if (!songs.isEmpty()) {
                printResultData();
                break;
            } else
                System.out.println("No such song found!");
        }

        addSongToUserPlaylist();
    }

    private void printUserPlaylistData() {

        if (songs.isEmpty())
            System.out.println("Your playlist is empty!");
        else
            printResultData();
    }

    private void printResultData() {

        Iterator<Song> songIterator = songs.iterator();

        System.out.println();
        System.out.printf("%-5s %-25s %-25s %-25s %-25s", "Id:", "Title:", "Artist:", "Album:", "Year:");
        System.out.println();

        while (songIterator.hasNext())
            songIterator.next().printSong();
    }

    private void addSongToUserPlaylist() {

        System.out.println();

        System.out.print("Please, enter the id of song you want to add: ");

        enterSomeIntegerValue();

        try {
            songs = songDatabase.setSongList(userDatabase.setListResultSet(userChoice));
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        System.out.println("\nThe song has been successfully added to your playlist!");
    }

    private void congratulationMessage() {
        System.out.println("\nWelcome to the awesome playlist! You can easily search and add different songs to your own playlist!");
    }

    private void enterSomeIntegerValue() {
        try {
            userChoice = in.nextInt();
        } catch (InputMismatchException exception) {
            System.out.println("Mismatch! Restart the program!");
        }
    }
}