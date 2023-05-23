package com.sa.notification;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class NotificationListener {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    private void sendEmail(EmailInfo emailInfo) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(emailInfo.getEmail());
        message.setSubject("Delivery Status Update");
        message.setText(emailInfo.getMessage());
        mailSender.send(message);
    }

    @KafkaListener(topics = {"email"})
    public void newEmail(String emailInfoAsString) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            EmailInfo emailInfo = objectMapper.readValue(emailInfoAsString,
                    EmailInfo.class);

            sendEmail(emailInfo);

            System.out.println(emailInfo);
        } catch (Exception e) {
            System.out.println(e);
            System.out.println("Cannot convert - " + emailInfoAsString);
        }
    }

}
