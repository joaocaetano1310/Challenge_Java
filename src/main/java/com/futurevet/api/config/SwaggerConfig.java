package com.futurevet.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("FutureVet API")
                        .version("1.0.0")
                        .description("API REST para gerenciamento veterinário do FutureVet. " +
                                "Permite cadastro de usuários, pets, vacinas e consultas.")
                        .contact(new Contact()
                                .name("Equipe FutureVet")
                                .email("contato@futurevet.com")));
    }
}
