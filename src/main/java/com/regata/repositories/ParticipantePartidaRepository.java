package com.regata.repositories;

import com.regata.entities.ParticipantePartida;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParticipantePartidaRepository extends JpaRepository<ParticipantePartida, Long> {
    
    // Buscar participantes de una partida específica
    List<ParticipantePartida> findByPartidaId(Long partidaId);
    
    // Buscar participantes de una partida ordenados por turno
    @Query("SELECT pp FROM ParticipantePartida pp WHERE pp.partida.id = :partidaId ORDER BY pp.ordenTurno")
    List<ParticipantePartida> findByPartidaIdOrderByOrdenTurno(@Param("partidaId") Long partidaId);
    
    // Buscar participante específico en una partida
    Optional<ParticipantePartida> findByPartidaIdAndJugadorId(Long partidaId, Long jugadorId);
    
    // Verificar si un jugador ya está en una partida
    boolean existsByPartidaIdAndJugadorId(Long partidaId, Long jugadorId);
    
    // Contar participantes en una partida
    @Query("SELECT COUNT(pp) FROM ParticipantePartida pp WHERE pp.partida.id = :partidaId")
    Long countByPartidaId(@Param("partidaId") Long partidaId);
    
    // Buscar participantes que han llegado a la meta
    @Query("SELECT pp FROM ParticipantePartida pp WHERE pp.partida.id = :partidaId AND pp.haLlegadoMeta = true ORDER BY pp.posicionFinal")
    List<ParticipantePartida> findGanadoresByPartidaId(@Param("partidaId") Long partidaId);
    
    // Buscar el siguiente participante en el turno
    @Query("SELECT pp FROM ParticipantePartida pp WHERE pp.partida.id = :partidaId AND pp.ordenTurno = :ordenTurno")
    Optional<ParticipantePartida> findByPartidaIdAndOrdenTurno(@Param("partidaId") Long partidaId, @Param("ordenTurno") Integer ordenTurno);
    
    // Buscar participantes que no han llegado a la meta
    @Query("SELECT pp FROM ParticipantePartida pp WHERE pp.partida.id = :partidaId AND pp.haLlegadoMeta = false ORDER BY pp.ordenTurno")
    List<ParticipantePartida> findParticipantesEnJuegoByPartidaId(@Param("partidaId") Long partidaId);
    
    // Buscar todas las partidas de un jugador
    List<ParticipantePartida> findByJugadorId(Long jugadorId);
    
    // Buscar el máximo orden de turno en una partida
    @Query("SELECT MAX(pp.ordenTurno) FROM ParticipantePartida pp WHERE pp.partida.id = :partidaId")
    Optional<Integer> findMaxOrdenTurnoByPartidaId(@Param("partidaId") Long partidaId);
    
    // Eliminar participante de una partida
    void deleteByPartidaIdAndJugadorId(Long partidaId, Long jugadorId);
}