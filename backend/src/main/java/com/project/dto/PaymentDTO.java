package com.project.dto;
import jakarta.validation.constraints.*; import java.math.BigDecimal; import java.util.UUID;
public class PaymentDTO {
 private UUID paymentId; @NotNull private UUID projectId; private UUID milestoneId;
 @NotNull private UUID clientProfileId; @NotNull private UUID expertProfileId;
 @NotNull @DecimalMin("0.01") private BigDecimal amount; private BigDecimal platformFee;
 private String paymentMethod; @NotBlank private String status;
 public UUID getPaymentId(){return paymentId;} public void setPaymentId(UUID v){paymentId=v;}
 public UUID getProjectId(){return projectId;} public void setProjectId(UUID v){projectId=v;}
 public UUID getMilestoneId(){return milestoneId;} public void setMilestoneId(UUID v){milestoneId=v;}
 public UUID getClientProfileId(){return clientProfileId;} public void setClientProfileId(UUID v){clientProfileId=v;}
 public UUID getExpertProfileId(){return expertProfileId;} public void setExpertProfileId(UUID v){expertProfileId=v;}
 public BigDecimal getAmount(){return amount;} public void setAmount(BigDecimal v){amount=v;}
 public BigDecimal getPlatformFee(){return platformFee;} public void setPlatformFee(BigDecimal v){platformFee=v;}
 public String getPaymentMethod(){return paymentMethod;} public void setPaymentMethod(String v){paymentMethod=v;}
 public String getStatus(){return status;} public void setStatus(String v){status=v;}
}
