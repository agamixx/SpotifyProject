package App.Entity;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Vector;

public class Playlist implements Serializable{

    private String userName;
    private String playlistName;
    private int playlistId;
    private List<Track> tracks;
    public Playlist(String pName, String uName)
    {
        userName = uName;
        playlistName = pName;
        tracks = new
                ArrayList<>();
    }

    public void addToPlaylist(Track t)
    {
        tracks.add(t);
    }
    public boolean removeFromPlaylist(String trackId)
    {
        for(int i = 0; i < tracks.size();i++)
        {
            if(tracks.get(i).getTrackId().equals(trackId))
            {
                tracks.remove(i);
                return true;
            }
        }
        return false;
    }

    public boolean isInPlaylist(Track t)
    {
        for (Track track : tracks) {
            if(track.equals(t))
                return true;
        }
        return false;
    }

    public boolean isInPlaylist(String trackId)
    {
        for (Track track : tracks) {
            if(track.getTrackId().equals(trackId))
                return true;
        }
        return false;
    }

    @Override
    public String toString() {
        String first = "Playlist{" +
                "userName='" + userName + '\'' +
                ", playlistName='" + playlistName + '\'' +
                ", playlistId=" + playlistId +
                '}' + "\nTracks:";
        String tracks = "";
        for (Track t : this.tracks) {
            tracks += t.toString() + '\n';
        }
        return first + tracks;
    }

    public void setId(int id)
    {
        playlistId = id;
    }

    public String getUserName()
    {
        return this.userName;
    }
    public int getPlaylistSize()
    {
        return tracks.size();
    }

    @Override
    public boolean equals(Object o) {
        if(o.getClass() != this.getClass()) return false;
        return this.playlistId == ((Playlist)o).playlistId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(tracks);
    }

    public int getId() {
        return playlistId;
    }
}
