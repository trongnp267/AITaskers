package com.project.service;

import com.project.dto.NotificationDTO;
import com.project.exception.BaseException;
import com.project.exception.NotificationException;
import com.project.model.Notification;
import com.project.model.NotificationType;
import com.project.model.User;
import com.project.repository.INotificationRepository;
import com.project.util.service.EmailService;
import java.time.LocalDateTime;
import java.util.List;
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

            User user = userService.findEntity(notificationDTO.getUserId());
            if (Boolean.FALSE.equals(user.getSubscribed())) {
                throw new NotificationException("User is not subscribed");
            }

            if (notificationDTO.getType() == null) {
                notificationDTO.setType(NotificationType.SYSTEM_ANNOUNCEMENT);
            }
            validateReference(notificationDTO);

            Notification notification = toEntity(notificationDTO);
            notification.setId(null);
            notification.setRead(false);
            notification.setReadAt(null);

            if (hasText(user.getEmail())) {
                emailService.send(user.getEmail(), notificationDTO.getTitle(), notificationDTO.getMessage());
            }

            notification.setSent(true);
            return toDTO(notificationRepository.save(notification));
        } catch (BaseException exception) {
            throw exception;
        } catch (Exception exception) {
            throw new NotificationException("Cannot send notification: " + exception.getMessage());
        }
    }

    @Override
    public List<NotificationDTO> findByUser(Long userId) {
        try {
            if (userId == null || userId <= 0) {
                throw new NotificationException("User id must be greater than 0");
            }

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
    public NotificationDTO findById(Long notificationId) {
        try {
            if (notificationId == null || notificationId <= 0) {
                throw new NotificationException("Notification id must be greater than 0");
            }

            Notification notification = notificationRepository.findById(notificationId)
                    .orElseThrow(() -> new NotificationException("Notification not found: " + notificationId));
            return toDTO(notification);
        } catch (BaseException exception) {
            throw exception;
        } catch (Exception exception) {
            throw new NotificationException("Cannot get notification detail: " + exception.getMessage());
        }
    }

    @Override
    public List<NotificationDTO> findUnreadByUser(Long userId) {
        try {
            if (userId == null || userId <= 0) {
                throw new NotificationException("User id must be greater than 0");
            }

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
    public List<NotificationDTO> findByUserAndType(Long userId, NotificationType type) {
        try {
            if (userId == null || userId <= 0) {
                throw new NotificationException("User id must be greater than 0");
            }
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
    public List<NotificationDTO> findByUserAndReferenceType(Long userId, String referenceType) {
        try {
            if (userId == null || userId <= 0) {
                throw new NotificationException("User id must be greater than 0");
            }
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
    public long countUnreadByUser(Long userId) {
        try {
            if (userId == null || userId <= 0) {
                throw new NotificationException("User id must be greater than 0");
            }

            return notificationRepository.countByUserIdAndReadFalse(userId);
        } catch (BaseException exception) {
            throw exception;
        } catch (Exception exception) {
            throw new NotificationException("Cannot count unread notifications: " + exception.getMessage());
        }
    }

    @Override
    public NotificationDTO markAsRead(Long notificationId) {
        try {
            if (notificationId == null || notificationId <= 0) {
                throw new NotificationException("Notification id must be greater than 0");
            }

            Notification notification = notificationRepository.findById(notificationId)
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
    public List<NotificationDTO> markAllAsRead(Long userId) {
        try {
            if (userId == null || userId <= 0) {
                throw new NotificationException("User id must be greater than 0");
            }

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
    public void delete(Long notificationId) {
        try {
            if (notificationId == null || notificationId <= 0) {
                throw new NotificationException("Notification id must be greater than 0");
            }
            if (!notificationRepository.existsById(notificationId)) {
                throw new NotificationException("Notification not found: " + notificationId);
            }

            notificationRepository.deleteById(notificationId);
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
