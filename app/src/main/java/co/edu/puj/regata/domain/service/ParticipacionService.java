package co.edu.puj.regata.domain.service;

import co.edu.puj.regata.domain.entity.Participacion;
import co.edu.puj.regata.domain.repo.ParticipacionRepo;
import co.edu.puj.regata.domain.exception.EntityNotFoundException;
import co.edu.puj.regata.domain.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ParticipacionService {

    @Autowired
    private ParticipacionRepo participacionRepo;

    public List<Participacion> listAll() {
        return participacionRepo.findAll();
    }

    public Participacion findById(Long id) {
        if (id == null) {
            throw new ValidationException("ID cannot be null");
        }
        return participacionRepo.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Participacion", id));
    }

    public Optional<Participacion> findByIdOptional(Long id) {
        if (id == null) {
            throw new ValidationException("ID cannot be null");
        }
        return participacionRepo.findById(id);
    }

    public Participacion save(Participacion participacion) {
        validateParticipacion(participacion);
        return participacionRepo.save(participacion);
    }

    public void delete(Long id) {
        if (id == null) {
            throw new ValidationException("ID cannot be null");
        }
        if (!participacionRepo.existsById(id)) {
            throw new EntityNotFoundException("Participacion", id);
        }
        participacionRepo.deleteById(id);
    }

    private void validateParticipacion(Participacion participacion) {
        if (participacion == null) {
            throw new ValidationException("Participacion cannot be null");
        }
        if (participacion.getBarco() == null) {
            throw new ValidationException("barco", "null", "cannot be null");
        }
        if (participacion.getPartida() == null) {
            throw new ValidationException("partida", "null", "cannot be null");
        }
        if (participacion.getPosX() < 0) {
            throw new ValidationException("posX", String.valueOf(participacion.getPosX()), "must be greater than or equal to 0");
        }
        if (participacion.getPosY() < 0) {
            throw new ValidationException("posY", String.valueOf(participacion.getPosY()), "must be greater than or equal to 0");
        }
    }

    public List<Participacion> findByPartidaId(Long partidaId) {
        return participacionRepo.findByPartidaId(partidaId);
    }
}