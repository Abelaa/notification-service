package com.sa.notification;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sa.notification.models.CartItem;
import com.sa.notification.models.EmailInfo;
import com.sa.notification.models.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class NotificationListener {

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private EmailService emailService;

    @Value("${spring.mail.username}")
    private String from;

    @KafkaListener(topics = {"email"})
    public void newEmail(String emailInfoAsString) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            EmailInfo emailInfo = objectMapper.readValue(emailInfoAsString,
                    EmailInfo.class);

            emailService.sendDeliveryEmail(emailInfo);

            System.out.println(emailInfo);
        } catch (Exception e) {
            System.out.println(e);
            System.out.println("Cannot convert - " + emailInfoAsString);
        }
    }

    @KafkaListener(topics = {"receipt"})
    public void receiptEmail(String orderAsAsString) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // objectMapper.readValue(orderAsAsString, Order.class);
            Order order = getOrder();

            emailService.sendReceiptEmail(order);

            System.out.println(order);
        } catch (Exception e) {
            System.out.println(e);
            System.out.println("Cannot convert - " + orderAsAsString);
        }
    }

    private Order getOrder() {
        List<CartItem> shoppingCart = new ArrayList<>();
        shoppingCart.add(new CartItem("1", "Taco", 2, 25.0));
        shoppingCart.add(new CartItem("1", "Burito", 3, 25.0));
        shoppingCart.add(new CartItem("1", "Sandwich", 1, 25.0));
        shoppingCart.add(new CartItem("1", "Burger", 2, 25.0));
        Order order = new Order(1L, "abelteferany@gmail.com", 8, 200.0,
                LocalDate.now().toString(), shoppingCart);
        return order;
    }
}
