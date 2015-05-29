package com.pinomg.determinator.net;

/**
 * The Exception thrown when the server wants to communicate an error to the application. See API documentation for error codes.
 */
public class ApiErrorException extends Exception{
    private int code;

    public ApiErrorException(String err, int i){
        super(err);
        this.code = i;
    }

    public int getCode(){
        return this.code;
    }
}
