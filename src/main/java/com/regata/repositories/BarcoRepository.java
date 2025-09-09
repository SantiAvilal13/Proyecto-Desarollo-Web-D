package com.regata.repositories;

import com.regata.entities.Barco;
import com.regata.entities.Jugador;
import com.regata.entities.Modelo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BarcoRepository extends JpaRepository<Barco, Long> {
    
    List<Barco> findByJugador(Jugador jugador);
    
    List<Barco> findByModelo(Modelo modelo);
    
    List<Barco> findByJugadorId(Long jugadorId);
    
    List<Barco> findByModeloId(Long modeloId);
    
    @Query("SELECT b FROM Barco b WHERE b.posX = :posX AND b.posY = :posY")
    List<Barco> findByPosicion(@Param("posX") Integer posX, @Param("posY") Integer posY);
    
    @Query("SELECT COUNT(b) FROM Barco b WHERE b.jugador.id = :jugadorId")
    Long countByJugadorId(@Param("jugadorId") Long jugadorId);
    
    @Query("SELECT b FROM Barco b JOIN FETCH b.jugador JOIN FETCH b.modelo")
    List<Barco> findAllWithJugadorAndModelo();
    
    @Query("SELECT b FROM Barco b JOIN FETCH b.jugador JOIN FETCH b.modelo WHERE b.id = :id")
    Optional<Barco> findByIdWithJugadorAndModelo(@Param("id") Long id);
    
}