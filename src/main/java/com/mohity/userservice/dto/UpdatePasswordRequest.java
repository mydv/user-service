package com.mohity.userservice.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class UpdatePasswordRequest {
    @NotEmpty(message = "New password is required")
    private String newPassword;
}
