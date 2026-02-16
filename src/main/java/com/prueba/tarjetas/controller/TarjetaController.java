package com.prueba.tarjetas.controller;

import com.prueba.tarjetas.dto.AgregarSaldoDTO;
import com.prueba.tarjetas.dto.TarjetaCreateDTO;
import com.prueba.tarjetas.service.TarjetaService;
import com.prueba.tarjetas.util.Result;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tarjetas")
@CrossOrigin(origins = "*")
public class TarjetaController {
    
    @Autowired
    private TarjetaService tarjetaService;
    
    @PostMapping
    public ResponseEntity<Result> crearTarjeta(@Valid @RequestBody TarjetaCreateDTO dto,
                                                BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errores = bindingResult.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .reduce("", (a, b) -> a + "; " + b);
            return ResponseEntity.badRequest().body(Result.error("Errores de validación: " + errores));
        }
        
        Result resultado = tarjetaService.crearTarjeta(dto);
        
        if (resultado.correct) {
            return ResponseEntity.status(HttpStatus.CREATED).body(resultado);
        } else {
            return ResponseEntity.badRequest().body(resultado);
        }
    }
    
    @GetMapping
    public ResponseEntity<Result> obtenerTodas() {
        Result resultado = tarjetaService.obtenerTodas();
        return ResponseEntity.ok(resultado);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Result> obtenerPorId(@PathVariable Long id) {
        Result resultado = tarjetaService.obtenerPorId(id);
        
        if (resultado.correct) {
            return ResponseEntity.ok(resultado);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resultado);
        }
    }
    

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<Result> obtenerPorUsuario(@PathVariable Long usuarioId) {
        Result resultado = tarjetaService.obtenerPorUsuario(usuarioId);
        return ResponseEntity.ok(resultado);
    }
    
    
    @GetMapping("/usuario/{usuarioId}/activas")
    public ResponseEntity<Result> obtenerActivasPorUsuario(@PathVariable Long usuarioId) {
        Result resultado = tarjetaService.obtenerActivasPorUsuario(usuarioId);
        return ResponseEntity.ok(resultado);
    }
    

    @PostMapping("/{id}/agregar-saldo")
    public ResponseEntity<Result> agregarSaldo(@PathVariable Long id,
                                                @Valid @RequestBody AgregarSaldoDTO dto,
                                                BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errores = bindingResult.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .reduce("", (a, b) -> a + "; " + b);
            return ResponseEntity.badRequest().body(Result.error("Errores de validación: " + errores));
        }
        
        Result resultado = tarjetaService.agregarSaldo(id, dto);
        
        if (resultado.correct) {
            return ResponseEntity.ok(resultado);
        } else {
            return ResponseEntity.badRequest().body(resultado);
        }
    }
    

    @DeleteMapping("/{id}")
    public ResponseEntity<Result> eliminarTarjeta(@PathVariable Long id) {
        Result resultado = tarjetaService.eliminarTarjeta(id);
        
        if (resultado.correct) {
            return ResponseEntity.ok(resultado);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resultado);
        }
    }
}
