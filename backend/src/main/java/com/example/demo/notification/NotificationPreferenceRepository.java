package com.example.demo.notification;

import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationPreferenceRepository extends JpaRepository<NotificationDeliveryPreference, Long> {
    List<NotificationDeliveryPreference> findByRecipientOrderByTypeAsc(User recipient);

    Optional<NotificationDeliveryPreference> findByRecipientAndType(User recipient, NotificationType type);
}
