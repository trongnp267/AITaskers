package com.example.demo.notification;

public record NotificationPreferenceResponse(
        NotificationType type,
        boolean webEnabled,
        boolean emailEnabled
) {
    public static NotificationPreferenceResponse from(NotificationDeliveryPreference preference) {
        return new NotificationPreferenceResponse(
                preference.getType(),
                preference.isWebEnabled(),
                preference.isEmailEnabled()
        );
    }
}
