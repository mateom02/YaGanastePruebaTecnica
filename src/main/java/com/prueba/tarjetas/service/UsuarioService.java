package com.prueba.tarjetas.service;

import com.prueba.tarjetas.dto.UsuarioCreateDTO;
import com.prueba.tarjetas.exceptions.DuplicateResourceException;
import com.prueba.tarjetas.exceptions.NotFoundException;
import com.prueba.tarjetas.exceptions.ValidationException;
import com.prueba.tarjetas.model.Usuario;
import com.prueba.tarjetas.repository.UsuarioRepository;
import com.prueba.tarjetas.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional
    public Result crearUsuario(UsuarioCreateDTO dto) {
        Result result = new Result();
        try {
            // Validaciones
            if (dto.getEmail() == null || dto.getEmail().isBlank()) {
                throw new ValidationException("email", "El email no puede estar vacío.");
            }

            if (dto.getNombre() == null || dto.getNombre().isBlank()) {
                throw new ValidationException("nombre", "El nombre no puede estar vacío.");
            }

            if (usuarioRepository.existsByEmail(dto.getEmail())) {
                throw new DuplicateResourceException("Usuario", "email", dto.getEmail());
            }

            Usuario usuario = new Usuario();
            usuario.setNombre(dto.getNombre());
            usuario.setApellido(dto.getApellido());
            usuario.setEmail(dto.getEmail());
            usuario.setActivo(true);

            Usuario usuarioGuardado = usuarioRepository.save(usuario);
            result.correct = true;
            result.object = usuarioGuardado;

        } catch (ValidationException | DuplicateResourceException ex) {
            throw ex;
        } catch (Exception ex) {
            throw ex;
        }
        return result;
    }

    public Result obtenerTodos() {
        List<Object> lista = usuarioRepository.findAll()
                .stream()
                .map(u -> (Object) u)
                .toList();

        Result result = new Result();
        result.correct = true;
        result.objects = lista;

        return result;
    }

    public Result obtenerPorId(Long id) {
        Result result = new Result();

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuario", id));

        result.correct = true;
        result.object = usuario;
        return result;
    }

    @Transactional
    public Result actualizarUsuario(Long id, UsuarioCreateDTO dto) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuario", id));

        if (!usuario.getEmail().equals(dto.getEmail())
                && usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateResourceException("Usuario", "email", dto.getEmail());
        }

        usuario.setNombre(dto.getNombre());
        usuario.setApellido(dto.getApellido());
        usuario.setEmail(dto.getEmail());

        Result result = new Result();
        result.correct = true;
        result.object = usuarioRepository.save(usuario);
        return result;
    }

}
