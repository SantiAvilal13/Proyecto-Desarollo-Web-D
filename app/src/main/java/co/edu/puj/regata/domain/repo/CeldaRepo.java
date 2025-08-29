package co.edu.puj.regata.domain.repo;

import co.edu.puj.regata.domain.entity.Celda;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CeldaRepo extends JpaRepository<Celda, Long> {
    List<Celda> findByMapaId(Long mapaId);
    Optional<Celda> findByMapaIdAndXAndY(Long mapaId, int x, int y);
}