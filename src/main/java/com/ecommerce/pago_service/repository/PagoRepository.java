package com.ecommerce.pago_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.pago_service.model.Pago;

public interface PagoRepository extends JpaRepository<Pago, Long>{
    
    List<Pago> findByOrdenId(Long ordenId);

    List<Pago> findByUsuarioId(Long usuarioId);

    List<Pago> findByEstado(String estado);

    List<Pago> findByMetodoPago(String metodoPago);

    void deleteByEstado(String estado);

    void deleteByOrdenId(Long ordenId);
}
