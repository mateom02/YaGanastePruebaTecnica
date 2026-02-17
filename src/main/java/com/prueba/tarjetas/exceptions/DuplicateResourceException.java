package com.prueba.tarjetas.exceptions;

public class DuplicateResourceException extends RuntimeException {

    private final String entidad;
    private final String campo;
    private final Object valor;

    public DuplicateResourceException(String entidad, String campo, Object valor) {
        super(String.format("%s con %s '%s' ya existe.", entidad, campo, valor));
        this.entidad = entidad;
        this.campo = campo;
        this.valor = valor;
    }

    public String getEntidad() {
        return entidad;
    }

    public String getCampo() {
        return campo;
    }

    public Object getValor() {
        return valor;
    }
}
