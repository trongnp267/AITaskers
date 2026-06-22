package com.project.util.service;

import com.project.exception.EmailException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final String from;

    public EmailService(JavaMailSender mailSender, @Value("${app.mail.from}") String from) {
        this.mailSender = mailSender;
        this.from = from;
    }

    public void send(String email, String subject, String message) {
        requireText(email, "Email address is required");
        requireText(subject, "Email subject is required");
        requireText(message, "Email message is required");
        requireText(from, "Sender email address is required");

        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setFrom(from);
        mail.setTo(email.trim());
        mail.setSubject(subject.trim());
        mail.setText(message);

        try {
            mailSender.send(mail);
        } catch (MailException exception) {
            throw new EmailException("Cannot send email: " + exception.getMessage());
        }
    }

    private void requireText(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new EmailException(message);
        }
    }
}
