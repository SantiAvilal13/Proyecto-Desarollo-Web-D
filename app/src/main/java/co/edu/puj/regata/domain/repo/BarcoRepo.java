package co.edu.puj.regata.domain.repo;

import co.edu.puj.regata.domain.entity.Barco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface BarcoRepo extends JpaRepository<Barco, Long> {
    List<Barco> findByJugadorId(Long jugadorId);
    List<Barco> findByModeloId(Long modeloId);
    List<Barco> findByJugadorIdAndModeloId(Long jugadorId, Long modeloId);
    
    @Query("SELECT COUNT(p) FROM Participacion p WHERE p.barco.id = :barcoId")
    long countParticipacionesByBarcoId(@Param("barcoId") Long barcoId);
}