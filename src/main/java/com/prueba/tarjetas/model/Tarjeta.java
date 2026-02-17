package com.prueba.tarjetas.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "TARJETA")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TIPOTARJETA", discriminatorType = DiscriminatorType.STRING)
public abstract class Tarjeta {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idtarjeta")
    private Long idTarjeta;
    
    @NotBlank(message = "El número de tarjeta es obligatorio")
    @Column(name = "numerotarjeta", nullable = false, unique = true, length = 16)
    private String numeroTarjeta;
    
    //Igual y lo quito
    @NotBlank(message = "El titular es obligatorio")
    @Column(name = "titular", nullable = false, length = 100)
    private String titular;
    
    @NotNull(message = "La fecha de vencimiento es obligatoria")
    @Column(name = "fechavencimiento", nullable = false)
    private LocalDate fechaVencimiento;
    
    @NotNull(message = "El saldo es obligatorio")
    @Positive(message = "El saldo debe ser positivo")
    @Column(name = "saldo", nullable = false, precision = 15, scale = 2)
    private BigDecimal saldo;
    
    @Column(name = "activa", nullable = false)
    private Boolean activa = true;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idusuario", nullable = false)
    @JsonIgnore
    private Usuario usuario;
    
    public Tarjeta() {
        this.saldo = BigDecimal.ZERO;
        this.activa = true;
    }
    
    public Tarjeta(String numeroTarjeta, String titular, LocalDate fechaVencimiento) {
        this.numeroTarjeta = numeroTarjeta;
        this.titular = titular;
        this.fechaVencimiento = fechaVencimiento;
        this.saldo = BigDecimal.ZERO;
        this.activa = true;
    }
       
    public boolean estaVencida() {
        return LocalDate.now().isAfter(fechaVencimiento);
    }
    
    public abstract String getTipoTarjeta();

    public Long getIdTarjeta() {
        return idTarjeta;
    }

    public void setIdTarjeta(Long idTarjeta) {
        this.idTarjeta = idTarjeta;
    }
    
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
    
    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }
    
    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }
    
    public BigDecimal getSaldo() {
        return saldo;
    }
    
    protected void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }
    
    public Boolean getActiva() {
        return activa;
    }
    
    public void setActiva(Boolean activa) {
        this.activa = activa;
    }
    
    public Usuario getUsuario() {
        return usuario;
    }
    
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    
}
