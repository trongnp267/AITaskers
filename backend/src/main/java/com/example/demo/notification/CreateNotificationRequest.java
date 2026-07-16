package com.example.demo.notification;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateNotificationRequest(
        @NotNull NotificationType type,
        @NotBlank @Size(max = 180) String title,
        @NotBlank @Size(max = 1000) String message,
        @Size(max = 500) String targetUrl
) {
}
