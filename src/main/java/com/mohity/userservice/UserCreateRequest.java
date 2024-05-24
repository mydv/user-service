package com.mohity.userservice;

import lombok.Data;

@Data
public class UserCreateRequest {
    private String name;
    private String email;
    private String password;
}
