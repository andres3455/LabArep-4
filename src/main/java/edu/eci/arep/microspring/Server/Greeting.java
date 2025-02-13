package edu.eci.arep.microspring.Server;

public class Greeting {

    private String message;

    public Greeting(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "{\"message\": \"" + message + "\"}";
    }

}
