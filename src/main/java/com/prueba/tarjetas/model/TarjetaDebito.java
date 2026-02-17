package com.prueba.tarjetas.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.time.LocalDate;

@Entity
@DiscriminatorValue("DEBITO")
public class TarjetaDebito extends Tarjeta {
    
    @Column(name = "cuentaasociada", length = 20)
    private String cuentaAsociada;
   
    
    // Constructor por defecto
    public TarjetaDebito() {
        super();
    }
    
    // Constructor con parámetros
    public TarjetaDebito(String numeroTarjeta, String titular, LocalDate fechaVencimiento,
                         String cuentaAsociada) {
        super(numeroTarjeta, titular, fechaVencimiento);
        this.cuentaAsociada = cuentaAsociada;
    }
   
    
    @Override
    public String getTipoTarjeta() {
        return "DEBITO";
    }
    
    
    public String getCuentaAsociada() {
        return cuentaAsociada;
    }
    
    public void setCuentaAsociada(String cuentaAsociada) {
        this.cuentaAsociada = cuentaAsociada;
    }

    
}
