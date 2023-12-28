package com.chatApplication.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreateRequest {
        private String username;
        private String passcode;
}
