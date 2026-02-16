package com.prueba.tarjetas.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public class TarjetaCreateDTO {
    
    @NotBlank(message = "El número de tarjeta es obligatorio")
    private String numeroTarjeta;
    
    @NotBlank(message = "El titular es obligatorio")
    private String titular;
    
    @NotBlank(message = "La fecha de vencimiento es obligatoria (formato: YYYY-MM-DD)")
    private String fechaVencimiento;
    
    @NotNull(message = "El ID del usuario es obligatorio")
    private Long idUsuario;
    
    @NotBlank(message = "El tipo de tarjeta es obligatorio (CREDITO, DEBITO, NOMINA)")
    private String tipoTarjeta;
    
    // Campos específicos para tarjeta de crédito
    @Positive(message = "El límite de crédito debe ser positivo")
    private BigDecimal limiteCredito;
    private BigDecimal tasaInteres;
    
    // Campos específicos para tarjeta de débito
    private String cuentaAsociada;
    private Boolean permiteSobregiro;
    private BigDecimal limiteSobregiro;
    
    // Campos específicos para tarjeta de nómina
    private String empresa;
    private String numeroEmpleado;
    
    // Constructores
    public TarjetaCreateDTO() {}
    
    // Getters y Setters
    public String getNumeroTarjeta() {
        return numeroTarjeta;
    }
    
    public void setNumeroTarjeta(String numeroTarjeta) {
        this.numeroTarjeta = numeroTarjeta;
    }
    
    public String getTitular() {
        return titular;
    }
    
    public void setTitular(String titular) {
        this.titular = titular;
    }
    
    public String getFechaVencimiento() {
        return fechaVencimiento;
    }
    
    public void setFechaVencimiento(String fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }
    
    public Long getIdUsuario() {
        return idUsuario;
    }
    
    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }
    
    public String getTipoTarjeta() {
        return tipoTarjeta;
    }
    
    public void setTipoTarjeta(String tipoTarjeta) {
        this.tipoTarjeta = tipoTarjeta;
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
}
