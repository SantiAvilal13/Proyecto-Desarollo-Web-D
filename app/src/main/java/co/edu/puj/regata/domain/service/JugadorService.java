package co.edu.puj.regata.domain.service;

import co.edu.puj.regata.domain.entity.Jugador;
import co.edu.puj.regata.domain.entity.Barco;
import co.edu.puj.regata.domain.repo.JugadorRepo;
import co.edu.puj.regata.domain.repo.BarcoRepo;
import co.edu.puj.regata.domain.exception.EntityNotFoundException;
import co.edu.puj.regata.domain.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class JugadorService {

    @Autowired
    private JugadorRepo jugadorRepo;
    
    @Autowired
    private BarcoRepo barcoRepo;

    public List<Jugador> listAll() {
        return jugadorRepo.findAll();
    }

    public Jugador findById(Long id) {
        if (id == null) {
            throw new ValidationException("ID cannot be null");
        }
        return jugadorRepo.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Jugador", id));
    }

    public Optional<Jugador> findByIdOptional(Long id) {
        if (id == null) {
            throw new ValidationException("ID cannot be null");
        }
        return jugadorRepo.findById(id);
    }

    public Jugador save(Jugador jugador) {
        validateJugador(jugador);
        return jugadorRepo.save(jugador);
    }

    public void delete(Long id) {
        if (id == null) {
            throw new ValidationException("ID cannot be null");
        }
        if (!jugadorRepo.existsById(id)) {
            throw new EntityNotFoundException("Jugador", id);
        }
        
        // Verificar si tiene barcos asociados
        List<Barco> barcosAsociados = barcoRepo.findByJugadorId(id);
        if (!barcosAsociados.isEmpty()) {
            throw new ValidationException("No se puede eliminar el jugador porque tiene " + barcosAsociados.size() + " barco(s) asociado(s)");
        }
        
        jugadorRepo.deleteById(id);
    }

    private void validateJugador(Jugador jugador) {
        if (jugador == null) {
            throw new ValidationException("Jugador cannot be null");
        }
        if (jugador.getNombre() == null || jugador.getNombre().trim().isEmpty()) {
            throw new ValidationException("nombre", jugador.getNombre(), "cannot be null or empty");
        }
        if (jugador.getNombre().length() > 50) {
            throw new ValidationException("nombre", jugador.getNombre(), "cannot exceed 50 characters");
        }
        
        // Validar nombre único
        Optional<Jugador> existingJugador = jugadorRepo.findByNombre(jugador.getNombre());
        if (existingJugador.isPresent() && !existingJugador.get().getId().equals(jugador.getId())) {
            throw new ValidationException("nombre", jugador.getNombre(), "ya existe otro jugador con este nombre");
        }
        
        if (jugador.getContraseña() == null || jugador.getContraseña().trim().isEmpty()) {
            throw new ValidationException("contraseña", jugador.getContraseña(), "cannot be null or empty");
        }
        if (jugador.getRol() == null) {
            throw new ValidationException("rol", "null", "cannot be null");
        }
    }
}