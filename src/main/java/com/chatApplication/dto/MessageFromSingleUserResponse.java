package com.chatApplication.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MessageFromSingleUserResponse {
    private String status;
    private List<MessageFromSingleSenderResponse> data;
}
