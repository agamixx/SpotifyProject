package App.Exceptions;

public class UserNameTakenException extends Exception{
    public UserNameTakenException(String userName)
    {
        super("The username: " + userName + " is already taken.");
    }

}
