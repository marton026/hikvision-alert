package com.marton.hikalert;

import java.io.Serializable;

import org.springframework.stereotype.Service;

@Service
public class GreetService implements Serializable {

    public String greet(String name) {
        if (name == null || name.isEmpty()) {
            return "Witaj !!!";
        } else if (name.equals("Marton")) {

            return "Przekierowanie";
        } else {

            return "Witaj " + name;
        }
    }


}
