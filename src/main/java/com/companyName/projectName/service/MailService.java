package com.companyName.projectName.service;

import com.companyName.projectName.configuration.MailConfig;
import com.companyName.projectName.entity.SendMailRequest;
import com.companyName.projectName.login.auth.UserIdentity;
import java.util.Collections;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.MailSender.*;
import org.springframework.mail.javamail.JavaMailSenderImpl;
//import org.springframework.mail.javamail.JavaMailSenderImpl;

import javax.annotation.PreDestroy;
import org.springframework.stereotype.Service;

@Slf4j
public class MailService {

    @Autowired
    private MailConfig mailConfig;

    private final long tag;
    private final JavaMailSenderImpl mailSender;
    private final UserIdentity userIdentity;

    public MailService(JavaMailSenderImpl mailSender, UserIdentity userIdentity){
        System.out.println("mailSender = "+mailSender);
        this.tag = System.currentTimeMillis();
        this.mailSender = mailSender;
        this.userIdentity = userIdentity;
    }

    public void sendMail(SendMailRequest request) {
        sendMail(request.getSubject(), request.getContent(), request.getReceivers());
    }

    public void sendMail(String subject, String content, List<String> receivers) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(mailSender.getUsername());
        message.setTo(receivers.toArray(new String[0]));
        message.setSubject(subject);
        message.setText(content);

        try {
            mailSender.send(message);
        } catch (MailAuthenticationException e) {
            log.error(e.getMessage());
        } catch (Exception e) {
            log.warn(e.getMessage());
        }
    }

    public void sendNewProductMail(String productId) {
        String content = String.format("Hi, %s. There's a new created product (%s).",
            userIdentity.getName(), productId);
        sendMail("New Product", content,
            Collections.singletonList(userIdentity.getEmail()));
    }

    public void sendDeleteProductMail(String productId) {
        String content = String.format("Hi, %s. There's a product deleted (%s).",
            userIdentity.getName(), productId);
        sendMail("Product Deleted", content,
            Collections.singletonList(userIdentity.getEmail()));
    }

    @PreDestroy
    public void preDestroy() {
        System.out.println("##########");
        System.out.printf("Spring Boot is about to destroy Mail Service %d.\n\n", tag);
    }
}
