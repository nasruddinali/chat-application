package com.chatApplication.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class UserCreateResponse {
    private String status;
    private String message;
    private HttpStatus httpStatus;
}
