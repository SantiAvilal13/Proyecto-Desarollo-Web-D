package co.edu.puj.regata.domain.repo;

import co.edu.puj.regata.domain.entity.Partida;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface PartidaRepo extends JpaRepository<Partida, Long> {
    @Query("SELECT COUNT(p) FROM Participacion p WHERE p.partida.id = :partidaId")
    long countParticipacionesByPartidaId(@Param("partidaId") Long partidaId);
    
    @Query("SELECT p FROM Partida p WHERE p.estado = 'EN_CURSO'")
    List<Partida> findPartidasActivas();
    
    List<Partida> findByEstado(String estado);
    List<Partida> findByMapaId(Long mapaId);
    List<Partida> findByEstadoAndMapaId(String estado, Long mapaId);
}