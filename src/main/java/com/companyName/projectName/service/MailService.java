package com.companyName.projectName.service;

import com.companyName.projectName.configuration.MailConfig;
import com.companyName.projectName.entity.SendMailRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Properties;

@Service
@Slf4j
public class MailService {

    @Autowired
    private MailConfig mailConfig;

    private JavaMailSenderImpl mailSender;

    @PostConstruct
    private void init() {
        mailSender = new JavaMailSenderImpl();
        mailSender.setHost(mailConfig.getHost());
        mailSender.setPort(mailConfig.getPort());
        mailSender.setUsername(mailConfig.getUsername());
        mailSender.setPassword(mailConfig.getPassword());

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.smtp.auth", mailConfig.isAuthEnabled());
        props.put("mail.smtp.starttls.enable", mailConfig.isStarttlsEnabled());
        props.put("mail.transport.protocol", mailConfig.getProtocol());
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


}
