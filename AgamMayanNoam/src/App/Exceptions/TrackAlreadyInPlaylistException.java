package App.Exceptions;

public class TrackAlreadyInPlaylistException extends Exception{
    public TrackAlreadyInPlaylistException(int id, String trackName)
    {
        super("Playlist: " + id + " already has track: " + trackName);
    }
}
