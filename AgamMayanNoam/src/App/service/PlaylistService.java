package App.service;

import App.DataManagement.dao;
import App.Entity.Playlist;
import App.Entity.Track;
import App.Entity.User;
import App.Exceptions.PlaylistNotFoundInUserException;
import App.Exceptions.TrackAlreadyInPlaylistException;
import App.Exceptions.TrackNotFoundInPlaylistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Component("playlistService")
@PropertySource("setup.properties")
public class PlaylistService {
    @Autowired
    private dao db;
    @Value("${python.script}")
    private String pythonScriptPath;
    @Value("${python.interpreter}")
    private String pythonInterpreterPath;

    public void addPlaylist(String playlistName, User u) throws Exception {
        db.writePlaylist(new Playlist(playlistName, u.getUserName()));
    }

    public void deletePlaylist(int playlistId,User u) throws Exception {
        if(getPlaylistById(playlistId,u) == null)
            throw new PlaylistNotFoundInUserException(playlistId,u.getUserName());
        db.removePlaylist(playlistId, u);
    }

    public ArrayList<Track> searchSong(String songName) throws IOException {
        String trackId;
        String trackName;
        String albumName;
        String artistName;
        String albumImage;
        String artistLink;
        ArrayList<Track> searchResults = new ArrayList<>();

        String[] cmd = new String[3];
        cmd[0] = pythonInterpreterPath;
        cmd[1] = pythonScriptPath;
        cmd[2] = songName;

        ProcessBuilder pb = new ProcessBuilder(cmd);
        Process p = pb.start();

        BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));

        // Read the output from the command
        String s;
        while ((s = stdInput.readLine()) != null) {
            trackId = s;
            trackName = stdInput.readLine();
            albumName = stdInput.readLine();
            artistName = stdInput.readLine();
            albumImage = stdInput.readLine();
            artistLink = stdInput.readLine();
            searchResults.add(new Track(trackId, trackName, artistName, albumName, albumImage, artistLink));
        }
        return searchResults;
    }
    public void addSongToPlaylist(int playlistId, Track song, User u) throws Exception {
        if(db.getPlaylistById(playlistId,u) == null)
            throw new PlaylistNotFoundInUserException(playlistId,u.getUserName());
        List<Playlist> l = db.getAllPlaylistByUser(u);
        if(getPlaylistById(playlistId, u).isInPlaylist(song))
            throw new TrackAlreadyInPlaylistException(playlistId, song.getTrackName());
        db.addTrackToPlaylist(playlistId, song, u);
    }

    public void removeSongFromPlaylist(int playlistId, String trackId, User u) throws Exception {
        if(db.getPlaylistById(playlistId,u) == null)
            throw new PlaylistNotFoundInUserException(playlistId, u.getUserName());
        if(db.getPlaylistById(playlistId,u).isInPlaylist(trackId)) {
            db.removeTrackFromPlaylist(playlistId, trackId, u);
            return;
        }
        throw new TrackNotFoundInPlaylistException(playlistId, trackId);
    }

    private Playlist getPlaylistById(int id, User u) throws Exception {
        if(db.getPlaylistById(id, u) != null)
            return db.getPlaylistById(id, u);
        throw new PlaylistNotFoundInUserException(id, u.getUserName());
    }
    public ArrayList<Playlist> getAllPlaylistsByUser(User u) throws Exception {
        return db.getAllPlaylistByUser(u);
    }
}
