package com.project.dto;
import jakarta.validation.constraints.*; import java.util.UUID;
public class ReviewRequest {
 @NotNull private UUID paymentId;
 @NotNull @Min(1) @Max(5) private Integer ratingStar;
 private String reviewComment;
 @Min(1) @Max(5) private Integer deliveryQuality;
 @Min(1) @Max(5) private Integer communicationQuality;
 @Min(1) @Max(5) private Integer deadlineSatisfaction;
 private Boolean wouldHireAgain;
 public UUID getPaymentId(){return paymentId;} public void setPaymentId(UUID v){paymentId=v;}
 public Integer getRatingStar(){return ratingStar;} public void setRatingStar(Integer v){ratingStar=v;}
 public String getReviewComment(){return reviewComment;} public void setReviewComment(String v){reviewComment=v;}
 public Integer getDeliveryQuality(){return deliveryQuality;} public void setDeliveryQuality(Integer v){deliveryQuality=v;}
 public Integer getCommunicationQuality(){return communicationQuality;} public void setCommunicationQuality(Integer v){communicationQuality=v;}
 public Integer getDeadlineSatisfaction(){return deadlineSatisfaction;} public void setDeadlineSatisfaction(Integer v){deadlineSatisfaction=v;}
 public Boolean getWouldHireAgain(){return wouldHireAgain;} public void setWouldHireAgain(Boolean v){wouldHireAgain=v;}
}
