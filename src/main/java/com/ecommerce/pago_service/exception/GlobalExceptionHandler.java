package com.ecommerce.pago_service.exception;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.LocalDateTime;

@RestControllerAdvice

public class GlobalExceptionHandler{

    // **** ERROR PAGO NO ENCONTRADO
    @ExceptionHandler(PagoNoEncontradoException.class)
    public ResponseEntity<ErrorResponse> handlePagoNoEncontrado(PagoNoEncontradoException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    // **** ERROR ID ORDEN NO ENCONTRADA
    @ExceptionHandler(IdOrdenNoEncontradaException.class)
    public ResponseEntity<ErrorResponse> handleOrdenNoEncontrada(IdOrdenNoEncontradaException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    // **** ERROR ID USUARIO NO ENCONTRADO
    @ExceptionHandler(IdUsuarioNoEncontradoException.class)
    public ResponseEntity<ErrorResponse> handleUsuarioNoEncontrado(IdUsuarioNoEncontradoException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    // **** ERROR GENERAL
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntime(RuntimeException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        String mensaje = ex.getBindingResult().getFieldError().getDefaultMessage();
        return buildResponse(HttpStatus.BAD_REQUEST, mensaje);
    }

    // **** ERROR DE AUTENTICACION
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthentication(AuthenticationException ex) {
        return buildResponse(HttpStatus.UNAUTHORIZED, "Token inválido o expirado");
    }

    // **** ERROR DE ACCESO
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException ex) {
        return buildResponse(HttpStatus.FORBIDDEN, "No tienes permisos para acceder a este recurso");
    }
    
    private ResponseEntity<ErrorResponse> buildResponse(HttpStatus status, String mensaje) {
        ErrorResponse error = new ErrorResponse(mensaje, status.value(), LocalDateTime.now());
        return new ResponseEntity<>(error, status);
    }
}