package com.mohity.userservice;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Override
    public UserResponse createUser(UserCreateRequest userCreateRequest) {
        return null;
    }

    @Override
    public Optional<UserResponse> getUserById(Long id) {
        return Optional.empty();
    }
}
