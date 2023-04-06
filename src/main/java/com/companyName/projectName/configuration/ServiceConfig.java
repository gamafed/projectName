package com.companyName.projectName.configuration;

import com.companyName.projectName.repository.ProductRepository;
import com.companyName.projectName.service.MailService;
import com.companyName.projectName.service.ProductService;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class ServiceConfig {

    @Bean
    public ProductService productService(ProductRepository repository, MailService mailService) {
        System.out.println("Product Service is created.");
        return new ProductService(repository, mailService);
    }

}