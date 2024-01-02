package com.chatApplication.dto;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SendMessageDto {
    private String sender;
    private String receiver;
    private String text;
}
