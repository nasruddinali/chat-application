package com.chatApplication.exception;

public class UserAlreadyExist  extends Exception{
    private String message;

    public UserAlreadyExist(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
