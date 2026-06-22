package com.project.repository;

import com.project.model.Notification;
import com.project.model.NotificationType;
import java.util.List;
import java.util.UUID;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface INotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByUserIdOrderByCreatedAtDesc(UUID userId);

    List<Notification> findByUserIdAndReadFalseOrderByCreatedAtDesc(UUID userId);

    List<Notification> findByUserIdAndTypeOrderByCreatedAtDesc(UUID userId, NotificationType type);

    List<Notification> findByUserIdAndReferenceTypeIgnoreCaseOrderByCreatedAtDesc(UUID userId, String referenceType);

    long countByUserIdAndReadFalse(UUID userId);

    Optional<Notification> findByIdAndUserId(Long id, UUID userId);
}
