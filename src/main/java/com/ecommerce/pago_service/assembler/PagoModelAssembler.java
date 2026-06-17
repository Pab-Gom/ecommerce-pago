package com.ecommerce.pago_service.assembler;

import com.ecommerce.pago_service.controller.PagoControllerV2;
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
            linkTo(methodOn(PagoControllerV2.class).obtenerPorId(pago.getId())).withSelfRel(),
            linkTo(methodOn(PagoControllerV2.class).obtenerTodos()).withRel("pagos"),
            linkTo(methodOn(PagoControllerV2.class).obtenerMisPagos()).withRel("mis-pagos"),
            linkTo(methodOn(PagoControllerV2.class).obtenerPorEstado(pago.getEstado())).withRel("estado"),
            linkTo(methodOn(PagoControllerV2.class).obtenerPorOrden(pago.getOrdenId())).withRel("orden"),
            linkTo(methodOn(PagoControllerV2.class).obtenerPorUsuario(pago.getUsuarioId())).withRel("usuario")
        );
    }

    @Override
    public CollectionModel<EntityModel<PagoResponseDto>> toCollectionModel(Iterable<? extends Pago> entities) {
        CollectionModel<EntityModel<PagoResponseDto>> models = RepresentationModelAssembler.super.toCollectionModel(entities);
        models.add(linkTo(methodOn(PagoControllerV2.class).obtenerTodos()).withSelfRel());
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