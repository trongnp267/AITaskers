package com.project.dto;

public class UserDTO {
    private final String userId;
    private final String role;

    public UserDTO(String userId, String role) {
        this.userId = userId;
        this.role = role;
    }

    public String getUserId() {
        return userId;
    }

    public String getRole() {
        return role;
    }
}
