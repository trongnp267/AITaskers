package com.example.demo.notification;

import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByRecipientAndHiddenFalseOrderByCreatedAtDesc(User recipient);

    List<Notification> findByRecipientAndTypeAndHiddenFalseOrderByCreatedAtDesc(
            User recipient,
            NotificationType type
    );

    List<Notification> findByRecipientAndHiddenFalseAndReadStatusFalse(User recipient);

    long countByRecipientAndHiddenFalseAndReadStatusFalse(User recipient);

    Optional<Notification> findByIdAndRecipientAndHiddenFalse(Long id, User recipient);
}
