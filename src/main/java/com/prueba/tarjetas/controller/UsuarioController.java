package com.prueba.tarjetas.controller;

import com.prueba.tarjetas.dto.UsuarioCreateDTO;
import com.prueba.tarjetas.service.UsuarioService;
import com.prueba.tarjetas.util.Result;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {
    
    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<Result> crearUsuario(@Valid @RequestBody UsuarioCreateDTO dto, 
                                                BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errores = bindingResult.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .reduce("", (a, b) -> a + "; " + b);
            return ResponseEntity.badRequest().body(Result.error("Errores de validación: " + errores));
        }
        
        Result resultado = usuarioService.crearUsuario(dto);
        
        if (resultado.correct) {
            return ResponseEntity.status(HttpStatus.CREATED).body(resultado);
        } else {
            return ResponseEntity.badRequest().body(resultado);
        }
    }
    
    //validacion si no existen registradas
    @GetMapping
    public ResponseEntity<Result> obtenerTodos() {
        Result resultado = usuarioService.obtenerTodos();
        return ResponseEntity.ok(resultado);
    }
    
//validacion por si no hay existentes
    @GetMapping("/{id}")
    public ResponseEntity<Result> obtenerPorId(@PathVariable Long id) {
        Result resultado = usuarioService.obtenerPorId(id);
        
        if (resultado.correct) {
            return ResponseEntity.ok(resultado);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resultado);
        }
    }
    
    
    @PutMapping("/{id}")
    public ResponseEntity<Result> actualizarUsuario(@PathVariable Long id,
                                                     @Valid @RequestBody UsuarioCreateDTO dto,
                                                     BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errores = bindingResult.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .reduce("", (a, b) -> a + "; " + b);
            return ResponseEntity.badRequest().body(Result.error("Errores de validación: " + errores));
        }
        
        Result resultado = usuarioService.actualizarUsuario(id, dto);
        
        if (resultado.correct) {
            return ResponseEntity.ok(resultado);
        } else {
            return ResponseEntity.badRequest().body(resultado);
        }
    }
    

}
