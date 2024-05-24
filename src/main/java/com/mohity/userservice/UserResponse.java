package com.mohity.userservice;

import lombok.Data;

@Data
public class UserResponse {
    private String name;
    private String email;
    private Role role;
}
