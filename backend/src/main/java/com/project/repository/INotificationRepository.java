package com.project.repository;

import com.project.model.Notification;
import com.project.model.NotificationType;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface INotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByUserIdOrderByCreatedAtDesc(Long userId);

    List<Notification> findByUserIdAndReadFalseOrderByCreatedAtDesc(Long userId);

    List<Notification> findByUserIdAndTypeOrderByCreatedAtDesc(Long userId, NotificationType type);

    List<Notification> findByUserIdAndReferenceTypeIgnoreCaseOrderByCreatedAtDesc(Long userId, String referenceType);

    long countByUserIdAndReadFalse(Long userId);
}
