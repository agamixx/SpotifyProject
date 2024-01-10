import App.DataManagement.FileDao;
import App.DataManagement.dao;
import App.Entity.Playlist;
import App.Entity.Track;
import App.Entity.User;
import App.Exceptions.*;
import App.PrintingHelper;
import App.service.PlaylistService;
import App.service.UserService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.BufferedReader;
import java.io.IOException;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        //Setup-----------------------------------------------------------------------------------------------------------------
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        PlaylistService ps = context.getBean("playlistService", PlaylistService.class);
        UserService us = context.getBean("userService", UserService.class);
        User u = null;
        Scanner scanner = new Scanner(System.in);
        String userChoice;
        //----------------------------------------------------------------------------------------------------------------------

        PrintingHelper.printWelcome();
        do {
            userChoice = scanner.nextLine();
            switch (userChoice) {
                case "1":
                    do{
                        String userName = PrintingHelper.scanString("Enter username: ");
                        String password = PrintingHelper.scanString("Enter password: ");

                        try{
                            u = new User(userName, password);
                            u = us.login(u);
                        }
                        catch (UserNotFoundException unfe)
                        {
                            u = null;
                            System.out.println(unfe.getMessage());
                        } catch (Exception e) {
                            System.out.println("Something went wrong");
                        }
                    }while(u == null);
                    break;
                case "2":
                    do{
                        String userName = PrintingHelper.scanString("Enter username: ");
                        String password = PrintingHelper.scanString("Enter password: ");
                        u = new User(userName, password);
                        try{
                            u = us.register(u);
                        }
                        catch (UserNameTakenException unte)
                        {
                            u = null;
                            System.out.println(unte.getMessage());
                        }
                        catch (Exception e)
                        {
                            System.out.println("Something went wrong");
                        }
                    }while(u == null);
                    break;
                case "3":
                    System.out.println("GOODBYE!");
                    System.exit(0);
                default:
                    System.out.println("Invalid input!");
            }
        }while (!userChoice.equals("1") && !userChoice.equals("2"));

        do {
            PrintingHelper.printMenu();
            userChoice = scanner.nextLine();
            switch (userChoice) {
                case "1":
                    try {
                        ps.addPlaylist(PrintingHelper.scanString("Enter playlist name: "), u);
                    }catch (Exception e)
                    {
                        System.out.println("Something went wrong");
                    }
                    break;
                case "2":
                    try{
                        for (Playlist p : ps.getAllPlaylistsByUser(u)) {
                            System.out.println(p);
                        }
                    }catch (Exception e){
                        System.out.println("Something went wrong");
                    }

                    try{
                        ps.deletePlaylist(PrintingHelper.scanInt("Enter playlist id to remove: "), u);
                    } catch (NumberFormatException e)
                    {
                        System.out.println("Expected number!");
                    } catch (PlaylistNotFoundInUserException e) {
                        System.out.println(e.getMessage());
                    }
                    catch (Exception e)
                    {
                        System.out.println("Something went wrong");
                    }

                    break;
                case "3":
                    ArrayList<Track> searchResults = null;
                    int choice;
                    try {
                        for (Playlist p : ps.getAllPlaylistsByUser(u)) {
                            System.out.println(p);
                        }
                        choice = PrintingHelper.scanInt("Enter playlist id to add track: ");
                        ps.getPlaylistById(choice,u);
                        searchResults = ps.searchSong(PrintingHelper.scanString("Enter song to search: "));
                        PrintingHelper.printSearchResults(searchResults);
                        ps.addSongToPlaylist(choice,searchResults.get(PrintingHelper.scanInt("Enter song number to add: ") - 1), u);
                        System.out.println("Song added successfully!");
                    }catch (NumberFormatException e)
                    {
                        System.out.println("Expected number!");
                    }
                    catch (TrackAlreadyInPlaylistException | PlaylistNotFoundInUserException e) {
                        System.out.println(e.getMessage());
                    }
                    catch (Exception e) {
                        System.out.println("problem with searching song");
                        System.exit(1);
                    }
                    break;
                case "4":
                    try {
                        for (Playlist p : ps.getAllPlaylistsByUser(u)) {
                            System.out.println(p);
                        }
                        int playlistId = PrintingHelper.scanInt("Enter playlist id to remove track: ");
                        System.out.println(ps.getPlaylistById(playlistId, u));
                        ps.removeSongFromPlaylist(playlistId, PrintingHelper.scanString("Enter trackId (copy from the list): "), u);
                    }
                    catch (PlaylistNotFoundInUserException | TrackNotFoundInPlaylistException e)
                    {
                        System.out.println(e.getMessage());
                    }
                    catch (NumberFormatException e)
                    {
                        System.out.println("Expected number!");
                    }
                    catch (Exception e)
                    {
                        System.out.println("Something went wrong");
                    }
                    break;
                case "5":
                    try {
                        for (Playlist p : ps.getAllPlaylistsByUser(u)) {
                            System.out.println(p);
                        }
                    }catch (Exception e)
                    {
                        System.out.println("Something went wrong");
                    }
                    break;
                case "6":
                    System.out.println("GOODBYE!");
                    System.exit(0);
                default:
                    System.out.println("Invalid input!");
            }

        }while (true);
    }
}