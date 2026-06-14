package com.example.demo.dto;

public class RegisterRequest {
    private String username;        // Đây chính là Email
    private String password;
    private String confirmPassword; // Xác nhận lại mật khẩu
    private String role;            // Vai trò (Ví dụ: CLIENT, FREELANCER...)

    public RegisterRequest() {}

    public RegisterRequest(String username, String password, String confirmPassword, String role) {
        this.username = username;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.role = role;
    }

    // Getters and Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getConfirmPassword() { return confirmPassword; }
    public void setConfirmPassword(String confirmPassword) { this.confirmPassword = confirmPassword; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}