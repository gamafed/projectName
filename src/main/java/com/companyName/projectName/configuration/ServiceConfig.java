package com.companyName.projectName.configuration;

import com.companyName.projectName.login.auth.UserIdentity;
import com.companyName.projectName.login.repository.AppUserRepository;
import com.companyName.projectName.login.service.AppUserService;
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
  @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
  public ProductService productService(
      ProductRepository repository, UserIdentity userIdentity) {
    return new ProductService(repository, userIdentity);
  }

//  @Bean
//  @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
//  public AppUserService appUserService(AppUserRepository repository) {
//    return new AppUserService(repository);
//  }
}
