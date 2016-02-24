package com.Sixtel;

import jodd.json.JsonParser;
import jodd.json.JsonSerializer;
import spark.*;
import spark.template.mustache.MustacheTemplateEngine;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

public class Main {


    static HashMap<String, User> userMap = new HashMap();
    static boolean passMisMatch = false;

    public static void main(String[] args) {
        Spark.init(); //starting Spark

        Spark.get(
                "/",
                ((request, response) -> {  //anon function
                    HashMap m = new HashMap();   //temp HashMap needed for Mustache
                    //get reference to current user
                    User user = getUserFromSession(request.session());


                    if (user != null) {
                       m.put("name", user.getName());  //if the user has entered something then add to my hashmap
                       m.put("messages", user.getMessageList()); //i think this is just a way for mustache to display all this?
                        return new ModelAndView(m, "messages.html");  //can also just pass the user here
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

                   try {
                       userMap = readFromJson(); //run a function to populate the map from a file
                   } catch (Exception e) {
                        //this can just continue on if the file doesn't exist. NBD.
                   }

                    if (!userMap.containsKey(name)) {  //if the user does not yet exist
                        //add the user to the map
                        userMap.put(name, new User(name, pass));

                        //create session for user
                        Session session = request.session();
                        session.attribute("userName", name);

                        response.redirect("/");
                        return "";
                    } else if ((userMap.get(name).getPassword().equalsIgnoreCase(pass))) {   //if the user does exist and pass matches
                        //create session for user
                        Session session = request.session();
                        session.attribute("userName", name);

                        response.redirect("/");
                        return "";
                    } else {  //otherwise just go back to index because the user entered bad pass

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
                    user.getMessageList().add(m);  //add to the current user
                    response.redirect("/");
                    return "";
                })
        );

        Spark.post(
                "delete-message",
                ((request1, response1) -> {
                    //reference to user
                    User user = getUserFromSession(request1.session());

                    int index = (Integer.parseInt(request1.queryParams("deleteInput")) - 1 );  //index is zero-indexed
                    user.getMessageList().remove(index); //this should work to delete the message
                    response1.redirect("/");
                    return "";
                })
        );

        Spark.post(
                "edit-message",
                ((request1, response1) -> {
                    //reference to user
                    User user = getUserFromSession(request1.session());

                    int index = (Integer.parseInt(request1.queryParams("editNumberInput")) - 1 ); //get index
                    Messages m = new Messages(request1.queryParams("editMessageInput")); //get new message object

                    user.getMessageList().set(index, m); //replace the old object with the new object.
                    response1.redirect("/");
                    return "";
                })
        );

        Spark.post(
                "log-out",
                ((request, response) -> {
                    //make sure map is up to date not sure that i need to but it cant hurt?
                    User user = getUserFromSession(request.session());
                    userMap.put(user.getName(), user);

                    //save to JSON
                    saveToJson();

                    Session session = request.session();
                    session.invalidate();
                    response.redirect("/");
                    return "";
                })
        );


    }

    static void saveToJson() throws IOException {
        JsonSerializer serializer = new JsonSerializer();
        File f = new File("microMessages.json");
        FileWriter fw = new FileWriter(f);

        JsonWrapper wrapper = new JsonWrapper(userMap);  //special class made solely to wrap the hashmap containing users

        //note this little call to setClassMetadataName here. Had to do that to make this work. From Docs.
        String serialized = serializer.deep(true).setClassMetadataName("JsonWrapper").serialize(wrapper);

        fw.write(serialized);
        fw.close();
    }

    static HashMap<String, User> readFromJson() throws FileNotFoundException {


        JsonParser parser = new JsonParser();
        File f = new File("microMessages.json");
        Scanner scanner = new Scanner(f);

        scanner.useDelimiter("\\Z");
        String data = scanner.next();

        JsonWrapper wrappedData = parser.parse(data, JsonWrapper.class);

        HashMap<String, User> m = new HashMap<>();

        wrappedData.getWrappedData().forEach((k, v) -> m.put(k, v));  //anon function that populates a map

        return m;
    }

    static User getUserFromSession(Session session) {
        String name = session.attribute("userName");
        return  userMap.get(name);
    }



}
