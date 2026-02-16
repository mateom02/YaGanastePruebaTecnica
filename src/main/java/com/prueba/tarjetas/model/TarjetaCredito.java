package com.prueba.tarjetas.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@DiscriminatorValue("CREDITO")
public class TarjetaCredito extends Tarjeta {
    
    @NotNull(message = "El límite de crédito es obligatorio")
    @Positive(message = "El límite de crédito debe ser positivo")
    @Column(name = "LIMITE_CREDITO", precision = 15, scale = 2)
    private BigDecimal limiteCredito;
    
    @Column(name = "TASA_INTERES", precision = 5, scale = 2)
    private BigDecimal tasaInteres;
    
    // Constructor por defecto
    public TarjetaCredito() {
        super();
        this.tasaInteres = new BigDecimal("18.00"); // Tasa por defecto
    }
    
    // Constructor con parámetros
    public TarjetaCredito(String numeroTarjeta, String titular, LocalDate fechaVencimiento, 
                          BigDecimal limiteCredito) {
        super(numeroTarjeta, titular, fechaVencimiento);
        this.limiteCredito = limiteCredito;
        this.tasaInteres = new BigDecimal("18.00");
        this.setSaldo(limiteCredito);
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
        
        BigDecimal nuevoSaldo = this.getSaldo().add(monto);
        
        if (nuevoSaldo.compareTo(limiteCredito) > 0) {
            throw new Exception("El pago excede el límite de crédito disponible");
        }
        
        this.setSaldo(nuevoSaldo);
    }
    
    
    public void cargar(BigDecimal monto) throws Exception {
        if (monto == null || monto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new Exception("El monto debe ser mayor a cero");
        }
        
        if (!this.getActiva()) {
            throw new Exception("La tarjeta está inactiva");
        }
        
        if (this.estaVencida()) {
            throw new Exception("La tarjeta está vencida");
        }
        
        if (this.getSaldo().compareTo(monto) < 0) {
            throw new Exception("Saldo insuficiente. Disponible: " + this.getSaldo());
        }
        
        this.setSaldo(this.getSaldo().subtract(monto));
    }
    
    @Override
    public String getTipoTarjeta() {
        return "CREDITO";
    }
    
    
    public BigDecimal getLimiteCredito() {
        return limiteCredito;
    }
    
    public void setLimiteCredito(BigDecimal limiteCredito) {
        this.limiteCredito = limiteCredito;
    }
    
    public BigDecimal getTasaInteres() {
        return tasaInteres;
    }
    
    public void setTasaInteres(BigDecimal tasaInteres) {
        this.tasaInteres = tasaInteres;
    }
    
}
