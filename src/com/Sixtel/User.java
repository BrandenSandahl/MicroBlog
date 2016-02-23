package com.Sixtel;

import java.util.ArrayList;

/**
 * Created by branden on 2/22/16 at 12:46.
 */
public class User {

    String name, password;
    ArrayList<Messages> messageList = new ArrayList<>(); //i dont really fully get by this has to be a class to displau
    int id;

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }
}