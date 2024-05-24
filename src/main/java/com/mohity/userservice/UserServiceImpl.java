package com.mohity.userservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.mohity.userservice.Role.USER;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    private static final Role DEFAULT_ROLE = USER;

    @Override
    public UserResponse createUser(UserCreateRequest request) {
        Optional<User> existingUser = userRepository.findByEmail(request.getEmail());
        if (existingUser.isPresent()) {
            throw new RuntimeException("User with this email already exists");
        }

        // hash password before saving?

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(request.getPassword())
                .role(DEFAULT_ROLE)
                .build();

        // save
        User savedUser = userRepository.save(user);

        // mapper
        return UserResponse.builder()
                .id(savedUser.getId())
                .name(savedUser.getName())
                .email(savedUser.getEmail())
                .role(savedUser.getRole())
                .build();
    }

    @Override
    public Optional<UserResponse> getUserById(Long id) {
        return Optional.empty();
    }
}
