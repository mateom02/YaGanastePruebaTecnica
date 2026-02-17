package com.prueba.tarjetas.repository;

import com.prueba.tarjetas.model.Tarjeta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TarjetaRepository extends JpaRepository<Tarjeta, Long> {

    Optional<Tarjeta> findByNumeroTarjeta(String numeroTarjeta);

    boolean existsByNumeroTarjeta(String numeroTarjeta);

    @Query("SELECT t FROM Tarjeta t WHERE t.usuario.idUsuario = :usuarioId")
    List<Tarjeta> findByUsuario(Long usuarioId);

    @Query("SELECT t FROM Tarjeta t WHERE t.usuario.idUsuario = :usuarioId AND activa= :activa")
    List<Tarjeta> findByIdUsuarioAndActiva(Long usuarioId, Boolean activa);

    @Query("SELECT t FROM Tarjeta t WHERE TYPE(t) = :tipo")
    List<Tarjeta> findByTipo(@Param("tipo") Class<? extends Tarjeta> tipo);

    @Query("SELECT t FROM Tarjeta t WHERE t.usuario.id = :usuarioId AND TYPE(t) = :tipo")
    List<Tarjeta> findByIdUsuarioAndTipo(@Param("usuarioId") Long usuarioId,
            @Param("tipo") Class<? extends Tarjeta> tipo);
}
