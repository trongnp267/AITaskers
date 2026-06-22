package com.project.service;

import com.project.dto.NotificationDTO;
import com.project.model.NotificationType;
import java.util.List;

public interface INotificationService {

    NotificationDTO send(NotificationDTO notificationDTO);

    NotificationDTO findById(Long notificationId);

    List<NotificationDTO> findByUser(Long userId);

    List<NotificationDTO> findUnreadByUser(Long userId);

    List<NotificationDTO> findByUserAndType(Long userId, NotificationType type);

    List<NotificationDTO> findByUserAndReferenceType(Long userId, String referenceType);

    long countUnreadByUser(Long userId);

    NotificationDTO markAsRead(Long notificationId);

    List<NotificationDTO> markAllAsRead(Long userId);

    void delete(Long notificationId);
}
