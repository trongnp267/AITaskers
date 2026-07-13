package com.aitasker.backend.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    private static final String SCHEME_NAME = "bearerAuth";

    @Bean
    public OpenAPI customOpenAPI() {
        // TRUOC DAY: JobController/ProposalController co @SecurityRequirement(name
        // = "bearerAuth") nhung khong noi nao dinh nghia scheme "bearerAuth" nay
        // ca -> Swagger UI khong biet phai hien nut Authorize the nao, nen no
        // KHONG HIEN nut Authorize luon. Dang ky scheme JWT Bearer o day de
        // Swagger UI tu hien nut khoa Authorize va tu dinh kem token vao header
        // "Authorization: Bearer <token>" cho moi request.
        return new OpenAPI()
                .info(new Info()
                        .title("AITasker API Documentation")
                        .version("1.0")
                        .description("Tài liệu API cho dự án AITasker"))
                .components(new Components()
                        .addSecuritySchemes(SCHEME_NAME, new SecurityScheme()
                                .name(SCHEME_NAME)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
        // Luu y: KHONG goi .addSecurityItem(...) o day de bat buoc TOAN BO API -
        // vi nhu vay se hien nham nut khoa tren ca /api/login, /api/register
        // (nhung API nay khong can token). Cac controller nao can token thi da
        // tu khai bao @SecurityRequirement(name = "bearerAuth") rieng
        // (xem JobController, ProposalController).
    }
}
