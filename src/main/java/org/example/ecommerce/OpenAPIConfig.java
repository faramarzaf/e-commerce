package org.example.ecommerce;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {


    @Bean
    public OpenAPI customOpenAPI() {
        Info license = new Info().title("API Title Example")
                .description("API Description Example")
                .version("API Version")
                .contact(new Contact()
                        .name("API Contact Name")
                        .url("https://api.contact.url")
                        .email("example@example.email"))
                .termsOfService("https://api.terms.of.service")
                .license(new License()
                        .name("API License")
                        .url("https://api.license.url"));

        ExternalDocumentation apiExternalDocumentation = new ExternalDocumentation()
                .description("API External Documentation")
                .url("https://api.external.documentation.url");

        Components components = new Components()
                .addSecuritySchemes("Bearer Authentication", new SecurityScheme()
                        .name("Bearer Authentication")
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT"));

        return new OpenAPI()
                .info(license)
                .externalDocs(apiExternalDocumentation)
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .components(components);
    }
}
