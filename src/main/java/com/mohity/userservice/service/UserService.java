package com.mohity.userservice.service;

import com.mohity.userservice.dto.*;

import java.util.List;
import java.util.Optional;

public interface UserService {

    UserResponse createUser(UserCreateRequest userCreateRequest);

    Optional<UserResponse> getUserById(Long id);

    Optional<UserResponse> updateUser(Long id, UserUpdateRequest userUpdateRequest);

    Optional<UserResponse> updateUserPassword(Long id, UpdatePasswordRequest updatePasswordRequest);

    void deleteUser(Long id);

    List<UserResponse> getAllUsers();

    Optional<UserResponse> updateUserRole(Long id, UpdateUserRoleRequest updateUserRoleRequest);
}
