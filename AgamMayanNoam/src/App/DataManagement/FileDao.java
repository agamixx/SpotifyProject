package App.DataManagement;

import App.Entity.Playlist;
import App.Entity.Track;
import App.Entity.User;
import App.Exceptions.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Component("myDao")
@PropertySource("setup.properties")
public class FileDao implements dao{
    @Value("${playlists.name}")
    private String pdat;
    @Value("${users.name}")
    private String udat;

    public int finalPlaylistId;

    private ArrayList<Playlist> getArrayListPlaylist(String dat) throws Exception {
        File datFile = new File(dat);
        // Read existing playlists if file exists and is not empty
        if (datFile.exists() && datFile.length() > 0) {
            FileInputStream fis = new FileInputStream(datFile);
            ObjectInputStream ois = new ObjectInputStream(fis);
            return (ArrayList<Playlist>) ois.readObject();
        }
        return null;
    }
    private ArrayList<User> getArrayListUser(String dat) throws Exception {
        File datFile = new File(dat);
        // Read existing playlists if file exists and is not empty
        if (datFile.exists() && datFile.length() > 0) {
            FileInputStream fis = new FileInputStream(datFile);
            ObjectInputStream ois = new ObjectInputStream(fis);
            return (ArrayList<User>) ois.readObject();
        }
        return null;
    }

    private <T> void writeToFile(T obj, File f)
    {
        try (FileOutputStream fos = new FileOutputStream(f);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(obj);
        } catch (Exception e) {
            e.printStackTrace();
            // Handle exception
        }
    }

    @PostConstruct
    public void startFileDao() throws Exception
    {
        ArrayList<Playlist> playlists = getArrayListPlaylist(pdat);

        // Assign ID and add the new playlist
        if(playlists == null)
            finalPlaylistId = 0;
        else if(playlists.isEmpty())
            finalPlaylistId = 0;
        else
            finalPlaylistId = playlists.get(playlists.size() - 1).getId();
    }

    @Override
    public void writePlaylist(Playlist p) throws Exception {
        File pdatFile = new File(pdat);
        ArrayList<Playlist> playlists = getArrayListPlaylist(pdat);
        if(playlists == null)
            playlists = new ArrayList<>();

        // Assign ID and add the new playlist
        p.setId(finalPlaylistId + 1);
        finalPlaylistId += 1;
        playlists.add(p);

        // Write the updated list of playlists
        writeToFile(playlists, pdatFile);
    }


    @Override
    public void removePlaylist(int id, User u) throws Exception {
        File pdatFile = new File(pdat);
        ArrayList<Playlist> playlists = getArrayListPlaylist(pdat);
        if(playlists == null)
            throw new PlaylistNotFoundInUserException(id, u.getUserName());

        // Remove the playlist with the given ID
        if(!playlists.removeIf(p -> p.getId() == id && p.getUserName().equals(u.getUserName())))
            throw new PlaylistNotFoundInUserException(id, u.getUserName());

        // Write the updated list of playlists back to the file
        writeToFile(playlists, pdatFile);
    }

    public Playlist getPlaylistById(int id, User u) throws Exception {
        ArrayList<Playlist> playlists = getArrayListPlaylist(pdat);
        if(playlists == null)
            return null;

        for (Playlist p : playlists) {
            if (p.getId() == id && u.getUserName().equals(p.getUserName()))
                return p;
        }
        return null;
    }
    @Override
    public void addTrackToPlaylist(int id, Track t, User u) throws Exception {
        File pdatFile = new File(pdat);
        ArrayList<Playlist> playlists = getArrayListPlaylist(pdat);
        if(playlists == null)
            throw new PlaylistNotFoundInUserException(id,u.getUserName());

        // find index of spesific playlist id
        for (Playlist playlist : playlists) {
            if (playlist.getId() == id && playlist.getUserName().equals(u.getUserName())) {
                playlist.addToPlaylist(t);
            }
        }
        // Write the updated list of playlists back to the file
        writeToFile(playlists, pdatFile);
    }

    @Override
    public void removeTrackFromPlaylist(int id, String trackId, User u) throws Exception {
        File pdatFile = new File(pdat);
        ArrayList<Playlist> playlists = getArrayListPlaylist(pdat);
        if(playlists == null)
            throw new PlaylistNotFoundInUserException(id, u.getUserName());


        for (Playlist playlist : playlists) {
            if (playlist.getId() == id && playlist.getUserName().equals(u.getUserName())) {
                playlist.removeFromPlaylist(trackId);
            }
        }

        // Write the updated list of playlists back to the file
        writeToFile(playlists, pdatFile);
    }

    @Override
    public ArrayList<Playlist> getAllPlaylistByUser(User u) throws Exception {
        String userName = u.getUserName();
        ArrayList<Playlist> ret = new ArrayList<>();
        ArrayList<Playlist> playlists = getArrayListPlaylist(pdat);
        if(playlists == null)
            return ret;

        // make ret value
        for (Playlist playlist : playlists) {
            if (playlist.getUserName().equals(userName)) {
                ret.add(playlist);
            }
        }
        return ret;
    }

    public boolean usernameExists(String username) throws Exception {
        List<User> l = getAllUsers();
        if(l == null)
            return false;
        for (User u : l) {
            if(u.getUserName().equals(username))
                return true;
        }
        return false;
    }

    @Override
    public User registerUser(User u) throws Exception {
        File pdatFile = new File(udat);
        ArrayList<User> users = getAllUsers();
        if(users == null)
            users = new ArrayList<>();
        users.add(u);

        // Write the updated list of users back to the file
        writeToFile(users, pdatFile);
        return u;
    }

    public boolean successLogin(String username, String password) throws Exception {
        ArrayList<User> users = getAllUsers();
        for(User u : users)
            if(u.getUserName().equals(username) && u.getPassword().equals(password))
                return true;
        return false;
    }

    @Override
    public User loginUser(User u) throws Exception {
        File pdatFile = new File(udat);
        ArrayList<User> users = getAllUsers();

        // find if user exists
        for (User user : users)
            if (user.getUserName().equals(u.getUserName()) && user.getPassword().equals(u.getPassword()))
                return user;

        throw new UserNotFoundException(u.getUserName());
    }

    public ArrayList<User> getAllUsers() throws Exception {
        try{
            return getArrayListUser(udat);
        }catch (IOException e)
        {
            return null;
        }

    }
}
