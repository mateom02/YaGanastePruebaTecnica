package com.prueba.tarjetas.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

/**
 * DTO para operaciones de agregar saldo a tarjetas
 */
public class AgregarSaldoDTO {
    
    @NotNull(message = "El monto es obligatorio")
    @Positive(message = "El monto debe ser mayor a cero")
    private BigDecimal monto;
    
    // Constructor
    public AgregarSaldoDTO() {}
    
    public AgregarSaldoDTO(BigDecimal monto) {
        this.monto = monto;
    }
    
    // Getters y Setters
    public BigDecimal getMonto() {
        return monto;
    }
    
    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }
}
