package com.mohity.userservice.controller;

import com.mohity.userservice.service.UserService;
import com.mohity.userservice.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(summary = "Create new user", description = "Registers a new user in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping("/signup")
    public ResponseEntity<UserResponse> createUser(@RequestBody UserCreateRequest userCreateRequest) {
        UserResponse createdUser = userService.createUser(userCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @Operation(summary = "Get user by ID", description = "Retrieves a user's details by their unique identifier.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Update user by ID", description = "Updates an existing user's details by their unique identifier.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully", content = @Content(schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long id, @RequestBody UserUpdateRequest userUpdateRequest) {
        return userService.updateUser(id, userUpdateRequest)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Update User Password", description = "Allows user to change their password.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request - Invalid password format"), // Invalid input
            @ApiResponse(responseCode = "403", description = "Forbidden - Not authorized to update password"), // Unauthorized
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{id}/password")
    public ResponseEntity<UserResponse> updateUserPassword(@PathVariable Long id,
                                                           @RequestBody @Valid UpdatePasswordRequest updatePasswordRequest) {
        return userService.updateUserPassword(id, updatePasswordRequest)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Delete User by ID", description = "Deletes user from the system by their unique identifier.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User deleted successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build(); // 204 No Content on successful deletion
    }

    @Operation(summary = "Get All Users", description = "Retrieves a list of all registered users in the system. Requires an admin role for access.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserResponse.class)))), // Array of UserResponse
            @ApiResponse(responseCode = "403", description = "Forbidden - Not authorized to access this resource"), // Forbidden for non-admins
            @ApiResponse(responseCode = "500", description = "Internal server error") // Generic error
    })
    @GetMapping("/admin/all")
    public List<UserResponse> getAllUsers() {
        return userService.getAllUsers();
    }

    @Operation(summary = "Update User Role", description = "Updates the roles associated with a specific user. Requires an admin role to modify user role.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request - Invalid role data"), // Invalid input
            @ApiResponse(responseCode = "403", description = "Forbidden - Not authorized to update roles"), // Unauthorized
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/admin/{userId}/role")
    public ResponseEntity<UserResponse> updateUserRole(@PathVariable Long id, @RequestParam UpdateUserRoleRequest updateUserRoleRequest) {
        return userService.updateUserRole(id, updateUserRoleRequest)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
