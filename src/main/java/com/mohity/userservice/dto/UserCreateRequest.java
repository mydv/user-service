package com.mohity.userservice.dto;

import lombok.Data;

@Data
public class UserCreateRequest {
    private String name;
    private String email;
    private String password;
}
