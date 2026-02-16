package com.prueba.tarjetas.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@DiscriminatorValue("NOMINA")
public class TarjetaNomina extends Tarjeta {
    
    @Column(name = "EMPRESA", length = 100)
    private String empresa;
    
    @Column(name = "NUMERO_EMPLEADO", length = 50)
    private String numeroEmpleado;
    
    @Column(name = "DEPOSITO_MENSUAL", precision = 15, scale = 2)
    private BigDecimal depositoMensual;
    
    // Constructor por defecto
    public TarjetaNomina() {
        super();
        this.depositoMensual = BigDecimal.ZERO;
    }
    
    // Constructor con parámetros
    public TarjetaNomina(String numeroTarjeta, String titular, LocalDate fechaVencimiento,
                         String empresa, String numeroEmpleado) {
        super(numeroTarjeta, titular, fechaVencimiento);
        this.empresa = empresa;
        this.numeroEmpleado = numeroEmpleado;
        this.depositoMensual = BigDecimal.ZERO;
    }
    
    /**
     * Implementación del método add para tarjetas de nómina
     * Permite depósitos de nómina y otros abonos
     */
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
    
    /**
     * Método específico para depositar la nómina mensual
     */
    public void depositarNomina(BigDecimal monto) throws Exception {
        if (monto == null || monto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new Exception("El monto de la nómina debe ser mayor a cero");
        }
        
        this.depositoMensual = monto;
        this.add(monto);
    }
    
    /**
     * Método para realizar un retiro de la tarjeta de nómina
     */
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
        
        if (this.getSaldo().compareTo(monto) < 0) {
            throw new Exception("Saldo insuficiente. Disponible: " + this.getSaldo());
        }
        
        this.setSaldo(this.getSaldo().subtract(monto));
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
    
    public String getNumeroEmpleado() {
        return numeroEmpleado;
    }
    
    public void setNumeroEmpleado(String numeroEmpleado) {
        this.numeroEmpleado = numeroEmpleado;
    }
    
    public BigDecimal getDepositoMensual() {
        return depositoMensual;
    }
    
    public void setDepositoMensual(BigDecimal depositoMensual) {
        this.depositoMensual = depositoMensual;
    }
    
   
}
