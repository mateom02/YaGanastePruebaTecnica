package com.prueba.tarjetas.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.prueba.tarjetas.dto.TarjetaCreateDTO;
import com.prueba.tarjetas.service.TarjetaService;
import com.prueba.tarjetas.util.Result;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Tarjetas", description = "Gestión de tarjetas débito, crédito y nómina")
@RestController
@RequestMapping("/api/tarjetas")
@CrossOrigin(origins = "*")
public class TarjetaController {

    @Autowired
    private TarjetaService tarjetaService;

    @Operation(
            summary = "Crear tarjeta",
            description = "Registra una nueva tarjeta (DEBITO, CREDITO o NOMINA) asociada a un usuario existente."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Tarjeta creada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos o formato de fecha incorrecto"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
        @ApiResponse(responseCode = "409", description = "El número de tarjeta ya está registrado"),
        @ApiResponse(responseCode = "422", description = "Tipo de tarjeta no soportado")
    })
    @PostMapping
    public ResponseEntity<Result> crearTarjeta(@Valid @RequestBody TarjetaCreateDTO dto) {
        Result resultado = tarjetaService.crearTarjeta(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(resultado);
    }

    @Operation(
            summary = "Obtener todas las tarjetas",
            description = "Retorna la lista completa de tarjetas registradas en el sistema."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente"),
        @ApiResponse(responseCode = "204", description = "No hay tarjetas registradas")
    })
    @GetMapping
    public ResponseEntity<Result> obtenerTodas() {
        Result resultado = tarjetaService.obtenerTodas();
        return ResponseEntity.ok(resultado);
    }

    @Operation(
            summary = "Obtener tarjetas por usuario",
            description = "Retorna todas las tarjetas asociadas a un usuario específico."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Tarjetas obtenidas exitosamente"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
        @ApiResponse(responseCode = "204", description = "El usuario no tiene tarjetas registradas")
    })
    @GetMapping("/usuario/{id}")
    public ResponseEntity<Result> obtenerPorUsuario(
            @Parameter(description = "ID del usuario a consultar", example = "1")
            @PathVariable Long id) {
        Result resultado = tarjetaService.obtenerPorUsuario(id);
        return ResponseEntity.ok(resultado);
    }
}
