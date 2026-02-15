package com.ssafy.live.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfiguration {

    @Bean
    public OpenAPI whereIsOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("SSAFY Board API")
                .description("WhereIsMyHome API 문서")
                .version("v1")
                .contact(new Contact().name("SSAFY").url("https://edu.ssafy.com").email("ssafy@ssafy.com")));
    }
}
