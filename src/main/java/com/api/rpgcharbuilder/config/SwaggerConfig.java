package com.api.rpgcharbuilder.config;

import io.swagger.annotations.Api;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

@Configuration
@EnableSwagger2
public class SwaggerConfig{

    @Bean
    public Docket api(){
        return new Docket(DocumentationType.SWAGGER_2)
                .select().apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .pathMapping("/")
                .apiInfo(metaData())
                .tags(new Tag("Race Controller","Controlador para Raças jogáveis inspiradas de Gêneros RPG/Fantasia Medieval"),
                        new Tag("Classe Controller", "Controlador para Classes/Profissões inspiradas de Gêneros RPG/Fantasia Medieval"),
                        new Tag("Items Controller", "Controlador para Itens jogáveis inspirados de Gêneros RPG/Fantasia Medieval"),
                        new Tag("Characters Controller", "Controlador para os Personagens(Chars) dos Jogadores e do Mestre"));
    }

    private ApiInfo metaData(){

        Contact contact = new Contact("Gabriel Chagas", "https://github.com/devChagas",
                "gabriel.chagas@opus-software.com.br");

        return new ApiInfo(
                "RPG Character Builder API Documentation",
                "A REST API to create RPG Characters with Races, Classes and Items options provided by your DM!!\n\n\n" +
                        "Tell us your name, choose a race, pick a class, grab your items and battle among yourselves!!",
                "1.0",
                "Terms of Service: ...",
                contact,
                "",
                "",
                new ArrayList<>());
    }


}

