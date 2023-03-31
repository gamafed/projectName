package com.companyName.projectName.configuration;

import com.companyName.projectName.repository.ProductRepository;
import com.companyName.projectName.service.ProductService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfig {

    @Bean
    public ProductService productService(ProductRepository repository) {
        return new ProductService(repository);
    }
}