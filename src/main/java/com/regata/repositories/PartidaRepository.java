package com.regata.repositories;

import com.regata.entities.EstadoPartida;
import com.regata.entities.Partida;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PartidaRepository extends JpaRepository<Partida, Long> {
    
    // Buscar partidas por estado
    List<Partida> findByEstado(String estado);
    
    // Buscar partidas que están esperando jugadores
    @Query("SELECT p FROM Partida p WHERE p.estado = 'ESPERANDO' ORDER BY p.fechaCreacion DESC")
    List<Partida> findPartidasEsperando();
    
    // Buscar partidas en juego
    @Query("SELECT p FROM Partida p WHERE p.estado = 'EN_JUEGO' ORDER BY p.fechaInicio DESC")
    List<Partida> findPartidasEnJuego();
    
    // Buscar partidas terminadas
    @Query("SELECT p FROM Partida p WHERE p.estado = 'TERMINADA' ORDER BY p.fechaFin DESC")
    List<Partida> findPartidasTerminadas();
    
    // Buscar partidas por nombre (búsqueda parcial)
    List<Partida> findByNombreContainingIgnoreCase(String nombre);
    
    // Verificar si existe una partida con el nombre dado
    boolean existsByNombre(String nombre);
    
    // Buscar partidas donde un jugador específico está participando
    @Query("SELECT DISTINCT p FROM Partida p JOIN p.participantes pp WHERE pp.jugador.id = :jugadorId")
    List<Partida> findPartidasByJugadorId(@Param("jugadorId") Long jugadorId);
    
    // Contar partidas por estado
    @Query("SELECT COUNT(p) FROM Partida p WHERE p.estado = :estado")
    Long countByEstado(@Param("estado") EstadoPartida estado);
    
    // Buscar partidas que pueden unirse (esperando y con espacio)
    @Query("SELECT p FROM Partida p WHERE p.estado = 'ESPERANDO' AND " +
           "(SELECT COUNT(pp) FROM ParticipantePartida pp WHERE pp.partida.id = p.id) < p.maxJugadores " +
           "ORDER BY p.fechaCreacion DESC")
    List<Partida> findPartidasDisponibles();
    
    // Buscar la partida más reciente de un jugador
    @Query("SELECT p FROM Partida p JOIN p.participantes pp WHERE pp.jugador.id = :jugadorId " +
           "ORDER BY p.fechaCreacion DESC")
    Optional<Partida> findUltimaPartidaByJugadorId(@Param("jugadorId") Long jugadorId);
}