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

        // Assign ID and add the new playlist
        p.setId(finalPlaylistId + 1);
        finalPlaylistId += 1;
        playlists.add(p);

        // Write the updated list of playlists
        try (FileOutputStream fos = new FileOutputStream(pdatFile);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(playlists);
        } catch (Exception e) {
            e.printStackTrace();
            // Handle exception
        }
    }


    @Override
    public void removePlaylist(int id, User u) throws Exception {
        File pdatFile = new File(pdat);
        ArrayList<Playlist> playlists = getArrayListPlaylist(pdat);

        // Remove the playlist with the given ID
        if(!playlists.removeIf(p -> p.getId() == id && p.getUserName().equals(u.getUserName())))
            throw new PlaylistNotFoundInUserException(id, u.getUserName());

        // Write the updated list of playlists back to the file
        try (FileOutputStream fos = new FileOutputStream(pdatFile);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(playlists);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Playlist getPlaylistById(int id, User u) throws Exception {
        ArrayList<Playlist> playlists = getArrayListPlaylist(pdat);

        for (Playlist p : playlists) {
            if(p.getId() == id)
                return p;
        }
        throw new PlaylistNotFoundInUserException(id, u.getUserName());
    }
    @Override
    public void addTrackToPlaylist(int id, Track t, User u) throws Exception {
        File pdatFile = new File(pdat);
        ArrayList<Playlist> playlists = getArrayListPlaylist(pdat);

        boolean addedSuccsessfuly = false;
        // find index of spesific playlist id
        for(int i = 0; i < playlists.size();i++)
        {
            if(playlists.get(i).getId() == id && playlists.get(i).getUserName().equals(u.getUserName()))
            {
                if(playlists.get(i).isInPlaylist(t))
                    throw new TrackAlreadyInPlaylistException(id, t.getTrackName());
                playlists.get(i).addToPlaylist(t);
                addedSuccsessfuly = true;
            }
        }
        if(!addedSuccsessfuly)
            throw new PlaylistNotFoundInUserException(id, u.getUserName());

        // Write the updated list of playlists back to the file
        try (FileOutputStream fos = new FileOutputStream(pdatFile);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(playlists);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeTrackFromPlaylist(int id, String trackId, User u) throws Exception {
        File pdatFile = new File(pdat);
        ArrayList<Playlist> playlists = getArrayListPlaylist(pdat);

        boolean removed = false;
        // find index of spesific playlist id
        for(int i = 0; i < playlists.size();i++)
        {
            if(playlists.get(i).getId() == id && playlists.get(i).getUserName().equals(u.getUserName()))
            {
                removed = playlists.get(i).removeFromPlaylist(trackId);
            }
        }
        if (!removed)
            throw new TrackNotFoundInPlaylistException(id, trackId);

        // Write the updated list of playlists back to the file
        try (FileOutputStream fos = new FileOutputStream(pdatFile);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(playlists);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<Playlist> getAllPlaylistByUser(User u) throws Exception {
        String userName = u.getUserName();
        ArrayList<Playlist> ret = new ArrayList<>();
        ArrayList<Playlist> playlists = getArrayListPlaylist(pdat);

        // make ret value
        for (Playlist playlist : playlists) {
            if (playlist.getUserName().equals(userName)) {
                ret.add(playlist);
            }
        }
        return ret;
    }

    @Override
    public User registerUser(User u) throws Exception {
        File pdatFile = new File(udat);
        ArrayList<User> users = getArrayListUser(udat);

        // find if user exists
        for (User user : users)
            if (user.equals(u))
                throw new UserNameTakenException(u.getUserName());
        users.add(u);

        // Write the updated list of users back to the file
        try (FileOutputStream fos = new FileOutputStream(pdatFile);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(users);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return u;
    }

    @Override
    public User loginUser(User u) throws Exception {
        File pdatFile = new File(udat);
        ArrayList<User> users = getArrayListUser(udat);

        // find if user exists
        for (User user : users)
            if (user.getUserName().equals(u.getUserName()) && user.getPassword().equals(u.getPassword()))
                return user;

        throw new UserNotFoundException(u.getUserName());
    }

    public ArrayList<User> getAllUsers() throws Exception {
        return getArrayListUser(udat);
    }
}
