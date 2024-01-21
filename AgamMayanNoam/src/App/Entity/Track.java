package App.Entity;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Objects;
public class Track implements Comparable<Track>, Serializable {
    private String trackId;
    private String trackName;
    private String trackArtist;
    private String trackAlbum;
    private String trackAlbumUrl;
    private String trackArtistUrl;
    public Track(String tId, String tName, String tArtist, String tAlbum, String tAlbumUrl, String artistUrl)
    {
        trackId = tId;
        trackName = tName;
        trackArtist = tArtist;
        trackAlbum = tAlbum;
        trackAlbumUrl = tAlbumUrl;
        trackArtistUrl = artistUrl;
    }

    public String getTrackId() {
        return trackId;
    }

    public String getTrackName() {
        return trackName;
    }

    public String getTrackArtist() {
        return trackArtist;
    }

    public String getTrackAlbum() {
        return trackAlbum;
    }

    public String getTrackAlbumUrl() {
        return trackAlbumUrl;
    }

    public String getTrackArtistUrl() {
        return trackArtistUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Track track = (Track) o;
        return Objects.equals(trackId, track.trackId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(trackId);
    }

    @Override
    public String toString() {
        return "Track{" +
                "trackId='" + trackId + '\'' +
                ", trackName='" + trackName + '\'' +
                ", trackArtist='" + trackArtist + '\'' +
                ", trackAlbum='" + trackAlbum + '\'' +
                ", trackAlbumUrl='" + trackAlbumUrl + '\'' +
                ", trackArtistUrl='" + trackArtistUrl + '\'' +
                '}';
    }

    @Override
    public int compareTo(Track o) {
        return this.trackName.compareTo(o.trackName);
    }


}
