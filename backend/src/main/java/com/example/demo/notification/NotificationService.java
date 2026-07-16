package com.example.demo.notification;

import com.example.demo.entity.User;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final NotificationPreferenceRepository preferenceRepository;
    private final UserRepository userRepository;

    public NotificationService(
            NotificationRepository notificationRepository,
            NotificationPreferenceRepository preferenceRepository,
            UserRepository userRepository
    ) {
        this.notificationRepository = notificationRepository;
        this.preferenceRepository = preferenceRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<NotificationResponse> getNotifications(String username, NotificationType type) {
        User recipient = getUser(username);
        List<Notification> notifications = type == null
                ? notificationRepository.findByRecipientAndHiddenFalseOrderByCreatedAtDesc(recipient)
                : notificationRepository.findByRecipientAndTypeAndHiddenFalseOrderByCreatedAtDesc(recipient, type);

        return notifications.stream()
                .map(NotificationResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public long countUnread(String username) {
        return notificationRepository.countByRecipientAndHiddenFalseAndReadStatusFalse(getUser(username));
    }

    @Transactional
    public NotificationResponse createForCurrentUser(String username, CreateNotificationRequest request) {
        User recipient = getUser(username);
        return NotificationResponse.from(createNotification(
                recipient,
                request.type(),
                request.title(),
                request.message(),
                request.targetUrl()
        ));
    }

    @Transactional
    public Notification createNotification(
            User recipient,
            NotificationType type,
            String title,
            String message,
            String targetUrl
    ) {
        Notification notification = new Notification();
        notification.setRecipient(recipient);
        notification.setType(type);
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setTargetUrl(targetUrl);
        return notificationRepository.save(notification);
    }

    @Transactional
    public NotificationResponse markAsRead(String username, Long notificationId) {
        Notification notification = getVisibleNotification(username, notificationId);
        if (!notification.isReadStatus()) {
            notification.setReadStatus(true);
            notification.setReadAt(LocalDateTime.now());
        }
        return NotificationResponse.from(notification);
    }

    @Transactional
    public void markAllAsRead(String username) {
        User recipient = getUser(username);
        LocalDateTime now = LocalDateTime.now();
        notificationRepository.findByRecipientAndHiddenFalseAndReadStatusFalse(recipient)
                .forEach(notification -> {
                    notification.setReadStatus(true);
                    notification.setReadAt(now);
                });
    }

    @Transactional
    public void hideNotification(String username, Long notificationId) {
        Notification notification = getVisibleNotification(username, notificationId);
        notification.setHidden(true);
    }

    @Transactional
    public List<NotificationPreferenceResponse> getPreferences(String username) {
        User recipient = getUser(username);
        ensureDefaultPreferences(recipient);
        return preferenceRepository.findByRecipientOrderByTypeAsc(recipient).stream()
                .map(NotificationPreferenceResponse::from)
                .toList();
    }

    @Transactional
    public List<NotificationPreferenceResponse> updatePreferences(
            String username,
            UpdateNotificationPreferencesRequest request
    ) {
        User recipient = getUser(username);
        ensureDefaultPreferences(recipient);

        request.preferences().forEach(item -> {
            NotificationDeliveryPreference preference = preferenceRepository
                    .findByRecipientAndType(recipient, item.type())
                    .orElseGet(() -> createDefaultPreference(recipient, item.type()));

            preference.setWebEnabled(item.webEnabled());
            preference.setEmailEnabled(item.emailEnabled());
            preferenceRepository.save(preference);
        });

        return preferenceRepository.findByRecipientOrderByTypeAsc(recipient).stream()
                .map(NotificationPreferenceResponse::from)
                .toList();
    }

    private Notification getVisibleNotification(String username, Long notificationId) {
        User recipient = getUser(username);
        return notificationRepository.findByIdAndRecipientAndHiddenFalse(notificationId, recipient)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found"));
    }

    private User getUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private void ensureDefaultPreferences(User recipient) {
        Arrays.stream(NotificationType.values()).forEach(type ->
                preferenceRepository.findByRecipientAndType(recipient, type)
                        .orElseGet(() -> preferenceRepository.save(createDefaultPreference(recipient, type)))
        );
    }

    private NotificationDeliveryPreference createDefaultPreference(User recipient, NotificationType type) {
        NotificationDeliveryPreference preference = new NotificationDeliveryPreference();
        preference.setRecipient(recipient);
        preference.setType(type);
        preference.setWebEnabled(true);
        preference.setEmailEnabled(false);
        return preference;
    }
}
