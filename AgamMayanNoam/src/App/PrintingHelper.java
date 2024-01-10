package App;

import App.Entity.Track;

import java.util.ArrayList;
import java.util.Scanner;

public class PrintingHelper {

    public static void printMenu()
    {
        System.out.println("---------------------------------------------");
        System.out.println("                 Main Menu");
        System.out.println("---------------------------------------------");
        System.out.println("---------------------------------------------");
        System.out.println("          Press 1 to Make a playlist");
        System.out.println("---------------------------------------------");
        System.out.println("---------------------------------------------");
        System.out.println("       Press 2 to Remove a playlist");
        System.out.println("---------------------------------------------");
        System.out.println("---------------------------------------------");
        System.out.println("      Press 3 to Add song to playlist");
        System.out.println("---------------------------------------------");
        System.out.println("---------------------------------------------");
        System.out.println("    Press 4 to Remove song from playlist");
        System.out.println("---------------------------------------------");
        System.out.println("---------------------------------------------");
        System.out.println("      Press 5 to display your playlists");
        System.out.println("---------------------------------------------");
        System.out.println("---------------------------------------------");
        System.out.println("              Press 6 to exit");
        System.out.println("---------------------------------------------");
    }

    public static void printWelcome()
    {
        System.out.println("---------------------------------------------");
        System.out.println("           WELCOME TO STATIFY");
        System.out.println("---------------------------------------------");
        System.out.println("---------------------------------------------");
        System.out.println("           Press 1 to login");
        System.out.println("---------------------------------------------");
        System.out.println("---------------------------------------------");
        System.out.println("           Press 2 to register");
        System.out.println("---------------------------------------------");
        System.out.println("---------------------------------------------");
        System.out.println("           Press 3 to exit");
        System.out.println("---------------------------------------------");
    }

    public static void printSearchResults(ArrayList<Track> searchResults)
    {
        int counter = 1;
        for (Track t : searchResults) {
            System.out.println(counter + ". " + t);
            counter++;
        }
    }

    public static int scanInt(String msg)
    {
        System.out.print(msg);
        Scanner scanner = new Scanner(System.in);
        return Integer.parseInt(scanner.nextLine());
    }
    public static String scanString(String msg)
    {
        System.out.print(msg);
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }


}
