package com.project.event;

import com.project.dto.NotificationDTO;
import com.project.model.NotificationType;
import com.project.service.INotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class ReviewNotificationListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReviewNotificationListener.class);

    private final INotificationService notificationService;

    public ReviewNotificationListener(INotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onReviewCreated(ReviewCreatedEvent event) {
        NotificationDTO notification = new NotificationDTO();
        notification.setUserId(event.expertAccountId());
        notification.setTitle("Ban co review moi");
        notification.setMessage("Ban vua nhan duoc review " + event.score() + " sao tu client.");
        notification.setType(NotificationType.RATING_RECEIVED);
        notification.setReferenceType("REVIEW");
        notification.setReferenceId(event.reviewId());

        try {
            notificationService.send(notification);
        } catch (RuntimeException exception) {
            LOGGER.error("Review {} was saved but its notification could not be created",
                    event.reviewId(), exception);
        }
    }
}
