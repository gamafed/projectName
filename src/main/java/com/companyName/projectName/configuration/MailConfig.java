package com.companyName.projectName.configuration;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Data
@PropertySource({"classpath:mail.properties"})
public class MailConfig {

    @Value("${mail.host}")
    private String host;

    @Value("${mail.port:25}") // 使用「:」符號可以加上預設值
    private int port;

    @Value("${mail.auth.enabled}")
    private boolean authEnabled;

    @Value("${mail.starttls.enabled}")
    private boolean starttlsEnabled;

    @Value("${mail.protocol}")
    private String protocol;

    @Value("${mail.username}")
    private String username;

    @Value("${mail.password}")
    private String password;

}
