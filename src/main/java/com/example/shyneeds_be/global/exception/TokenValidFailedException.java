package com.example.shyneeds_be.global.exception;

public class TokenValidFailedException extends RuntimeException{

    public TokenValidFailedException(){
        super("Failed to generate Token");
    }
    public TokenValidFailedException(String message) {
        super(message);
    }
}
