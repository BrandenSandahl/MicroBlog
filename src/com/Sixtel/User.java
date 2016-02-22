package com.Sixtel;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by branden on 2/22/16 at 12:46.
 */
public class User {

    String name;
    ArrayList<String> messages = new ArrayList<>();

    public User(String name) {
        this.name = name;
    }
}