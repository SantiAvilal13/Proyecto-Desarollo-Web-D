package co.edu.puj.regata.domain.service;

import co.edu.puj.regata.domain.entity.Partida;
import co.edu.puj.regata.domain.repo.PartidaRepo;
import co.edu.puj.regata.domain.exception.EntityNotFoundException;
import co.edu.puj.regata.domain.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;

@Service
@Transactional
public class PartidaService {

    @Autowired
    private PartidaRepo partidaRepo;

    public List<Partida> listAll() {
        return partidaRepo.findAll();
    }

    public Partida findById(Long id) {
        if (id == null) {
            throw new ValidationException("ID cannot be null");
        }
        return partidaRepo.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Partida", id));
    }

    public Optional<Partida> findByIdOptional(Long id) {
        if (id == null) {
            throw new ValidationException("ID cannot be null");
        }
        return partidaRepo.findById(id);
    }

    public Partida save(Partida partida) {
        validatePartida(partida);
        return partidaRepo.save(partida);
    }

    public void delete(Long id) {
        if (id == null) {
            throw new ValidationException("ID cannot be null");
        }
        if (!partidaRepo.existsById(id)) {
            throw new EntityNotFoundException("Partida", id);
        }
        
        // Verificar si tiene participaciones asociadas
        long participaciones = partidaRepo.countParticipacionesByPartidaId(id);
        if (participaciones > 0) {
            throw new ValidationException("No se puede eliminar la partida porque tiene " + participaciones + " participación(es) asociada(s)");
        }
        
        partidaRepo.deleteById(id);
    }
    
    public List<Partida> findByEstado(String estado) {
        return partidaRepo.findByEstado(estado);
    }
    
    public List<Partida> findByMapaId(Long mapaId) {
        return partidaRepo.findByMapaId(mapaId);
    }
    
    public List<Partida> findByEstadoAndMapaId(String estado, Long mapaId) {
        return partidaRepo.findByEstadoAndMapaId(estado, mapaId);
    }

    private void validatePartida(Partida partida) {
        if (partida == null) {
            throw new ValidationException("Partida cannot be null");
        }
        if (partida.getFechaInicio() != null && partida.getFechaFin() != null) {
            if (partida.getFechaInicio().isAfter(partida.getFechaFin())) {
                throw new ValidationException("fechaInicio", partida.getFechaInicio().toString(), "cannot be after fechaFin");
            }
        }
        if (partida.getMapa() == null) {
            throw new ValidationException("mapa", "null", "cannot be null");
        }
    }
}