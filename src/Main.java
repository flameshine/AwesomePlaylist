import java.util.*;
import java.sql.*;
import Data.DataBase;

public class Main {

    private static Scanner input = new Scanner(System.in);
    private final DataBase database = new DataBase();
    private static int userChoice;

    public static void main(String[] args) {
        congratulationMessage();
        printMainData();
    }

    private static void printMainData() {

        boolean play = true;

        while(play) {
            printMainMenu();
            System.out.print("Your choice: ");
            enterSomeIntegerValue();

            switch(userChoice) {
                case 1:
                    printSearchSongMenu();
                    enterSomeIntegerValue();
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

    private static void printMainMenu() {
        System.out.println("1. Search and add song.");
        System.out.println("2. Change username or password.");
        System.out.println("3. Exit.");
    }

    private static void printSearchSongMenu() {
        System.out.println("1. Search song by name.");
        System.out.println("2. Search song by artist.");
    }

    private static void congratulationMessage() {
        System.out.println("Welcome to the awesome playlist! You can search and add different songs to your own playlist: ");
    }

    private static void enterSomeIntegerValue() {
        try {
            userChoice = input.nextInt();
        } catch(InputMismatchException exception) {
            System.out.println("Mismatch! Restart the program!");
        }
    }
}