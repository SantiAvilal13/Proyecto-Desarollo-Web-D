package co.edu.puj.regata.domain.repo;

import co.edu.puj.regata.domain.entity.Participacion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ParticipacionRepo extends JpaRepository<Participacion, Long> {
    List<Participacion> findByPartidaId(Long partidaId);
    Optional<Participacion> findByBarcoId(Long barcoId);
}