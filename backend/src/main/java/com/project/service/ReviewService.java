package com.project.service;
import com.project.dto.*; import com.project.event.ReviewCreatedEvent; import com.project.exception.ReviewException;
import com.project.model.*; import com.project.repository.*; import java.util.*;
import org.springframework.context.ApplicationEventPublisher; import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus; import org.springframework.stereotype.Service; import org.springframework.transaction.annotation.Transactional;
@Service public class ReviewService {
 private final IPaymentRepository payments; private final IReviewRepository reviews; private final IExpertProfileRepository experts; private final ApplicationEventPublisher events;
 public ReviewService(IPaymentRepository p,IReviewRepository r,IExpertProfileRepository e,ApplicationEventPublisher publisher){payments=p;reviews=r;experts=e;events=publisher;}
 @Transactional public ReviewDTO create(ReviewRequest request){
  if(request==null) throw error(HttpStatus.BAD_REQUEST,"Body khong duoc de trong.");
  Payment payment=payments.findById(request.getPaymentId()).orElseThrow(()->error(HttpStatus.NOT_FOUND,"Payment khong ton tai."));
  if(payment.getStatus()!=PaymentStatus.RELEASED) throw error(HttpStatus.FORBIDDEN,"Chi duoc review sau khi payment da RELEASED.");
  if(reviews.existsByPaymentId(payment.getId())) throw error(HttpStatus.CONFLICT,"Payment nay da duoc review.");
  Review review=new Review(payment,request.getRatingStar(),normalize(request.getReviewComment()));
  review.setDetails(request.getDeliveryQuality(),request.getCommunicationQuality(),request.getDeadlineSatisfaction(),request.getWouldHireAgain());
  try {
   Review saved=reviews.saveAndFlush(review); ExpertProfile expert=payment.getExpertProfile(); expert.addRating(saved.getRatingStar()); experts.save(expert);
   events.publishEvent(new ReviewCreatedEvent(saved.getId(),payment.getClientProfile().getAccount().getId(),expert.getAccount().getId(),saved.getRatingStar()));
   return dto(saved);
  } catch(DataIntegrityViolationException e){throw error(HttpStatus.CONFLICT,"Payment nay da duoc review.");}
 }
 @Transactional(readOnly=true) public List<ReviewDTO> findByExpertId(UUID id){if(id==null)throw error(HttpStatus.BAD_REQUEST,"Expert profile id is required");return reviews.findByExpertProfileIdOrderByCreatedAtDesc(id).stream().map(this::dto).toList();}
 private ReviewDTO dto(Review r){return new ReviewDTO(r.getId(),r.getProject().getId(),r.getPayment().getId(),r.getClientProfile().getId(),r.getExpertProfile().getId(),r.getRatingStar(),r.getReviewComment(),r.getDeliveryQuality(),r.getCommunicationQuality(),r.getDeadlineSatisfaction(),r.getWouldHireAgain(),r.getReviewStatus(),r.getCreatedAt());}
 private ReviewException error(HttpStatus s,String m){return new ReviewException(s,m);} private String normalize(String s){return s==null?null:s.trim();}
}
