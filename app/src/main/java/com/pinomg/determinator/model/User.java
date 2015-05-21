package com.pinomg.determinator.model;

import java.io.Serializable;

/**
 * The User class. Holds all information about a basic user.
 */
public class User implements Serializable {

    private String username;

    public User(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o == null) {
            return false;
        } else if (this.getClass() != o.getClass()) {
            return false;
        } else {
            User tmp = (User) o;
            return username.equals(tmp.getUsername());
        }
    }

    @Override
    public int hashCode() {
        // TODO: Not ideal implementation..
        return (username).hashCode();
    }
    //This is required by the adapter for output in a list.
    public String toString(){
        return username;
    }

}
