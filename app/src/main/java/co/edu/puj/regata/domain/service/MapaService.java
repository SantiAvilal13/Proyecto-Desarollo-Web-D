package co.edu.puj.regata.domain.service;

import co.edu.puj.regata.domain.entity.Mapa;
import co.edu.puj.regata.domain.entity.Celda;
import co.edu.puj.regata.domain.repo.MapaRepo;
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
public class MapaService {

    @Autowired
    private MapaRepo mapaRepo;

    @Autowired
    private CeldaRepo celdaRepo;

    public List<Mapa> listAll() {
        return mapaRepo.findAll();
    }

    public Mapa findById(Long id) {
        if (id == null) {
            throw new ValidationException("ID cannot be null");
        }
        return mapaRepo.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Mapa", id));
    }

    public Optional<Mapa> findByIdOptional(Long id) {
        if (id == null) {
            throw new ValidationException("ID cannot be null");
        }
        return mapaRepo.findById(id);
    }

    public Mapa save(Mapa mapa) {
        validateMapa(mapa);
        return mapaRepo.save(mapa);
    }

    public void delete(Long id) {
        if (id == null) {
            throw new ValidationException("ID cannot be null");
        }
        if (!mapaRepo.existsById(id)) {
            throw new EntityNotFoundException("Mapa", id);
        }
        mapaRepo.deleteById(id);
    }

    private void validateMapa(Mapa mapa) {
        if (mapa == null) {
            throw new ValidationException("Mapa cannot be null");
        }
        if (mapa.getNombre() == null || mapa.getNombre().trim().isEmpty()) {
            throw new ValidationException("nombre", mapa.getNombre(), "cannot be null or empty");
        }
        if (mapa.getNombre().length() > 50) {
            throw new ValidationException("nombre", mapa.getNombre(), "cannot exceed 50 characters");
        }
        if (mapa.getTamColumnas() <= 0) {
            throw new ValidationException("tamColumnas", String.valueOf(mapa.getTamColumnas()), "must be greater than 0");
        }
        if (mapa.getTamFilas() <= 0) {
            throw new ValidationException("tamFilas", String.valueOf(mapa.getTamFilas()), "must be greater than 0");
        }
    }

    public List<Celda> getCeldasDeMapa(Long idMapa) {
        return celdaRepo.findByMapaId(idMapa);  // Recupera todas las celdas asociadas al mapa
    }
}