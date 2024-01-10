package App.Exceptions;

public class UserNotFoundException extends Exception {
    String userName;
    public UserNotFoundException(String userName)
    {
        super("Couldn't access " + userName);
        this.userName = userName;
    }
}
