package com.mohity.userservice.dto;

import com.mohity.userservice.model.Role;
import lombok.Data;

@Data
public class UpdateUserRoleRequest {
    private Role role;
}
