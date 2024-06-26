package com.mohity.userservice.dto;

import com.mohity.userservice.model.Role;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {
    private Long id;
    private String name;
    private String email;
    private Role role;
    private String phone;
    private String city;
    private String linkedinProfile;
    private String bio;
}
