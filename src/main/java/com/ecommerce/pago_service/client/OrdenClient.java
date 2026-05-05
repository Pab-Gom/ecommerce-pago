package com.ecommerce.pago_service.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.ecommerce.pago_service.dto.OrdenResponseDto;

@Component
public class OrdenClient {

    private final WebClient.Builder webClientBuilder;

    @Value("${orden-service.url}")
    private String ordenServiceUrl;

    public OrdenClient(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    //****OBTENER ORDEN POR ID
    public OrdenResponseDto obtenerOrdenPorId(Long id) {
        try {
            return webClientBuilder.build()
                    .get()
                    .uri(ordenServiceUrl + "/{id}", id)
                    .retrieve()
                    .bodyToMono(OrdenResponseDto.class)
                    .block();
        } catch (Exception e) {
            return null;
        }
    }

    //****ACTUALIZAR ESTADO DE ORDEN
    public void actualizarEstadoOrden(Long id, String estado) {
        try {
            webClientBuilder.build()
                    .patch()
                    .uri(ordenServiceUrl + "/" + id + "/estado?estado=" + estado)
                    .retrieve()
                    .toBodilessEntity()
                    .block();
        } catch (Exception e) {
            // puedes logear si quieres
        }
    }
}