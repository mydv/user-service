package com.mohity.userservice;

import lombok.Data;

@Data
public class UserUpdateRequest {
    private String name;
    private String email;
    private String phone;
    private String city;
    private String linkedinProfile;
    private String bio;
}
