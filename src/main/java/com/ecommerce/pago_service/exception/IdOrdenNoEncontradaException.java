package com.ecommerce.pago_service.exception;

public class IdOrdenNoEncontradaException extends RuntimeException {

    public IdOrdenNoEncontradaException(String mensaje) {
        super(mensaje);
    }
}