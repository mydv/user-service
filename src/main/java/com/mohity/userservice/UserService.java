package com.mohity.userservice;

import java.util.Optional;

public interface UserService {

    UserResponse createUser(UserCreateRequest userCreateRequest);

    Optional<UserResponse> getUserById(Long id);

    Optional<UserResponse> updateUser(Long id, UserUpdateRequest userUpdateRequest);

    Optional<UserResponse> updateUserPassword(Long id, UpdatePasswordRequest updatePasswordRequest);
}
