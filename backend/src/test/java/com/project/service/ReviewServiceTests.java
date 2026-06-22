package com.project.service;

import static org.junit.jupiter.api.Assertions.*; import static org.mockito.ArgumentMatchers.any; import static org.mockito.Mockito.*;
import com.project.dto.*; import com.project.event.ReviewCreatedEvent; import com.project.exception.ReviewException;
import com.project.model.*; import com.project.repository.*; import java.util.*;
import org.junit.jupiter.api.*; import org.junit.jupiter.api.extension.ExtendWith; import org.mockito.*;
import org.springframework.context.ApplicationEventPublisher; import org.springframework.http.HttpStatus; import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class) class ReviewServiceTests {
 @Mock IPaymentRepository payments; @Mock IReviewRepository reviews; @Mock IExpertProfileRepository experts; @Mock ApplicationEventPublisher events;
 @Mock Payment payment; @Mock Project project; @Mock ClientProfile client; @Mock ExpertProfile expert; @Mock Account clientAccount; @Mock Account expertAccount;
 ReviewService service; UUID paymentId=UUID.randomUUID();
 @BeforeEach void setup(){service=new ReviewService(payments,reviews,experts,events);}
 @Test void createsReviewUpdatesRatingAndPublishesEventWhenReleased(){
  when(payments.findById(paymentId)).thenReturn(Optional.of(payment)); when(payment.getId()).thenReturn(paymentId); when(payment.getStatus()).thenReturn(PaymentStatus.RELEASED);
  when(payment.getProject()).thenReturn(project); when(payment.getClientProfile()).thenReturn(client); when(payment.getExpertProfile()).thenReturn(expert);
  when(client.getAccount()).thenReturn(clientAccount); when(expert.getAccount()).thenReturn(expertAccount); when(clientAccount.getId()).thenReturn(UUID.randomUUID()); when(expertAccount.getId()).thenReturn(UUID.randomUUID());
  when(reviews.existsByPaymentId(paymentId)).thenReturn(false); when(reviews.saveAndFlush(any())).thenAnswer(i->i.getArgument(0));
  ReviewDTO result=service.create(request()); assertEquals(5,result.ratingStar()); verify(expert).addRating(5); verify(events).publishEvent(any(ReviewCreatedEvent.class));
 }
 @Test void rejectsReviewUntilPaymentIsReleased(){when(payments.findById(paymentId)).thenReturn(Optional.of(payment));when(payment.getStatus()).thenReturn(PaymentStatus.HELD);
  ReviewException e=assertThrows(ReviewException.class,()->service.create(request()));assertEquals(HttpStatus.FORBIDDEN,e.getStatus());}
 private ReviewRequest request(){ReviewRequest r=new ReviewRequest();r.setPaymentId(paymentId);r.setRatingStar(5);r.setReviewComment("Expert lam viec tot");return r;}
}
