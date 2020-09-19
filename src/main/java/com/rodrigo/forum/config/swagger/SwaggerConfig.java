package com.rodrigo.forum.config.swagger;

import com.google.common.base.Predicate;
import com.rodrigo.forum.model.Usuario;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Arrays;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket productApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.rodrigo.forum"))
                .paths(PathSelectors.ant("/**"))
                .build()
                .ignoredParameterTypes(Usuario.class)
                .globalOperationParameters(
                    Arrays.asList(
                            new ParameterBuilder()
                                    .name("Authorization")
                                    .description("Header para Token JWT")
                                    .modelRef(new ModelRef("string"))
                                    .parameterType("header")
                                    .required(false)
                                    .build()))
                .apiInfo(metaInfo());
    }
    private ApiInfo metaInfo() {

        @SuppressWarnings("rawtypes")
        ApiInfo apiInfo = new ApiInfo(
                "Forum API REST",
                "API REST fórum de desenvolvimentos",
                "1.0",
                "Terms of Service",
                new Contact("Rodrigo Moreira Carvalho", "https://github.com/RodrigoMCarvalho",
                        "rodrigo_moreira84@hotmail.com"),
                "Apache License Version 2.0",
                "https://www.apache.org/licesen.html", new ArrayList<VendorExtension>()
        );

        return apiInfo;
    }

}