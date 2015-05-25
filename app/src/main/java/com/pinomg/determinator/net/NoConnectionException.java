package com.pinomg.determinator.net;

/**
 * Exception cast when device does not have a connection.
 */
public class NoConnectionException extends Exception{
    public NoConnectionException(String msg){
        super(msg);
    }
}
