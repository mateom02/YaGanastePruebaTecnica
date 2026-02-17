package com.prueba.tarjetas.controller;

import com.prueba.tarjetas.dto.UsuarioCreateDTO;
import com.prueba.tarjetas.service.UsuarioService;
import com.prueba.tarjetas.util.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Usuarios", description = "Gestión de usuarios y sus tarjetas asociadas")
@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Operation(
            summary = "Crear usuario",
            description = "Registra un nuevo usuario en el sistema."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Usuario creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "409", description = "El email ya está registrado")
    })
    @PostMapping
    public ResponseEntity<Result> crearUsuario(@Valid @RequestBody UsuarioCreateDTO dto) {
        Result resultado = usuarioService.crearUsuario(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(resultado);
    }

    @Operation(
            summary = "Obtener todos los usuarios",
            description = "Retorna la lista completa de usuarios registrados."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente"),
        @ApiResponse(responseCode = "422", description = "No hay usuarios registrados")
    })
    @GetMapping
    public ResponseEntity<Result> obtenerTodos() {
        Result resultado = usuarioService.obtenerTodos();
        return ResponseEntity.ok(resultado);
    }

    @Operation(
            summary = "Obtener usuario por ID",
            description = "Retorna la información de un usuario específico junto con sus tarjetas."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Result> obtenerPorId(
            @Parameter(description = "ID del usuario a consultar", example = "1")
            @PathVariable Long id) {
        Result resultado = usuarioService.obtenerPorId(id);
        return ResponseEntity.ok(resultado);
    }

    @Operation(
            summary = "Actualizar usuario",
            description = "Actualiza los datos personales de un usuario existente."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Usuario actualizado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
        @ApiResponse(responseCode = "409", description = "El email ya pertenece a otro usuario")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Result> actualizarUsuario(
            @Parameter(description = "ID del usuario a actualizar", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody UsuarioCreateDTO dto) {
        Result resultado = usuarioService.actualizarUsuario(id, dto);
        return ResponseEntity.ok(resultado);
    }
}
