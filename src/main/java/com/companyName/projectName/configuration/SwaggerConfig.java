package com.companyName.projectName.configuration;


import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import java.util.Map;
import lombok.Builder;
import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
  @Bean
  public OpenAPI openAPI() {
    Info info = new Info()
        .title("XXX API Document")
        .description("The document will list APIs we practice before.")
        .version("Ver. 1.0.0")
        .contact(new Contact().email("contact@myapi.com").name("API Support Team"))
        .license(new License().name("Apache License 2.0").url("http://www.apache.org/licenses/LICENSE-2.0.html"))
        .termsOfService("https://myapi.com/terms-of-service")
        .summary("This is a summary of my API.")
        .extensions(Map.of("x-api-key", "my-secret-key"))
        ;

    String securitySchemeName = "JWT Authentication";
    SecurityRequirement securityRequirement =
        new SecurityRequirement().addList(securitySchemeName);

    Components components = new Components()
        .addSecuritySchemes(securitySchemeName,
            new SecurityScheme()
                .name(securitySchemeName)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
        );

    return new OpenAPI().info(info)
        .addSecurityItem(securityRequirement)
        .components(components)
        ;
  }

}
