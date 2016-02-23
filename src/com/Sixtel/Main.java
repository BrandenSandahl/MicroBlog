package com.Sixtel;

import spark.*;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.HashMap;

public class Main {

    //static HashMap<String , User> user = null; //just a temp var to hold the current user that is accessing system

    static HashMap<String, User> userMap = new HashMap();
    static boolean passMisMatch = false;

    public static void main(String[] args) {
        Spark.init(); //starting Spark


       // HashMap<String, User> userMap = new HashMap();  //hashmap that will contain all the various users (this would be pulled from DB I guess)

        userMap.put("koko", new User("koko", "pelli"));  //adding in a user to verify

        Spark.get(
                "/",
                ((request, response) -> {  //anon function
                    HashMap m = new HashMap();   //temp HashMap. not 100% on why we need this but it seems to be because of Mustache.
                    //get reference to current user
                    User user = getUserFromSession(request.session());



                    if (user != null) {
                      //  m.put("name", user.name);  //if the user has entered something then add to my hashmap
                       // m.put("messages", user.messageList); //i think this is just a way for mustache to display all this?

                        return new ModelAndView(user, "messages.html");  //can also just pass the user here
                    } else {
                        m.put("passMisMatch", passMisMatch);
                        return new ModelAndView(m, "index.html");  //can also just pass the user here
                    }
                }),
                new MustacheTemplateEngine()
        );

        Spark.post(
                "/create-user",
                ((request, response) -> {
                    String name = request.queryParams("nameInput");
                    String pass = request.queryParams("passwordInput");


                    if (!userMap.containsKey(name)) {  //if the user does not yet exist
                        //add the user to the map
                        userMap.put(name, new User(name, pass));

                        //create session for user
                        Session session = request.session();
                        session.attribute("userName", name);

                        response.redirect("/");
                        return "";
                    } else if ((userMap.get(name).password.equalsIgnoreCase(pass))) {   //if the user does exist and pass matches
                        //create session for user
                        Session session = request.session();
                        session.attribute("userName", name);

                        response.redirect("/");
                        return "";
                    } else {  //otherwise just go back to index because the user entered bad pass
                        //create a session for the bad pass user
                       // Session session = request.session();
                       // session.attribute("userName", name);

                        //userMap.get(name).passMisMatch = true;
                        passMisMatch = true;

                        response.redirect("/");
                        return "";
                    }

                }));

        Spark.post(
                "/create-message",
                ((request, response) -> {
                    //get a reference to the user
                    User user = getUserFromSession(request.session());


                    Messages m = new Messages(request.queryParams("messageInput"));  //new message object
                    user.messageList.add(m);  //add to the current user
                    response.redirect("/");
                    return "";
                }));

        Spark.post(
                "log-out",
                ((request, response) -> {
                    Session session = request.session();
                    session.invalidate();
                    response.redirect("/");
                    return "";
                })
        );


    }

    static User getUserFromSession(Session session) {
        String name = session.attribute("userName");
        return  userMap.get(name);
    }



}
