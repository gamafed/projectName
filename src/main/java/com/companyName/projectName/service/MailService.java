package com.companyName.projectName.service;

import com.companyName.projectName.configuration.MailConfig;
import com.companyName.projectName.entity.SendMailRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.MailSender.*;
import org.springframework.mail.javamail.JavaMailSenderImpl;
//import org.springframework.mail.javamail.JavaMailSenderImpl;

import javax.annotation.PreDestroy;

@Slf4j
public class MailService {

    @Autowired
    private MailConfig mailConfig;

    private final long tag;
    private final JavaMailSenderImpl mailSender;

    public MailService(JavaMailSenderImpl mailSender){
        System.out.println("mailSender = "+mailSender);
        this.tag = System.currentTimeMillis();
        this.mailSender = mailSender;
    }

    public void sendMail(SendMailRequest request) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(mailConfig.getUsername());
        message.setTo(request.getReceivers());
        message.setSubject(request.getSubject());
        message.setText(request.getContent());

        try {
            mailSender.send(message);
        } catch (MailAuthenticationException e) {
            log.error(e.getMessage());
        } catch (Exception e) {
            log.warn(e.getMessage());
        }
    }

    @PreDestroy
    public void preDestroy() {
        System.out.println("##########");
        System.out.printf("Spring Boot is about to destroy Mail Service %d.\n\n", tag);
    }


}
