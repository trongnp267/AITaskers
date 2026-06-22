package com.project.service;

import com.project.dto.NotificationDTO;
import com.project.exception.BaseException;
import com.project.exception.NotificationException;
import com.project.exception.EmailException;
import com.project.model.Notification;
import com.project.model.NotificationType;
import com.project.model.Account;
import com.project.repository.INotificationRepository;
import com.project.util.service.EmailService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class NotificationService implements INotificationService {

    private final INotificationRepository notificationRepository;
    private final UserService userService;
    private final EmailService emailService;

    public NotificationService(INotificationRepository notificationRepository, UserService userService,
            EmailService emailService) {
        this.notificationRepository = notificationRepository;
        this.userService = userService;
        this.emailService = emailService;
    }

    @Override
    public NotificationDTO send(NotificationDTO notificationDTO) {
        try {
            if (notificationDTO == null) {
                throw new NotificationException("Notification data is required");
            }

            Account user = userService.findEntity(notificationDTO.getUserId());
            if (notificationDTO.getType() == null) {
                notificationDTO.setType(NotificationType.SYSTEM_ANNOUNCEMENT);
            }
            validateReference(notificationDTO);

            Notification notification = toEntity(notificationDTO);
            notification.setId(null);
            notification.setRead(false);
            notification.setReadAt(null);

            notification.setSent(false);
            Notification saved = notificationRepository.save(notification);

            if (Boolean.TRUE.equals(user.getSubscribed()) && hasText(user.getEmail())) {
                try {
                    emailService.send(user.getEmail(), notificationDTO.getTitle(), notificationDTO.getMessage());
                    saved.setSent(true);
                    saved = notificationRepository.save(saved);
                } catch (EmailException exception) {
                    // In-app notification is already persisted. Email can be retried separately.
                }
            }
            return toDTO(saved);
        } catch (BaseException exception) {
            throw exception;
        } catch (Exception exception) {
            throw new NotificationException("Cannot send notification: " + exception.getMessage());
        }
    }

    @Override
    public List<NotificationDTO> findByUser(UUID userId) {
        try {
            if (userId == null) throw new NotificationException("User id is required");

            return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                    .map(this::toDTO)
                    .toList();
        } catch (BaseException exception) {
            throw exception;
        } catch (Exception exception) {
            throw new NotificationException("Cannot get notifications: " + exception.getMessage());
        }
    }

    @Override
    public NotificationDTO findById(Long notificationId, UUID userId) {
        try {
            if (notificationId == null || notificationId <= 0) {
                throw new NotificationException("Notification id must be greater than 0");
            }
            if (userId == null) throw new NotificationException("User id is required");

            Notification notification = notificationRepository.findByIdAndUserId(notificationId, userId)
                    .orElseThrow(() -> new NotificationException("Notification not found: " + notificationId));
            return toDTO(notification);
        } catch (BaseException exception) {
            throw exception;
        } catch (Exception exception) {
            throw new NotificationException("Cannot get notification detail: " + exception.getMessage());
        }
    }

    @Override
    public List<NotificationDTO> findUnreadByUser(UUID userId) {
        try {
            if (userId == null) throw new NotificationException("User id is required");

            return notificationRepository.findByUserIdAndReadFalseOrderByCreatedAtDesc(userId).stream()
                    .map(this::toDTO)
                    .toList();
        } catch (BaseException exception) {
            throw exception;
        } catch (Exception exception) {
            throw new NotificationException("Cannot get unread notifications: " + exception.getMessage());
        }
    }

    @Override
    public List<NotificationDTO> findByUserAndType(UUID userId, NotificationType type) {
        try {
            if (userId == null) throw new NotificationException("User id is required");
            if (type == null) {
                throw new NotificationException("Notification type is required");
            }

            return notificationRepository.findByUserIdAndTypeOrderByCreatedAtDesc(userId, type).stream()
                    .map(this::toDTO)
                    .toList();
        } catch (BaseException exception) {
            throw exception;
        } catch (Exception exception) {
            throw new NotificationException("Cannot get notifications by type: " + exception.getMessage());
        }
    }

    @Override
    public List<NotificationDTO> findByUserAndReferenceType(UUID userId, String referenceType) {
        try {
            if (userId == null) throw new NotificationException("User id is required");
            if (!hasText(referenceType)) {
                throw new NotificationException("Reference type is required");
            }

            return notificationRepository.findByUserIdAndReferenceTypeIgnoreCaseOrderByCreatedAtDesc(userId, referenceType).stream()
                    .map(this::toDTO)
                    .toList();
        } catch (BaseException exception) {
            throw exception;
        } catch (Exception exception) {
            throw new NotificationException("Cannot get notifications by reference type: " + exception.getMessage());
        }
    }

    @Override
    public long countUnreadByUser(UUID userId) {
        try {
            if (userId == null) throw new NotificationException("User id is required");

            return notificationRepository.countByUserIdAndReadFalse(userId);
        } catch (BaseException exception) {
            throw exception;
        } catch (Exception exception) {
            throw new NotificationException("Cannot count unread notifications: " + exception.getMessage());
        }
    }

    @Override
    public NotificationDTO markAsRead(Long notificationId, UUID userId) {
        try {
            if (notificationId == null || notificationId <= 0) {
                throw new NotificationException("Notification id must be greater than 0");
            }
            if (userId == null) throw new NotificationException("User id is required");

            Notification notification = notificationRepository.findByIdAndUserId(notificationId, userId)
                    .orElseThrow(() -> new NotificationException("Notification not found: " + notificationId));
            notification.setRead(true);
            notification.setReadAt(LocalDateTime.now());
            return toDTO(notificationRepository.save(notification));
        } catch (BaseException exception) {
            throw exception;
        } catch (Exception exception) {
            throw new NotificationException("Cannot mark notification as read: " + exception.getMessage());
        }
    }

    @Override
    public List<NotificationDTO> markAllAsRead(UUID userId) {
        try {
            if (userId == null) throw new NotificationException("User id is required");

            List<Notification> notifications = notificationRepository.findByUserIdAndReadFalseOrderByCreatedAtDesc(userId);
            LocalDateTime now = LocalDateTime.now();
            notifications.forEach(notification -> {
                notification.setRead(true);
                notification.setReadAt(now);
            });
            return notificationRepository.saveAll(notifications).stream()
                    .map(this::toDTO)
                    .toList();
        } catch (BaseException exception) {
            throw exception;
        } catch (Exception exception) {
            throw new NotificationException("Cannot mark all notifications as read: " + exception.getMessage());
        }
    }

    @Override
    public void delete(Long notificationId, UUID userId) {
        try {
            if (notificationId == null || notificationId <= 0) {
                throw new NotificationException("Notification id must be greater than 0");
            }
            if (userId == null) throw new NotificationException("User id is required");
            Notification notification = notificationRepository.findByIdAndUserId(notificationId, userId)
                    .orElseThrow(() -> new NotificationException("Notification not found: " + notificationId));
            notificationRepository.delete(notification);
        } catch (BaseException exception) {
            throw exception;
        } catch (Exception exception) {
            throw new NotificationException("Cannot delete notification: " + exception.getMessage());
        }
    }

    private NotificationDTO toDTO(Notification notification) {
        if (notification == null) {
            throw new NotificationException("Notification is required");
        }

        NotificationDTO dto = new NotificationDTO();
        dto.setId(notification.getId());
        dto.setUserId(notification.getUserId());
        dto.setTitle(notification.getTitle());
        dto.setMessage(notification.getMessage());
        dto.setType(notification.getType());
        dto.setReferenceType(notification.getReferenceType());
        dto.setReferenceId(notification.getReferenceId());
        dto.setSent(notification.getSent());
        dto.setRead(notification.getRead());
        dto.setCreatedAt(notification.getCreatedAt());
        dto.setReadAt(notification.getReadAt());
        return dto;
    }

    private Notification toEntity(NotificationDTO dto) {
        if (dto == null) {
            throw new NotificationException("Notification data is required");
        }

        Notification notification = new Notification();
        notification.setId(dto.getId());
        notification.setUserId(dto.getUserId());
        notification.setTitle(dto.getTitle());
        notification.setMessage(dto.getMessage());
        notification.setType(dto.getType());
        notification.setReferenceType(dto.getReferenceType());
        notification.setReferenceId(dto.getReferenceId());
        notification.setSent(dto.getSent());
        notification.setRead(dto.getRead());
        notification.setReadAt(dto.getReadAt());
        return notification;
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }

    private void validateReference(NotificationDTO notificationDTO) {
        if (hasText(notificationDTO.getReferenceType()) && notificationDTO.getReferenceId() == null) {
            throw new NotificationException("Reference id is required when reference type is provided");
        }
        if (!hasText(notificationDTO.getReferenceType()) && notificationDTO.getReferenceId() != null) {
            throw new NotificationException("Reference type is required when reference id is provided");
        }
    }
}
