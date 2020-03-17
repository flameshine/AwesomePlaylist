import java.util.*;
import java.sql.*;
import Data.DataBase;

public class Main {

    private final DataBase database = new DataBase();
    private static Scanner input = new Scanner(System.in);
    private static int userChoice;

    public static void main(String[] args) {
        Main main = new Main();
        main.printMainData();
    }

    private void printMainData() {

        congratulationMessage();

        boolean play = true;

        while(play) {

            printMainMenu();
            setUserChoice();

            switch(userChoice) {
                case 1:
                    printSearchSongMenu();
                    break;
                case 2:
                    break;
                case 3:
                    System.out.println("Good Luck! Your data are saved!");
                    play = false;
                    break;
                default:
                    System.out.println("Incorrect input! Please, restart the program!");
                    break;
            }
        }
    }

    private void printMainMenu() {
        System.out.println("1. Search and add song.");
        System.out.println("2. Change your personal account data.");
        System.out.println("3. Exit.");
    }

    private void printSearchSongMenu() {
        System.out.println("1. Search song by name.");
        System.out.println("2. Search song by artist.");

        setUserChoice();

        switch(userChoice) {
            case 1:
                printBaseData(database.selectAllSongsData());
                setUserChoice();
                break;
            case 2:
                printBaseData(database.selectAllArtistsData());
                setUserChoice();
                break;
            default:
                System.out.println("Incorrect input! Please, restart the program!");
                break;
        }
    }

    private void printBaseData(String source) {
        try {
            database.printBaseData(source);
        } catch(SQLException exception) {
            exception.printStackTrace();
        }
    }

    private void congratulationMessage() {
        System.out.println("Welcome to the awesome playlist! You can search and add different songs to your own playlist: ");
    }

    private void setUserChoice() {
        System.out.println();
        System.out.print("Your choice: ");
        enterSomeIntegerValue();
        System.out.println();
    }

    private void enterSomeIntegerValue() {
        try {
            userChoice = input.nextInt();
        } catch(InputMismatchException exception) {
            System.out.println("Mismatch! Restart the program!");
        }
    }
}