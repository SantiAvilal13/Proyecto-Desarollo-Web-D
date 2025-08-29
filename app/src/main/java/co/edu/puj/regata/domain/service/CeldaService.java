package co.edu.puj.regata.domain.service;

import co.edu.puj.regata.domain.entity.Celda;
import co.edu.puj.regata.domain.repo.CeldaRepo;
import co.edu.puj.regata.domain.exception.EntityNotFoundException;
import co.edu.puj.regata.domain.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CeldaService {

    @Autowired
    private CeldaRepo celdaRepo;

    public List<Celda> listAll() {
        return celdaRepo.findAll();
    }

    public Celda findById(Long id) {
        if (id == null) {
            throw new ValidationException("ID cannot be null");
        }
        return celdaRepo.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Celda", id));
    }

    public Optional<Celda> findByIdOptional(Long id) {
        if (id == null) {
            throw new ValidationException("ID cannot be null");
        }
        return celdaRepo.findById(id);
    }

    public Celda save(Celda celda) {
        validateCelda(celda);
        return celdaRepo.save(celda);
    }

    public void delete(Long id) {
        if (id == null) {
            throw new ValidationException("ID cannot be null");
        }
        if (!celdaRepo.existsById(id)) {
            throw new EntityNotFoundException("Celda", id);
        }
        celdaRepo.deleteById(id);
    }

    private void validateCelda(Celda celda) {
        if (celda == null) {
            throw new ValidationException("Celda cannot be null");
        }
        if (celda.getX() < 0) {
            throw new ValidationException("x", String.valueOf(celda.getX()), "must be greater than or equal to 0");
        }
        if (celda.getY() < 0) {
            throw new ValidationException("y", String.valueOf(celda.getY()), "must be greater than or equal to 0");
        }
        if (celda.getMapa() == null) {
            throw new ValidationException("mapa", "null", "cannot be null");
        }
    }
}