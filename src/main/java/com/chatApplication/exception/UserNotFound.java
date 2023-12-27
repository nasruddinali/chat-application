package com.chatApplication.exception;

public class UserNotFound extends Exception{
    private String message;

    public UserNotFound(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
