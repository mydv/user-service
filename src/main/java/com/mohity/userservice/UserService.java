package com.mohity.userservice;

import java.util.Optional;

public interface UserService {

    UserResponse createUser(UserCreateRequest userCreateRequest);

    Optional<UserResponse> getUserById(Long id);
}
