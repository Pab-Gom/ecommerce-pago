package com.ecommerce.pago_service.assembler;

import com.ecommerce.pago_service.controller.PagoController;
import com.ecommerce.pago_service.dto.PagoResponseDto;
import com.ecommerce.pago_service.model.Pago;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class PagoModelAssembler implements RepresentationModelAssembler<Pago, EntityModel<PagoResponseDto>> {

    @Override
    public EntityModel<PagoResponseDto> toModel(Pago pago) {
        PagoResponseDto dto = mapToDTO(pago);

        return EntityModel.of(dto,
            linkTo(methodOn(PagoController.class).obtenerPorId(pago.getId())).withSelfRel(),
            linkTo(methodOn(PagoController.class).obtenerTodos()).withRel("pagos"),
            linkTo(methodOn(PagoController.class).obtenerMisPagos()).withRel("mis-pagos"),
            linkTo(methodOn(PagoController.class).obtenerPorEstado(pago.getEstado())).withRel("estado"),
            linkTo(methodOn(PagoController.class).obtenerPorOrden(pago.getOrdenId())).withRel("orden"),
            linkTo(methodOn(PagoController.class).obtenerPorUsuario(pago.getUsuarioId())).withRel("usuario")
        );
    }

    @Override
    public CollectionModel<EntityModel<PagoResponseDto>> toCollectionModel(Iterable<? extends Pago> entities) {
        CollectionModel<EntityModel<PagoResponseDto>> models = RepresentationModelAssembler.super.toCollectionModel(entities);
        models.add(linkTo(methodOn(PagoController.class).obtenerTodos()).withSelfRel());
        return models;
    }

    private PagoResponseDto mapToDTO(Pago pago) {
        PagoResponseDto dto = new PagoResponseDto();
        dto.setId(pago.getId());
        dto.setOrdenId(pago.getOrdenId());
        dto.setUsuarioId(pago.getUsuarioId());
        dto.setMonto(pago.getMonto());
        dto.setMetodoPago(pago.getMetodoPago());
        dto.setEstado(pago.getEstado());
        dto.setFechaPago(pago.getFechaPago());
        return dto;
    }
}