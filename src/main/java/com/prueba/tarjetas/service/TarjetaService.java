package com.prueba.tarjetas.service;

import com.prueba.tarjetas.dto.AgregarSaldoDTO;
import com.prueba.tarjetas.dto.TarjetaCreateDTO;
import com.prueba.tarjetas.model.*;
import com.prueba.tarjetas.repository.TarjetaRepository;
import com.prueba.tarjetas.repository.UsuarioRepository;
import com.prueba.tarjetas.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TarjetaService {
    
    @Autowired
    private TarjetaRepository tarjetaRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Transactional
    public Result crearTarjeta(TarjetaCreateDTO dto) {
        try {
            Optional<Usuario> usuarioOpt = usuarioRepository.findById(dto.getIdUsuario());
            if (usuarioOpt.isEmpty()) {
                return Result.error("Usuario no encontrado con ID: " + dto.getIdUsuario());
            }
            
            Usuario usuario = usuarioOpt.get();
            
            if (!usuario.getActivo()) {
                return Result.error("El usuario está inactivo");
            }
            
            if (tarjetaRepository.existsByNumeroTarjeta(dto.getNumeroTarjeta())) {
                return Result.error("Ya existe una tarjeta con ese número");
            }
            
            Tarjeta tarjeta = crearTarjetaPorTipo(dto);
            
            if (tarjeta == null) {
                return Result.error("Tipo de tarjeta no válido: " + dto.getTipoTarjeta());
            }
            
            usuario.agregarTarjeta(tarjeta);
            
            Tarjeta tarjetaGuardada = tarjetaRepository.save(tarjeta);
            return Result.success(tarjetaGuardada);
            
        } catch (Exception e) {
            return Result.error("Error al crear la tarjeta: " + e.getMessage(), e);
        }
    }
    
    private Tarjeta crearTarjetaPorTipo(TarjetaCreateDTO dto) {
        LocalDate fechaVencimiento = LocalDate.parse(dto.getFechaVencimiento());
        
        switch (dto.getTipoTarjeta().toUpperCase()) {
            case "CREDITO":
                TarjetaCredito credito = new TarjetaCredito(
                    dto.getNumeroTarjeta(),
                    dto.getTitular(),
                    fechaVencimiento,
                    dto.getLimiteCredito()
                );
                if (dto.getTasaInteres() != null) {
                    credito.setTasaInteres(dto.getTasaInteres());
                }
                return credito;
                
            case "DEBITO":
                TarjetaDebito debito = new TarjetaDebito(
                    dto.getNumeroTarjeta(),
                    dto.getTitular(),
                    fechaVencimiento,
                    dto.getCuentaAsociada()
                );
                if (dto.getPermiteSobregiro() != null) {
                    debito.setPermiteSobregiro(dto.getPermiteSobregiro());
                }
                if (dto.getLimiteSobregiro() != null) {
                    debito.setLimiteSobregiro(dto.getLimiteSobregiro());
                }
                return debito;
                
            case "NOMINA":
                TarjetaNomina nomina = new TarjetaNomina(
                    dto.getNumeroTarjeta(),
                    dto.getTitular(),
                    fechaVencimiento,
                    dto.getEmpresa(),
                    dto.getNumeroEmpleado()
                );
                return nomina;
                
            default:
                return null;
        }
    }
    
    public Result obtenerTodas() {
        try {
            List<Tarjeta> tarjetas = tarjetaRepository.findAll();
            List<Object> resultado = new ArrayList<>(tarjetas);
            return Result.success(resultado);
        } catch (Exception e) {
            return Result.error("Error al obtener tarjetas: " + e.getMessage(), e);
        }
    }
    

    public Result obtenerPorId(Long id) {
        try {
            Optional<Tarjeta> tarjeta = tarjetaRepository.findById(id);
            
            if (tarjeta.isEmpty()) {
                return Result.error("Tarjeta no encontrada con ID: " + id);
            }
            
            return Result.success(tarjeta.get());
        } catch (Exception e) {
            return Result.error("Error al obtener la tarjeta: " + e.getMessage(), e);
        }
    }
    
    public Result obtenerPorUsuario(Long usuarioId) {
        try {
            List<Tarjeta> tarjetas = tarjetaRepository.findByUsuario(usuarioId);
            List<Object> resultado = new ArrayList<>(tarjetas);
            return Result.success(resultado);
        } catch (Exception e) {
            return Result.error("Error al obtener tarjetas del usuario: " + e.getMessage(), e);
        }
    }


    
 
    
   
}
