package com.Sixtel;

import spark.*;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.HashMap;

public class Main {

    static User user = null;

    public static void main(String[] args) {
        Spark.init();


        Spark.get(
                "/",
                ((request, response) -> {  //anon function
                    HashMap m = new HashMap();
                    if (user != null) {
                        m.put("name", user.name);
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
                    user = new User(name);
                    response.redirect("/");
                    return "";
                }));

        Spark.post(
                "/create-message",
                ((request, response) -> {
                    String message = request.queryParams("messageInput");
                    user.messages.add(message);
                    response.redirect("/");
                    return "";
                }));


    }


}
