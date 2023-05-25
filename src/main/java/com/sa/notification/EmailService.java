package com.sa.notification;

import com.sa.notification.models.CartItem;
import com.sa.notification.models.EmailInfo;
import com.sa.notification.models.Order;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;
    @Autowired
    private FreeMarkerConfigurationFactoryBean emailConfig;

    @Value("${spring.mail.username}")
    private String from;

    public void sendDeliveryEmail(EmailInfo emailInfo) throws MessagingException,
            IOException, TemplateException {

        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name());

        StringWriter stringWriter = new StringWriter();
        Map<String, Object> model = new HashMap<>();
        model.put("emailInfo", emailInfo);
        emailConfig.getObject().getTemplate("delivery.html")
                .process(model, stringWriter);
        String html = stringWriter.getBuffer().toString();

        mimeMessageHelper.setTo(emailInfo.getEmail());
        mimeMessageHelper.setText(html, true);
        mimeMessageHelper.setSubject("Delivery Update");
        mimeMessageHelper.setFrom(from);

        emailSender.send(message);
    }

    public void sendReceiptEmail(Order order) throws Exception {

        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name());

        StringWriter stringWriter = new StringWriter();
        Map<String, Object> model = new HashMap<>();
        model.put("order", order);
        emailConfig.getObject().getTemplate("receipt.html")
                .process(model, stringWriter);
        String html = stringWriter.getBuffer().toString();

        mimeMessageHelper.setTo(order.getUserEmail());
        mimeMessageHelper.setText(html, true);
        mimeMessageHelper.setSubject("Your Receipt");
        mimeMessageHelper.setFrom(from);

        emailSender.send(message);
    }

    private void sendTextEmail(EmailInfo emailInfo) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(emailInfo.getEmail());
        message.setSubject("Delivery Status Update");
        message.setText(emailInfo.getMessage());
        emailSender.send(message);
    }

}
