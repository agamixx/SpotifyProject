package App.Exceptions;

public class PlaylistNotFoundInUserException extends Exception{
    public PlaylistNotFoundInUserException(int id, String userName)
    {
        super("User: " + userName + " doesn't have a playlist with id: " + id);
    }
}
