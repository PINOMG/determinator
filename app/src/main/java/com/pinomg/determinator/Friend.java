package com.pinomg.determinator;

import java.io.Serializable;

/**
 * The Friend class. Holds all information about a friend.
 */
public class Friend implements Serializable {
    public String name;

    public Friend(String name) {
        this.name = name;
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
            Friend tmp = (Friend) o;
            return this.name.equals(tmp.name);
        }
    }

    @Override
    public int hashCode() { // Usernames are unique
        return (name).hashCode();
    }
    //This is required by the adapter for output in a list.
    public String toString(){
        return this.name;
    }

}
