package com.prueba.tarjetas.service;

import com.prueba.tarjetas.dto.TarjetaCreateDTO;
import com.prueba.tarjetas.exceptions.BusinessException;
import com.prueba.tarjetas.exceptions.DuplicateResourceException;
import com.prueba.tarjetas.exceptions.NotFoundException;
import com.prueba.tarjetas.exceptions.ValidationException;
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

@Service
public class TarjetaService {

    @Autowired
    private TarjetaRepository tarjetaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional
    public Result crearTarjeta(TarjetaCreateDTO dto) {
        Result result = new Result();

        Usuario usuario = usuarioRepository.findById(dto.getIdUsuario())
                .orElseThrow(() -> new NotFoundException("Usuario", dto.getIdUsuario()));

        if (tarjetaRepository.existsByNumeroTarjeta(dto.getNumeroTarjeta())) {
            throw new DuplicateResourceException("Tarjeta", "numero", dto.getNumeroTarjeta());
        }

        if (!dto.getNumeroTarjeta().matches("\\d{16}")) {
            throw new ValidationException("numeroTarjeta", "Debe contener exactamente 16 digitos.");
        }

        try {
            Tarjeta tarjeta = crearTarjetaPorTipo(dto);
            usuario.agregarTarjeta(tarjeta);
            result.correct = true;
            result.object = tarjeta;

        } catch (BusinessException ex) {
            throw ex;
        } catch (ValidationException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new BusinessException("ERROR_CREACION", ex.getMessage());
        }
        return result;
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
                if (dto.getLimiteCredito() != null) {
                    credito.setLimiteCredito(dto.getLimiteCredito());
                }
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
                if (dto.getCuentaAsociada() != null) {
                    debito.setCuentaAsociada(dto.getCuentaAsociada());
                }

                return debito;

            case "NOMINA":
                TarjetaNomina nomina = new TarjetaNomina(
                        dto.getNumeroTarjeta(),
                        dto.getTitular(),
                        fechaVencimiento
                );
                if (dto.getEmpresa() != null) {
                    nomina.setEmpresa(dto.getEmpresa());
                }
                if (dto.getDepositoMensual()!= null) {
                    nomina.setDepositoMensual(dto.getDepositoMensual());
                }
                return nomina;

            default:
                throw new BusinessException("TIPO_INVALIDO",
                        "Tipo de tarjeta no soportado: " + dto.getTipoTarjeta());
        }
    }

    public Result obtenerTodas() {
        List<Tarjeta> tarjetas = tarjetaRepository.findAll();

        if (tarjetas.isEmpty()) {
            throw new BusinessException("SIN_TARJETAS", "No hay tarjetas registradas.");
        }

        Result result = new Result();
        result.correct = true;
        result.objects = new ArrayList<>(tarjetas);
        return result;
    }

    public Result obtenerPorUsuario(Long usuarioId) {
        if (!usuarioRepository.existsById(usuarioId)) {
            throw new NotFoundException("Usuario", usuarioId);
        }

        List<Tarjeta> tarjetas = tarjetaRepository.findByUsuario(usuarioId);

        if (tarjetas.isEmpty()) {
            throw new BusinessException("SIN_TARJETAS",
                    "El usuario con id " + usuarioId + " no tiene tarjetas registradas.");
        }

        Result result = new Result();
        result.correct = true;
        result.objects = new ArrayList<>(tarjetas);
        return result;
    }

}
