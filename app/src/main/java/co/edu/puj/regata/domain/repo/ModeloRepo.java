package co.edu.puj.regata.domain.repo;

import co.edu.puj.regata.domain.entity.Modelo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ModeloRepo extends JpaRepository<Modelo, Long> {
    Optional<Modelo> findByNombre(String nombre);
}