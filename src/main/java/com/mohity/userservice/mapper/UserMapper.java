package com.mohity.userservice.mapper;

import com.mohity.userservice.dto.UserResponse;
import com.mohity.userservice.model.User;

public class UserMapper {

    public static UserResponse toUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .phone(user.getPhone())
                .city(user.getCity())
                .linkedinProfile(user.getLinkedinProfile())
                .bio(user.getBio())
                .build();
    }
}
