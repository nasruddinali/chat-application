package com.chatApplication.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@Builder
public class UserCreateResponse {
    private String status;
    private String message;
    private HttpStatus httpStatus;
}
