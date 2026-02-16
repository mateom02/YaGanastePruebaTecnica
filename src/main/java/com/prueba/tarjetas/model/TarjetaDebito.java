package com.prueba.tarjetas.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@DiscriminatorValue("DEBITO")
public class TarjetaDebito extends Tarjeta {
    
    @Column(name = "cuentaasociada", length = 20)
    private String cuentaAsociada;
    
    @Column(name = "PERMITE_SOBREGIRO")
    private Boolean permiteSobregiro = false;
    
    @Column(name = "LIMITE_SOBREGIRO", precision = 15, scale = 2)
    private BigDecimal limiteSobregiro;
    
    // Constructor por defecto
    public TarjetaDebito() {
        super();
        this.permiteSobregiro = false;
        this.limiteSobregiro = BigDecimal.ZERO;
    }
    
    // Constructor con parámetros
    public TarjetaDebito(String numeroTarjeta, String titular, LocalDate fechaVencimiento,
                         String cuentaAsociada) {
        super(numeroTarjeta, titular, fechaVencimiento);
        this.cuentaAsociada = cuentaAsociada;
        this.permiteSobregiro = false;
        this.limiteSobregiro = BigDecimal.ZERO;
    }
    
    @Override
    public void add(BigDecimal monto) throws Exception {
        if (monto == null || monto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new Exception("El monto debe ser mayor a cero");
        }
        
        if (!this.getActiva()) {
            throw new Exception("La tarjeta está inactiva");
        }
        
        if (this.estaVencida()) {
            throw new Exception("La tarjeta está vencida");
        }
        
        this.setSaldo(this.getSaldo().add(monto));
    }
    
    public void retirar(BigDecimal monto) throws Exception {
        if (monto == null || monto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new Exception("El monto debe ser mayor a cero");
        }
        
        if (!this.getActiva()) {
            throw new Exception("La tarjeta está inactiva");
        }
        
        if (this.estaVencida()) {
            throw new Exception("La tarjeta está vencida");
        }
        
        BigDecimal saldoDisponible = this.getSaldo();
        
        if (permiteSobregiro) {
            saldoDisponible = saldoDisponible.add(limiteSobregiro);
        }
        
        if (saldoDisponible.compareTo(monto) < 0) {
            throw new Exception("Saldo insuficiente. Disponible: " + saldoDisponible);
        }
        
        this.setSaldo(this.getSaldo().subtract(monto));
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
    
    public Boolean getPermiteSobregiro() {
        return permiteSobregiro;
    }
    
    public void setPermiteSobregiro(Boolean permiteSobregiro) {
        this.permiteSobregiro = permiteSobregiro;
    }
    
    public BigDecimal getLimiteSobregiro() {
        return limiteSobregiro;
    }
    
    public void setLimiteSobregiro(BigDecimal limiteSobregiro) {
        this.limiteSobregiro = limiteSobregiro;
    }
    
}
