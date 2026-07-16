package com.example.demo.notification;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record UpdateNotificationPreferencesRequest(
        @NotEmpty List<@Valid NotificationPreferenceUpdateItem> preferences
) {
}
