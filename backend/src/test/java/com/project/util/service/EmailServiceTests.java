package com.project.util.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;

import com.project.exception.EmailException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@ExtendWith(MockitoExtension.class)
class EmailServiceTests {

    @Mock
    private JavaMailSender mailSender;

    private EmailService emailService;

    @BeforeEach
    void setUp() {
        emailService = new EmailService(mailSender, "no-reply@aitaskers.local");
    }

    @Test
    void sendsEmailThroughConfiguredMailSender() {
        emailService.send("expert@example.com", "New review", "You received five stars.");

        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender).send(captor.capture());
        SimpleMailMessage mail = captor.getValue();
        org.junit.jupiter.api.Assertions.assertEquals("no-reply@aitaskers.local", mail.getFrom());
        org.junit.jupiter.api.Assertions.assertArrayEquals(
                new String[]{"expert@example.com"}, mail.getTo());
        org.junit.jupiter.api.Assertions.assertEquals("New review", mail.getSubject());
        org.junit.jupiter.api.Assertions.assertEquals("You received five stars.", mail.getText());
    }

    @Test
    void rejectsBlankRecipientBeforeSending() {
        assertThrows(EmailException.class, () -> emailService.send(" ", "Subject", "Message"));
    }
}
