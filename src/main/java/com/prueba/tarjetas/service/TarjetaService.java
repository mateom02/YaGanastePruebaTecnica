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
            // Validar que el usuario exista
            Optional<Usuario> usuarioOpt = usuarioRepository.findById(dto.getIdUsuario());
            if (usuarioOpt.isEmpty()) {
                return Result.error("Usuario no encontrado con ID: " + dto.getIdUsuario());
            }
            
            Usuario usuario = usuarioOpt.get();
            
            if (!usuario.getActivo()) {
                return Result.error("El usuario está inactivo");
            }
            
            // Validar que no exista una tarjeta con ese número
            if (tarjetaRepository.existsByNumeroTarjeta(dto.getNumeroTarjeta())) {
                return Result.error("Ya existe una tarjeta con ese número");
            }
            
            // Crear la tarjeta según el tipo (POLIMORFISMO)
            Tarjeta tarjeta = crearTarjetaPorTipo(dto);
            
            if (tarjeta == null) {
                return Result.error("Tipo de tarjeta no válido: " + dto.getTipoTarjeta());
            }
            
            // Agregar la tarjeta al usuario
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
            List<Tarjeta> tarjetas = tarjetaRepository.findByUsuarioId(usuarioId);
            List<Object> resultado = new ArrayList<>(tarjetas);
            return Result.success(resultado);
        } catch (Exception e) {
            return Result.error("Error al obtener tarjetas del usuario: " + e.getMessage(), e);
        }
    }

    @Transactional
    public Result agregarSaldo(Long id, AgregarSaldoDTO dto) {
        try {
            Optional<Tarjeta> tarjetaOpt = tarjetaRepository.findById(id);
            
            if (tarjetaOpt.isEmpty()) {
                return Result.error("Tarjeta no encontrada con ID: " + id);
            }
            
            Tarjeta tarjeta = tarjetaOpt.get();
            
            // Llamada polimórfica al método add()
            // Cada tipo de tarjeta ejecutará su propia implementación
            tarjeta.add(dto.getMonto());
            
            Tarjeta tarjetaActualizada = tarjetaRepository.save(tarjeta);
            return Result.success(tarjetaActualizada);
            
        } catch (Exception e) {
            return Result.error("Error al agregar saldo: " + e.getMessage(), e);
        }
    }
    
    @Transactional
    public Result eliminarTarjeta(Long id) {
        try {
            Optional<Tarjeta> tarjetaOpt = tarjetaRepository.findById(id);
            
            if (tarjetaOpt.isEmpty()) {
                return Result.error("Tarjeta no encontrada con ID: " + id);
            }
            
            Tarjeta tarjeta = tarjetaOpt.get();
            tarjeta.setActiva(false);
            
            tarjetaRepository.save(tarjeta);
            return Result.success();
            
        } catch (Exception e) {
            return Result.error("Error al eliminar la tarjeta: " + e.getMessage(), e);
        }
    }
    
    public Result obtenerActivasPorUsuario(Long usuarioId) {
        try {
            List<Tarjeta> tarjetas = tarjetaRepository.findByUsuarioIdAndActiva(usuarioId, true);
            List<Object> resultado = new ArrayList<>(tarjetas);
            return Result.success(resultado);
        } catch (Exception e) {
            return Result.error("Error al obtener tarjetas activas: " + e.getMessage(), e);
        }
    }
}
