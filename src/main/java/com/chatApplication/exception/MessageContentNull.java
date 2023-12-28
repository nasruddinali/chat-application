package com.chatApplication.exception;

public class MessageContentNull extends Exception{
    private String message;
    public MessageContentNull(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
