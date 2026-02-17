package com.prueba.tarjetas.exceptions;

public class NotFoundException extends RuntimeException {

    private final String entidad;
    private final Object id;

    public NotFoundException(String entidad, Object id) {
        super(String.format("%s con id '%s' no fue encontrada.", entidad, id));
        this.entidad = entidad;
        this.id = id;
    }

    public NotFoundException(String mensaje) {
        super(mensaje);
        this.entidad = null;
        this.id = null;
    }

    public String getEntidad() {
        return entidad;
    }

    public Object getId() {
        return id;
    }
}
