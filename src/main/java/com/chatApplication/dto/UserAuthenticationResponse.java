package com.chatApplication.dto;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class  UserAuthenticationResponse {
    private String status;
    private String message;
    private HttpStatus httpStatus;

    private String token;
}
