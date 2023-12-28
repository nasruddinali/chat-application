package com.chatApplication.exception;

public class ActionNotAllowed extends Exception{
    private String message;

    public ActionNotAllowed(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
