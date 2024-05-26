package com.mohity.userservice.service;

import com.mohity.userservice.dto.*;
import com.mohity.userservice.mapper.UserMapper;
import com.mohity.userservice.model.Role;
import com.mohity.userservice.model.User;
import com.mohity.userservice.repository.UserRepository;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.mohity.userservice.model.Role.USER;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private KafkaProducerService kafkaProducerService;

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

        UserEvent userEvent = UserEvent.builder()
                .eventType(EventType.CREATE_USER)
                .timestamp(LocalDateTime.now())
                .userId(userResponse.getId())
                .build();
        kafkaProducerService.publishUserEvent(userEvent);

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

        UserEvent userEvent = UserEvent.builder()
                .eventType(EventType.READ_USER)
                .timestamp(LocalDateTime.now())
                .userId(userResponse.getId())
                .build();
        kafkaProducerService.publishUserEvent(userEvent);

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

        UserEvent userEvent = UserEvent.builder()
                .eventType(EventType.UPDATE_USER)
                .timestamp(LocalDateTime.now())
                .userId(userResponse.getId())
                .build();
        kafkaProducerService.publishUserEvent(userEvent);

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

        UserEvent userEvent = UserEvent.builder()
                .eventType(EventType.UPDATE_USER_PASSWORD)
                .timestamp(LocalDateTime.now())
                .userId(id)
                .build();
        kafkaProducerService.publishUserEvent(userEvent);

        return Optional.of(userResponse);
    }

    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }

        userRepository.deleteById(id);

        UserEvent userEvent = UserEvent.builder()
                .eventType(EventType.DELETE_USER)
                .timestamp(LocalDateTime.now())
                .userId(id)
                .build();
        kafkaProducerService.publishUserEvent(userEvent);
    }

    @Override
    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();

        UserEvent userEvent = UserEvent.builder()
                .eventType(EventType.READ_ALL_USERS)
                .timestamp(LocalDateTime.now())
                .userId(null)
                .build();
        kafkaProducerService.publishUserEvent(userEvent);

        return users.stream().map(UserMapper::toUserResponse).toList();
    }

    @Override
    public Optional<UserResponse> updateUserRole(Long id, UpdateUserRoleRequest request) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        existingUser.setRole(request.getRole());
        User savedUser = userRepository.save(existingUser);
        UserResponse userResponse = UserMapper.toUserResponse(savedUser);

        UserEvent userEvent = UserEvent.builder()
                .eventType(EventType.UPDATE_USER_ROLE)
                .timestamp(LocalDateTime.now())
                .userId(id)
                .build();
        kafkaProducerService.publishUserEvent(userEvent);

        return Optional.of(userResponse);
    }
}
