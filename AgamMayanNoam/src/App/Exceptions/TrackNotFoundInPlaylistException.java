package App.Exceptions;

public class TrackNotFoundInPlaylistException extends Exception{
    public TrackNotFoundInPlaylistException(int id, String trackName)
    {
        super("Playlist: " + id + " doesn't contain the trackId: " + trackName);
    }
}
