package App.DataManagement;

import App.Entity.Playlist;
import App.Entity.Track;
import App.Entity.User;
import App.Exceptions.*;

import java.util.ArrayList;

public interface dao {
//playlist
public void writePlaylist(Playlist p) throws Exception;
public void removePlaylist(int id, User u) throws Exception;
public void addTrackToPlaylist(int id, Track t, User u) throws Exception;
public Playlist getPlaylistById(int id, User u) throws Exception;
public void removeTrackFromPlaylist(int id, String trackId, User u) throws Exception;

//user
ArrayList<Playlist> getAllPlaylistByUser(User u) throws Exception;
public User registerUser(User u) throws Exception;
public User loginUser(User u) throws Exception;
public ArrayList<User> getAllUsers() throws Exception;
}
