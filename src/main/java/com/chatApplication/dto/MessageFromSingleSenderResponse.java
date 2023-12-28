package com.chatApplication.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MessageFromSingleSenderResponse {
    private String username;
    private List<String> messages;
}
