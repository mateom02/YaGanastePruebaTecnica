package com.prueba.tarjetas.repository;

import com.prueba.tarjetas.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);

    boolean existsByEmail(String email);

    List<Usuario> findByActivo(Boolean activo);

    @Query("SELECT u FROM Usuario u WHERE "
            + "LOWER(u.nombre) LIKE LOWER(CONCAT('%', :busqueda, '%')) OR "
            + "LOWER(u.apellido) LIKE LOWER(CONCAT('%', :busqueda, '%'))")
    List<Usuario> buscarPorNombreOApellido(@Param("busqueda") String busqueda);

    @Query("SELECT DISTINCT u FROM Usuario u LEFT JOIN FETCH u.tarjetas WHERE u.id = :id")
    Optional<Usuario> findByIdWithTarjetas(@Param("id") Long id);
}
