package com.Sixtel;

import java.util.HashMap;

/**
 * Created by branden on 2/23/16 at 19:39.
 */
public class JsonWrapper {

    HashMap<String, User> wrappedData = new HashMap<>();

    public JsonWrapper() {
    }

    public JsonWrapper(HashMap<String, User> jsonWrapper) {
        this.wrappedData = jsonWrapper;
    }

    public HashMap<String, User> getWrappedData() {
        return wrappedData;
    }

    public void setWrappedData(HashMap<String, User> wrappedData) {
        this.wrappedData = wrappedData;
    }



}