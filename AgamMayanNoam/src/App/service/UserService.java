package App.service;

import App.DataManagement.dao;
import App.Entity.User;
import App.Exceptions.UserNameTakenException;
import App.Exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Scanner;

@Component("userService")
public class UserService {
    @Autowired
    private dao db;

    public User register(User u) throws Exception {
        if(db.usernameExists(u.getUserName()))
            throw new UserNameTakenException(u.getUserName());
        return db.registerUser(u);
    }

    public ArrayList<User> getUsers() throws Exception {
        return db.getAllUsers();
    }

    public User login(User u) throws Exception {
        if(db.successLogin(u.getUserName(), u.getPassword()))
            return u;
        throw new UserNotFoundException(u.getUserName());
    }



}
