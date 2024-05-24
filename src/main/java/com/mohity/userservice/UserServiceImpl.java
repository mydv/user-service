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
}
