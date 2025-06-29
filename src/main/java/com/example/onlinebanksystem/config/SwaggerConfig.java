package com.example.onlinebanksystem.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Online Bank System API").version("1.0").description("Onlayn Bank Sistemi üçün API Sənədləşdirməsi"))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth")) // Hər bir qorunan endpoint üçün bu tələbi əlavə edir
                .components(new Components()
                        .addSecuritySchemes("bearerAuth", new SecurityScheme() // "bearerAuth" adını "Authorize" pəncərəsində görəcəksiniz
                                .name("Authorization") // Başlığın adı
                                .type(SecurityScheme.Type.HTTP) // HTTP əsaslı təhlükəsizlik sxemi
                                .scheme("bearer") // Bearer token istifadə edirik
                                .bearerFormat("JWT") // Format JWT-dir
                                .description("JWT Autentifikasiyası. Tokeni 'Bearer {token}' formatında daxil edin.")));
    }
}