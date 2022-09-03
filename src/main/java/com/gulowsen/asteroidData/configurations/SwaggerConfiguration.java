package com.gulowsen.asteroidData.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration implements WebMvcConfigurer {

    @Bean
    public InternalResourceViewResolver defaultViewResolver() {
        return new InternalResourceViewResolver();
    }

    @Bean
    public Docket nonSecuredDocket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.gulowsen.asteroidData"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(getApiInfo())
                ;

    }


    private ApiInfo getApiInfo() {
        return new ApiInfo(
                "Asteroid Data API",
                "Webservice to query asteroid data",
                "1.0",
                null,
                new Contact("Anders Gulowsen", null,"andersgulowsen+asteroiddata@gmail.com"),
                null,
                null,
                Collections.emptyList()
        );
    }


}
