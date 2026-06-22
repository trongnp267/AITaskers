package com.project.controller;

import com.project.dto.NotificationDTO;
import com.project.model.NotificationType;
import com.project.service.INotificationService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/project/notifications")
public class NotificationController {

    private final INotificationService notificationService;

    public NotificationController(INotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping
    public ResponseEntity<?> send(@Valid @RequestBody NotificationDTO notificationDTO) {
        try {
            if (notificationDTO.getUserId() == null) {
                return ResponseEntity.badRequest().body(Map.of("message", "User id is required"));
            }

            NotificationDTO result = notificationService.send(notificationDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (RuntimeException exception) {
            return ResponseEntity.badRequest().body(Map.of("message", exception.getMessage()));
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> findByUser(@PathVariable UUID userId) {
        try {
            if (userId == null) return ResponseEntity.badRequest().body(Map.of("message", "User id is required"));

            List<NotificationDTO> notifications = notificationService.findByUser(userId);
            return ResponseEntity.ok(notifications);
        } catch (RuntimeException exception) {
            return ResponseEntity.badRequest().body(Map.of("message", exception.getMessage()));
        }
    }

    @GetMapping("/{notificationId}")
    public ResponseEntity<?> findById(@PathVariable Long notificationId, @RequestParam UUID userId) {
        try {
            if (notificationId == null || notificationId <= 0) {
                return ResponseEntity.badRequest().body(Map.of("message", "Notification id must be greater than 0"));
            }

            return ResponseEntity.ok(notificationService.findById(notificationId, userId));
        } catch (RuntimeException exception) {
            return ResponseEntity.badRequest().body(Map.of("message", exception.getMessage()));
        }
    }

    @GetMapping("/user/{userId}/unread")
    public ResponseEntity<?> findUnreadByUser(@PathVariable UUID userId) {
        try {
            if (userId == null) return ResponseEntity.badRequest().body(Map.of("message", "User id is required"));

            List<NotificationDTO> notifications = notificationService.findUnreadByUser(userId);
            return ResponseEntity.ok(notifications);
        } catch (RuntimeException exception) {
            return ResponseEntity.badRequest().body(Map.of("message", exception.getMessage()));
        }
    }

    @GetMapping("/user/{userId}/type/{type}")
    public ResponseEntity<?> findByUserAndType(@PathVariable UUID userId, @PathVariable NotificationType type) {
        try {
            if (userId == null) return ResponseEntity.badRequest().body(Map.of("message", "User id is required"));

            List<NotificationDTO> notifications = notificationService.findByUserAndType(userId, type);
            return ResponseEntity.ok(notifications);
        } catch (RuntimeException exception) {
            return ResponseEntity.badRequest().body(Map.of("message", exception.getMessage()));
        }
    }

    @GetMapping("/user/{userId}/reference/{referenceType}")
    public ResponseEntity<?> findByUserAndReferenceType(@PathVariable UUID userId, @PathVariable String referenceType) {
        try {
            if (userId == null) return ResponseEntity.badRequest().body(Map.of("message", "User id is required"));

            List<NotificationDTO> notifications = notificationService.findByUserAndReferenceType(userId, referenceType);
            return ResponseEntity.ok(notifications);
        } catch (RuntimeException exception) {
            return ResponseEntity.badRequest().body(Map.of("message", exception.getMessage()));
        }
    }

    @GetMapping("/user/{userId}/unread-count")
    public ResponseEntity<?> countUnreadByUser(@PathVariable UUID userId) {
        try {
            if (userId == null) return ResponseEntity.badRequest().body(Map.of("message", "User id is required"));

            return ResponseEntity.ok(Map.of("userId", userId, "unreadCount", notificationService.countUnreadByUser(userId)));
        } catch (RuntimeException exception) {
            return ResponseEntity.badRequest().body(Map.of("message", exception.getMessage()));
        }
    }

    @PatchMapping("/{notificationId}/read")
    public ResponseEntity<?> markAsRead(@PathVariable Long notificationId, @RequestParam UUID userId) {
        try {
            if (notificationId == null || notificationId <= 0) {
                return ResponseEntity.badRequest().body(Map.of("message", "Notification id must be greater than 0"));
            }

            return ResponseEntity.ok(notificationService.markAsRead(notificationId, userId));
        } catch (RuntimeException exception) {
            return ResponseEntity.badRequest().body(Map.of("message", exception.getMessage()));
        }
    }

    @PatchMapping("/user/{userId}/read-all")
    public ResponseEntity<?> markAllAsRead(@PathVariable UUID userId) {
        try {
            if (userId == null) return ResponseEntity.badRequest().body(Map.of("message", "User id is required"));

            return ResponseEntity.ok(notificationService.markAllAsRead(userId));
        } catch (RuntimeException exception) {
            return ResponseEntity.badRequest().body(Map.of("message", exception.getMessage()));
        }
    }

    @DeleteMapping("/{notificationId}")
    public ResponseEntity<?> delete(@PathVariable Long notificationId, @RequestParam UUID userId) {
        try {
            if (notificationId == null || notificationId <= 0) {
                return ResponseEntity.badRequest().body(Map.of("message", "Notification id must be greater than 0"));
            }

            notificationService.delete(notificationId, userId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException exception) {
            return ResponseEntity.badRequest().body(Map.of("message", exception.getMessage()));
        }
    }
}
