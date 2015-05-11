package com.pinomg.determinator.api;

/**
 * Created by patrik on 2015-05-11.
 */
public class NoConnectionException extends Exception{
    public NoConnectionException(){
        super();
    }

    public NoConnectionException(String msg){
        super(msg);
    }
}
