package com.pinomg.determinator.net;

/**
 * Created by patrik on 2015-05-11.
 */
public class ApiErrorException extends Exception{
    private int code;

    public ApiErrorException(){
        super();
    }

    public ApiErrorException(String err){
        super(err);
    }

    public  ApiErrorException(String err, int i){
        super(err);
        this.code = i;
    }

    public int getCode(){
        return this.code;
    }
}
