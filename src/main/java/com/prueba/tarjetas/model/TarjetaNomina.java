package com.prueba.tarjetas.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@DiscriminatorValue("NOMINA")
public class TarjetaNomina extends Tarjeta {
    
    @Column(name = "empresa", length = 100)
    private String empresa;
    
    @Column(name = "depositomensual", precision = 15, scale = 2)
    private BigDecimal depositoMensual;
    
    // Constructor por defecto
    public TarjetaNomina() {
        super();
        this.depositoMensual = BigDecimal.ZERO;
    }
    
    // Constructor con parámetros
    public TarjetaNomina(String numeroTarjeta, String titular, LocalDate fechaVencimiento
                         ) {
        super(numeroTarjeta, titular, fechaVencimiento);
    }

    @Override
    public String getTipoTarjeta() {
        return "NOMINA";
    }
    
    // Getters y Setters
    public String getEmpresa() {
        return empresa;
    }
    
    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }
    
    public BigDecimal getDepositoMensual() {
        return depositoMensual;
    }
    
    public void setDepositoMensual(BigDecimal depositoMensual) {
        this.depositoMensual = depositoMensual;
    }
    
   
}
