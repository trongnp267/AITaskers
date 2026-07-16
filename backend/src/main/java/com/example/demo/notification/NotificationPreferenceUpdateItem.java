package com.example.demo.notification;

import jakarta.validation.constraints.NotNull;

public record NotificationPreferenceUpdateItem(
        @NotNull NotificationType type,
        @NotNull Boolean webEnabled,
        @NotNull Boolean emailEnabled
) {
}
