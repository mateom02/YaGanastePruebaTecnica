package com.prueba.tarjetas.service;

import com.prueba.tarjetas.dto.UsuarioCreateDTO;
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
        try {
            // Validar que el email no esté registrado
            if (usuarioRepository.existsByEmail(dto.getEmail())) {
                return Result.error("Ya existe un usuario con ese email");
            }

            Usuario usuario = new Usuario();
            usuario.setNombre(dto.getNombre());
            usuario.setApellido(dto.getApellido());
            usuario.setEmail(dto.getEmail());
            usuario.setActivo(true);

            Usuario usuarioGuardado = usuarioRepository.save(usuario);
            return Result.success(usuarioGuardado);

        } catch (Exception e) {
            return Result.error("Error al crear el usuario: " + e.getMessage(), e);
        }
    }

    public Result obtenerTodos() {
        try {
            List<Usuario> usuarios = usuarioRepository.findAll();
            List<Object> resultado = new ArrayList<>(usuarios);
            return Result.success(resultado);
        } catch (Exception e) {
            return Result.error("Error al obtener usuarios: " + e.getMessage(), e);
        }
    }

    public Result obtenerPorId(Long id) {
        try {
            Optional<Usuario> usuario = usuarioRepository.findById(id);

            if (usuario.isEmpty()) {
                return Result.error("Usuario no encontrado con ID: " + id);
            }

            return Result.success(usuario.get());
        } catch (Exception e) {
            return Result.error("Error al obtener el usuario: " + e.getMessage(), e);
        }
    }

    public Result obtenerConTarjetas(Long id) {
        try {
            Optional<Usuario> usuario = usuarioRepository.findByIdWithTarjetas(id);

            if (usuario.isEmpty()) {
                return Result.error("Usuario no encontrado con ID: " + id);
            }

            return Result.success(usuario.get());
        } catch (Exception e) {
            return Result.error("Error al obtener el usuario con tarjetas: " + e.getMessage(), e);
        }
    }

    @Transactional
    public Result actualizarUsuario(Long id, UsuarioCreateDTO dto) {
        try {
            Optional<Usuario> usuarioOpt = usuarioRepository.findById(id);

            if (usuarioOpt.isEmpty()) {
                return Result.error("Usuario no encontrado con ID: " + id);
            }

            Usuario usuario = usuarioOpt.get();

            // Validar que el email no esté registrado por otro usuario
            if (!usuario.getEmail().equals(dto.getEmail())
                    && usuarioRepository.existsByEmail(dto.getEmail())) {
                return Result.error("Ya existe otro usuario con ese email");
            }

            usuario.setNombre(dto.getNombre());
            usuario.setApellido(dto.getApellido());
            usuario.setEmail(dto.getEmail());

            Usuario usuarioActualizado = usuarioRepository.save(usuario);
            return Result.success(usuarioActualizado);

        } catch (Exception e) {
            return Result.error("Error al actualizar el usuario: " + e.getMessage(), e);
        }
    }

    @Transactional
    public Result eliminarUsuario(Long id) {
        try {
            Optional<Usuario> usuarioOpt = usuarioRepository.findById(id);

            if (usuarioOpt.isEmpty()) {
                return Result.error("Usuario no encontrado con ID: " + id);
            }

            Usuario usuario = usuarioOpt.get();
            usuario.setActivo(false);

            // Desactivar todas las tarjetas del usuario
            usuario.getTarjetas().forEach(tarjeta -> tarjeta.setActiva(false));

            usuarioRepository.save(usuario);
            return Result.success();

        } catch (Exception e) {
            return Result.error("Error al eliminar el usuario: " + e.getMessage(), e);
        }
    }

    public Result buscarPorNombre(String busqueda) {
        try {
            List<Usuario> usuarios = usuarioRepository.buscarPorNombreOApellido(busqueda);
            List<Object> resultado = new ArrayList<>(usuarios);
            return Result.success(resultado);
        } catch (Exception e) {
            return Result.error("Error al buscar usuarios: " + e.getMessage(), e);
        }
    }

    public Result obtenerActivos() {
        try {
            List<Usuario> usuarios = usuarioRepository.findByActivo(true);
            List<Object> resultado = new ArrayList<>(usuarios);
            return Result.success(resultado);
        } catch (Exception e) {
            return Result.error("Error al obtener usuarios activos: " + e.getMessage(), e);
        }
    }
}
