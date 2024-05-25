package com.mohity.userservice;

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
