package co.edu.puj.regata.domain.repo;

import co.edu.puj.regata.domain.entity.Jugador;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface JugadorRepo extends JpaRepository<Jugador, Long> {
    Optional<Jugador> findByNombre(String nombre);
}