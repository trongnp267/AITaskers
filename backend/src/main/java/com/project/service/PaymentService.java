package com.project.service;
import com.project.dto.PaymentDTO; import com.project.model.*; import com.project.repository.*; import java.util.UUID;
import org.springframework.stereotype.Service; import org.springframework.transaction.annotation.Transactional;
@Service public class PaymentService {
 private final IPaymentRepository payments; private final IProjectRepository projects; private final IProjectMilestoneRepository milestones;
 private final IClientProfileRepository clients; private final IExpertProfileRepository experts;
 public PaymentService(IPaymentRepository p,IProjectRepository pr,IProjectMilestoneRepository m,IClientProfileRepository c,IExpertProfileRepository e){payments=p;projects=pr;milestones=m;clients=c;experts=e;}
 @Transactional public Payment save(PaymentDTO d){if(d==null)throw new IllegalArgumentException("Payment data is required"); PaymentStatus status=PaymentStatus.from(d.getStatus());
  if(d.getPaymentId()!=null){Payment existing=findById(d.getPaymentId());existing.setStatus(status);existing.setPlatformFee(d.getPlatformFee());existing.setPaymentMethod(d.getPaymentMethod());return payments.save(existing);}
  Project project=projects.findById(d.getProjectId()).orElseThrow(()->new IllegalArgumentException("Project not found"));
  ClientProfile client=clients.findById(d.getClientProfileId()).orElseThrow(()->new IllegalArgumentException("Client profile not found"));
  ExpertProfile expert=experts.findById(d.getExpertProfileId()).orElseThrow(()->new IllegalArgumentException("Expert profile not found"));
  if(!project.getClientProfile().getId().equals(client.getId())||!project.getExpertProfile().getId().equals(expert.getId()))throw new IllegalArgumentException("Payment profiles do not belong to project");
  ProjectMilestone milestone=d.getMilestoneId()==null?null:milestones.findById(d.getMilestoneId()).orElseThrow(()->new IllegalArgumentException("Milestone not found"));
  if(milestone!=null&&!milestone.getProject().getId().equals(project.getId()))throw new IllegalArgumentException("Milestone does not belong to project");
  Payment payment=new Payment(project,milestone,client,expert,d.getAmount(),status); payment.setPlatformFee(d.getPlatformFee());payment.setPaymentMethod(d.getPaymentMethod());return payments.save(payment);}
 public Payment findById(UUID id){if(id==null)throw new IllegalArgumentException("Payment id is required");return payments.findById(id).orElseThrow(()->new IllegalArgumentException("Payment not found: "+id));}
}
