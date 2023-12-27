package com.chatApplication.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter

public class AllUserResponse {
    private String status;
    private List<String> users;
}
