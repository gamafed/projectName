package com.companyName.projectName.controller;

import com.companyName.projectName.configuration.MailConfig;
import com.companyName.projectName.entity.SendMailRequest;
import com.companyName.projectName.service.MailService;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Hidden
@RestController
@RequestMapping(value = "/mail", produces = MediaType.APPLICATION_JSON_VALUE)
public class MailController {

    @Autowired
    private MailService mailService;

    @PostMapping
    public ResponseEntity<Void> sendMail(@Valid @RequestBody SendMailRequest request) {
        mailService.sendMail(request);
        return ResponseEntity.noContent().build();
    }

}
