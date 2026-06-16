package com.ecommerce.pago_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Microservicio de Pagos - Ecommerce")
                        .version("1.0")
                        .description(
                            "API para la gestión de pagos del ecommerce. Permite crear pagos asociados a órdenes, consultar el historial de pagos por usuario, orden o estado y actualizar o eliminar pagos. Requiere autenticación mediante JWT con roles USUARIO o ADMIN."
                        ));
        }                   
}