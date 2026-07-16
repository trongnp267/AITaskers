package com.example.demo.notification;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/notifications")
public class NotificationController {
    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping
    public List<NotificationResponse> getNotifications(
            Principal principal,
            @RequestParam(required = false) NotificationType type
    ) {
        return notificationService.getNotifications(principal.getName(), type);
    }

    @GetMapping("/unread-count")
    public Map<String, Long> getUnreadCount(Principal principal) {
        return Map.of("count", notificationService.countUnread(principal.getName()));
    }

    @PostMapping
    public NotificationResponse createNotification(
            Principal principal,
            @Valid @RequestBody CreateNotificationRequest request
    ) {
        return notificationService.createForCurrentUser(principal.getName(), request);
    }

    @PostMapping("/{id}/read")
    public NotificationResponse markAsRead(Principal principal, @PathVariable Long id) {
        return notificationService.markAsRead(principal.getName(), id);
    }

    @PostMapping("/read-all")
    public ResponseEntity<Void> markAllAsRead(Principal principal) {
        notificationService.markAllAsRead(principal.getName());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> hideNotification(Principal principal, @PathVariable Long id) {
        notificationService.hideNotification(principal.getName(), id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/preferences")
    public List<NotificationPreferenceResponse> getPreferences(Principal principal) {
        return notificationService.getPreferences(principal.getName());
    }

    @PutMapping("/preferences")
    public List<NotificationPreferenceResponse> updatePreferences(
            Principal principal,
            @Valid @RequestBody UpdateNotificationPreferencesRequest request
    ) {
        return notificationService.updatePreferences(principal.getName(), request);
    }
}
