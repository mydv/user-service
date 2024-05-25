package com.mohity.userservice;

import org.apache.kafka.common.errors.ResourceNotFoundException;
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
        Optional<User> existingUser = getUserByEmail(request.getEmail());
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

        User savedUser = userRepository.save(user);
        UserResponse userResponse = UserMapper.toUserResponse(savedUser);

        return userResponse;
    }

    @Override
    public Optional<UserResponse> getUserById(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if(userOptional.isEmpty()){
            throw new ResourceNotFoundException("User not found with id: " + id);
        }

        User existingUser = userOptional.get();
        UserResponse userResponse = UserMapper.toUserResponse(existingUser);

        return Optional.of(userResponse);
    }

    @Override
    public Optional<UserResponse> updateUser(Long id, UserUpdateRequest request) {
        Optional<User> userOptional = userRepository.findById(id);
        if(userOptional.isEmpty()){
            throw new ResourceNotFoundException("User not found with id: " + id);
        }

        User existingUser = userOptional.get();

        if (request.getEmail() != null && !request.getEmail().equals(existingUser.getEmail())) {
            Optional<User> existingUserByEmailOptional = getUserByEmail(request.getEmail());
            if(existingUserByEmailOptional.isPresent()){
                throw new RuntimeException("User with this email already exists");
            }
        }

        existingUser.builder()
                .name(Optional.ofNullable(request.getName()).orElse(existingUser.getName()))
                .phone(Optional.ofNullable(request.getPhone()).orElse(existingUser.getPhone()))
                .city(Optional.ofNullable(request.getCity()).orElse(existingUser.getCity()))
                .linkedinProfile(Optional.ofNullable(request.getLinkedinProfile()).orElse(existingUser.getLinkedinProfile()))
                .bio(Optional.ofNullable(request.getBio()).orElse(existingUser.getBio()));

        User savedUser = userRepository.save(existingUser);
        UserResponse userResponse = UserMapper.toUserResponse(savedUser);

        return Optional.of(userResponse);
    }

    private Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<UserResponse> updateUserPassword(Long id, UpdatePasswordRequest request) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        existingUser.setPassword(request.getNewPassword());

        User savedUser = userRepository.save(existingUser);
        UserResponse userResponse = UserMapper.toUserResponse(savedUser);

        return Optional.of(userResponse);
    }

    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }

        userRepository.deleteById(id);
    }
}
