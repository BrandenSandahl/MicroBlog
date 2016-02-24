package com.Sixtel;

/**
 * Created by branden on 2/22/16 at 18:10.
 */
public class Messages {

    private String message;
    //int id;


    public Messages() {
    }

    public Messages(String message) {
        setMessage(message);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}