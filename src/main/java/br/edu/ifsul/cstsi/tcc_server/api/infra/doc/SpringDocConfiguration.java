package br.edu.ifsul.cstsi.tcc_server.api.infra.doc;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration //indica que essa classe deve ser adicionada ao Contexto do aplicativo como um Bean de Configuração da Documentação da API
public class SpringDocConfiguration {
    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "bearerAuth";
        return new OpenAPI()
            .addSecurityItem(new SecurityRequirement()
                .addList(securitySchemeName))
            .components(new Components()
                .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                    .name(securitySchemeName)
                    .type(SecurityScheme.Type.HTTP)
                    .scheme("bearer")
                    .bearerFormat("JWT")))
            .info(new Info()
                .title("API de World of Tokusatsu")
                .description("API Rest da aplicação tcc_server, contendo as funcionalidades de CRUD de um streaming de séries.")
                .contact(new Contact()
                    .name("World of Tokusatsu")
                    .email("suporte.tokusatsu@gmail.com"))
                .license(new License()
                    .name("Apache 2.0")
                    .url("http://https://tccserver-f1d0375900a5.herokuapp.com/api/licenca")));
    }
}
