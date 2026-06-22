package com.project.service;

import static org.junit.jupiter.api.Assertions.*; import static org.mockito.ArgumentMatchers.any; import static org.mockito.Mockito.*;
import com.project.dto.NotificationDTO; import com.project.exception.EmailException; import com.project.exception.NotificationException;
import com.project.model.*; import com.project.repository.INotificationRepository; import com.project.util.service.EmailService;
import java.util.*; import org.junit.jupiter.api.*; import org.junit.jupiter.api.extension.ExtendWith; import org.mockito.*; import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class) class NotificationServiceTests {
 @Mock INotificationRepository repository; @Mock UserService users; @Mock EmailService email;
 NotificationService service; UUID userId=UUID.randomUUID(); Account account;
 @BeforeEach void setup(){service=new NotificationService(repository,users,email);account=new Account();account.setEmail("expert@example.com");account.setSubscribed(true);}
 @Test void persistsInAppNotificationWhenEmailFails(){when(repository.save(any())).thenAnswer(i->i.getArgument(0));when(users.findEntity(userId)).thenReturn(account);doThrow(new EmailException("smtp down")).when(email).send(any(),any(),any());
  NotificationDTO result=service.send(dto());assertFalse(result.getSent());verify(repository,times(1)).save(any());}
 @Test void unsubscribedAccountStillReceivesInAppNotification(){when(repository.save(any())).thenAnswer(i->i.getArgument(0));account.setSubscribed(false);when(users.findEntity(userId)).thenReturn(account);
  NotificationDTO result=service.send(dto());assertFalse(result.getSent());verifyNoInteractions(email);verify(repository).save(any());}
 @Test void notificationDetailRequiresMatchingOwner(){when(repository.findByIdAndUserId(7L,userId)).thenReturn(Optional.empty());
  assertThrows(NotificationException.class,()->service.findById(7L,userId));verify(repository).findByIdAndUserId(7L,userId);}
 private NotificationDTO dto(){NotificationDTO d=new NotificationDTO();d.setUserId(userId);d.setTitle("Review moi");d.setMessage("Ban co review moi");d.setType(NotificationType.RATING_RECEIVED);return d;}
}
