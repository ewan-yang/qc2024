package com.tecpie.platform.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("analysis-alarm接口文档")
                        .version("1.0")
                        .description( "analysis-alarm集成springdoc-openapi文档")
                        .license(new License().name("Apache 2.0")));
    }

}
