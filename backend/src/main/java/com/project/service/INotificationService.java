package com.project.service;

import com.project.dto.NotificationDTO;
import com.project.model.NotificationType;
import java.util.List;
import java.util.UUID;

public interface INotificationService {

    NotificationDTO send(NotificationDTO notificationDTO);

    NotificationDTO findById(Long notificationId, UUID userId);

    List<NotificationDTO> findByUser(UUID userId);

    List<NotificationDTO> findUnreadByUser(UUID userId);

    List<NotificationDTO> findByUserAndType(UUID userId, NotificationType type);

    List<NotificationDTO> findByUserAndReferenceType(UUID userId, String referenceType);

    long countUnreadByUser(UUID userId);

    NotificationDTO markAsRead(Long notificationId, UUID userId);

    List<NotificationDTO> markAllAsRead(UUID userId);

    void delete(Long notificationId, UUID userId);
}
