package com.prueba.tarjetas.exceptions;

public class BusinessException extends RuntimeException {

    private final String codigoError;

    public BusinessException(String mensaje) {
        super(mensaje);
        this.codigoError = "BUSINESS_ERROR";
    }

    public BusinessException(String codigoError, String mensaje) {
        super(mensaje);
        this.codigoError = codigoError;
    }

    public String getCodigoError() {
        return codigoError;
    }
}
