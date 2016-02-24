package com.Sixtel;

import java.util.ArrayList;

/**
 * Created by branden on 2/22/16 at 12:46.
 */
public class User {

    private String name, password;
   private ArrayList<Messages> messageList = new ArrayList<>(); //i dont really fully get by this has to be a class to displau
   private int id;


    public User() {
    }

    public User(String name, String password) {
        setName(name);
        setPassword(password);
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ArrayList<Messages> getMessageList() {
        return messageList;
    }

    public void setMessageList(ArrayList<Messages> messageList) {
        this.messageList = messageList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}