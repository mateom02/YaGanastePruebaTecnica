
package com.prueba.tarjetas.exceptions;

public class ValidationException extends RuntimeException {

    private final String campo;

    public ValidationException(String campo, String mensaje) {
        super(String.format("Campo '%s': %s", campo, mensaje));
        this.campo = campo;
    }

    public ValidationException(String mensaje) {
        super(mensaje);
        this.campo = null;
    }

    public String getCampo() {
        return campo;
    }

}
