package com.cashflow.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI myOpenAPI() {
        Contact contact = new Contact();
        contact.setEmail("hernandes_sp@yahoo.com.br");
        contact.setName("Thiago Hernandes de Souza");
        contact.setUrl("https://github.com/thiagohernandes");

        License mitLicense = new License()
                .name("MIT License")
                .url("https://opensource.org/license/mit/");

        Info info = new Info()
                .title("Cash Flow API")
                .version("1.0")
                .contact(contact)
                .description("Merchant's Cash Flow")
                .license(mitLicense);

        return new OpenAPI().info(info);
    }
}