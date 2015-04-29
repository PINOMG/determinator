package com.pinomg.determinator;

/**
 * The Friend class. Holds all information about a friend.
 */
public class Friend {
    public String name;
    public int id;

    public Friend(String name, int id) {
        this.name = name;
        this.id = id;
    }

    //This is required by the adapter for output in a list.
    public String toString(){
        return this.name;
    }

}
