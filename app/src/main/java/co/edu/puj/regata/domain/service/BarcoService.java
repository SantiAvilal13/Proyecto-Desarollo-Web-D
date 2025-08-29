package co.edu.puj.regata.domain.service;

import co.edu.puj.regata.domain.entity.Barco;
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
public class BarcoService {

    @Autowired
    private BarcoRepo barcoRepo;

    public List<Barco> listAll() {
        return barcoRepo.findAll();
    }
    
    public List<Barco> findByJugadorId(Long jugadorId) {
        return barcoRepo.findByJugadorId(jugadorId);
    }
    
    public List<Barco> findByModeloId(Long modeloId) {
        return barcoRepo.findByModeloId(modeloId);
    }
    
    public List<Barco> findByJugadorIdAndModeloId(Long jugadorId, Long modeloId) {
        return barcoRepo.findByJugadorIdAndModeloId(jugadorId, modeloId);
    }

    public Barco findById(Long id) {
        if (id == null) {
            throw new ValidationException("ID cannot be null");
        }
        return barcoRepo.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Barco", id));
    }

    public Optional<Barco> findByIdOptional(Long id) {
        if (id == null) {
            throw new ValidationException("ID cannot be null");
        }
        return barcoRepo.findById(id);
    }

    public Barco save(Barco barco) {
        validateBarco(barco);
        return barcoRepo.save(barco);
    }

    public void delete(Long id) {
        if (id == null) {
            throw new ValidationException("ID cannot be null");
        }
        if (!barcoRepo.existsById(id)) {
            throw new EntityNotFoundException("Barco", id);
        }
        
        // Verificar si está asociado a participaciones
        long participaciones = barcoRepo.countParticipacionesByBarcoId(id);
        if (participaciones > 0) {
            throw new ValidationException("No se puede eliminar el barco porque está asociado a " + participaciones + " participación(es) en partidas");
        }
        
        barcoRepo.deleteById(id);
    }

    private void validateBarco(Barco barco) {
        if (barco == null) {
            throw new ValidationException("Barco cannot be null");
        }
        if (barco.getModelo() == null) {
            throw new ValidationException("modelo", "null", "cannot be null");
        }
        if (barco.getJugador() == null) {
            throw new ValidationException("jugador", "null", "cannot be null");
        }
    }
}