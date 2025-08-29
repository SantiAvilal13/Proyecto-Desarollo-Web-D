package co.edu.puj.regata.domain.repo;

import co.edu.puj.regata.domain.entity.Mapa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MapaRepo extends JpaRepository<Mapa, Long> {
    // Consultas personalizadas si son necesarias
}