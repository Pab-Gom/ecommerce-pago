package com.ecommerce.pago_service.client;
import com.ecommerce.pago_service.dto.OrdenResponseDto;
import com.ecommerce.pago_service.util.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component

public class OrdenClient{

    private final WebClient webClient;
    private final JwtUtil jwtUtil;
    public OrdenClient(WebClient.Builder webClientBuilder,
                       JwtUtil jwtUtil,
                       @Value("${orden-service.url}") String ordenServiceUrl) {
        this.jwtUtil = jwtUtil;
        this.webClient = webClientBuilder.baseUrl(ordenServiceUrl).build();
    }

    // **** OBTIENE EL ID DEL MICROSERVICIO ORDEN
    public OrdenResponseDto obtenerOrdenPorId(Long id) {
        try {
            String token = jwtUtil.generateInternalToken("pago-service@internal");
            return webClient.get()
                    .uri("/{id}", id)
                    .header("Authorization", "Bearer " + token)
                    .retrieve()
                    .bodyToMono(OrdenResponseDto.class)
                    .block();
        } catch (Exception e) {
            return null;
        }
    }

    // **** DEVUELVE LA ACTUALIZACION DEL ESTADO A ORDEN, DE PENDIENTE A PAGADO
    public void actualizarEstadoOrden(Long id, String estado) {
        try {
            String token = jwtUtil.generateInternalToken("pago-service@internal");
            webClient.patch()
                    .uri("/" + id + "/estado?estado=" + estado)
                    .header("Authorization", "Bearer " + token)
                    .retrieve()
                    .toBodilessEntity()
                    .block();
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar estado de la orden " + id, e);
        }
    }
}