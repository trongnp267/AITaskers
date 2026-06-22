package com.project.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.project.dto.ReviewDTO;
import com.project.dto.ReviewRequest;
import com.project.event.ReviewCreatedEvent;
import com.project.exception.ReviewException;
import com.project.model.Payment;
import com.project.model.PaymentStatus;
import com.project.model.Review;
import com.project.repository.IPaymentRepository;
import com.project.repository.IReviewRepository;
import com.project.repository.IUserRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTests {

    @Mock
    private IPaymentRepository paymentRepository;

    @Mock
    private IReviewRepository reviewRepository;

    @Mock
    private IUserRepository userRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    private ReviewService reviewService;

    @BeforeEach
    void setUp() {
        reviewService = new ReviewService(
                paymentRepository, reviewRepository, userRepository, eventPublisher);
    }

    @Test
    void createsReviewAndPublishesEventWhenPaymentIsPaid() {
        ReviewRequest request = validRequest();
        when(paymentRepository.findById("pay_001"))
                .thenReturn(Optional.of(new Payment("pay_001", PaymentStatus.PAID)));
        when(userRepository.existsById(1L)).thenReturn(true);
        when(userRepository.existsById(2L)).thenReturn(true);
        when(reviewRepository.existsByPaymentIdAndClientIdAndExpertId("pay_001", 1L, 2L))
                .thenReturn(false);
        when(reviewRepository.saveAndFlush(any(Review.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ReviewDTO result = reviewService.create(request);

        assertEquals("pay_001", result.paymentId());
        assertEquals(5, result.score());
        verify(eventPublisher).publishEvent(any(ReviewCreatedEvent.class));
    }

    @Test
    void rejectsReviewWhenPaymentIsPending() {
        ReviewRequest request = validRequest();
        when(paymentRepository.findById("pay_001"))
                .thenReturn(Optional.of(new Payment("pay_001", PaymentStatus.PENDING)));

        ReviewException exception = assertThrows(
                ReviewException.class, () -> reviewService.create(request));

        assertEquals(HttpStatus.FORBIDDEN, exception.getStatus());
    }

    private ReviewRequest validRequest() {
        ReviewRequest request = new ReviewRequest();
        request.setPaymentId("pay_001");
        request.setJobId("job_001");
        request.setClientId(1L);
        request.setExpertId(2L);
        request.setScore(5);
        request.setComment("Expert lam viec tot");
        return request;
    }
}
