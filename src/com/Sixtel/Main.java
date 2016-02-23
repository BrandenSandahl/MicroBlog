package com.Sixtel;

import spark.*;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.HashMap;

public class Main {

    static User user = null; //just a temp var to hold the current user that is accessing system

    public static void main(String[] args) {
        Spark.init(); //starting Spark
        HashMap<String, User> users = new HashMap();  //hashmap that will contain all the various users (this would be pulled from DB I guess)
        User koko = new User("koko", "pelli");  //adding in a user to verify
        users.put("koko", koko);


        Spark.get(
                "/",
                ((request, response) -> {  //anon function
                    HashMap m = new HashMap();   //temp HashMap. not 100% on why we need this but it seems to be because of Mustache.
                    if (user != null) {
                        m.put("name", user.name);  //if the user has entered something then add to my hashmap
                        m.put("messages", user.messageList); //i think this is just a way for mustache to display all this?
                        return new ModelAndView(m, "messages.html");
                    } else {
                        return new ModelAndView(m, "index.html");
                    }
                }),
                new MustacheTemplateEngine()
        );

        Spark.post(
                "/create-user",
                ((request, response) -> {
                    String name = request.queryParams("nameInput");
                    String pass = request.queryParams("passwordInput");

                    //i could change this logic around a little bit to get rid of that && but it doesnt really seem worth the trouble
                    if (users.containsKey(name) && (users.get(name).password.equalsIgnoreCase(pass))) {  //if the user exists and passwords match
                        user = new User(name, pass);
                        response.redirect("/");
                        return "";
                    } else if (!users.containsKey(name)) {   //if the user does not yet exist
                        user = new User(name, pass);
                        users.put(user.name, user);
                        response.redirect("/");
                        return "";
                    } else {  //otherwise just go back to index
                        response.redirect("/");
                        return "";
                    }

                }));

        Spark.post(
                "/create-message",
                ((request, response) -> {
                    Messages m = new Messages(request.queryParams("messageInput"));  //new message object
                    user.messageList.add(m);  //add to the current user
                    users.put(user.name, user); //add the hashmap  of all users too
                    response.redirect("/");
                    return "";
                }));


    }


}
