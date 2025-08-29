package co.edu.puj.regata.domain.service;

import co.edu.puj.regata.domain.entity.Modelo;
import co.edu.puj.regata.domain.entity.Barco;
import co.edu.puj.regata.domain.repo.ModeloRepo;
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
public class ModeloService {

    @Autowired
    private ModeloRepo modeloRepo;
    
    @Autowired
    private BarcoRepo barcoRepo;

    public List<Modelo> listAll() {
        return modeloRepo.findAll();
    }

    public Modelo findById(Long id) {
        if (id == null) {
            throw new ValidationException("ID cannot be null");
        }
        return modeloRepo.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Modelo", id));
    }

    public Optional<Modelo> findByIdOptional(Long id) {
        if (id == null) {
            throw new ValidationException("ID cannot be null");
        }
        return modeloRepo.findById(id);
    }

    public Modelo save(Modelo modelo) {
        validateModelo(modelo);
        return modeloRepo.save(modelo);
    }

    public void delete(Long id) {
        if (id == null) {
            throw new ValidationException("ID cannot be null");
        }
        if (!modeloRepo.existsById(id)) {
            throw new EntityNotFoundException("Modelo", id);
        }
        
        // Verificar si tiene barcos asociados
        List<Barco> barcosAsociados = barcoRepo.findByModeloId(id);
        if (!barcosAsociados.isEmpty()) {
            throw new ValidationException("No se puede eliminar el modelo porque tiene " + barcosAsociados.size() + " barco(s) asociado(s)");
        }
        
        modeloRepo.deleteById(id);
    }

    private void validateModelo(Modelo modelo) {
        if (modelo == null) {
            throw new ValidationException("Modelo cannot be null");
        }
        if (modelo.getNombre() == null || modelo.getNombre().trim().isEmpty()) {
            throw new ValidationException("nombre", modelo.getNombre(), "cannot be null or empty");
        }
        if (modelo.getNombre().length() > 50) {
            throw new ValidationException("nombre", modelo.getNombre(), "cannot exceed 50 characters");
        }
        
        // Validar nombre único
        Optional<Modelo> existingModelo = modeloRepo.findByNombre(modelo.getNombre());
        if (existingModelo.isPresent() && !existingModelo.get().getId().equals(modelo.getId())) {
            throw new ValidationException("nombre", modelo.getNombre(), "ya existe otro modelo con este nombre");
        }
        
        if (modelo.getColor() == null || modelo.getColor().trim().isEmpty()) {
            throw new ValidationException("color", modelo.getColor(), "cannot be null or empty");
        }
        if (!modelo.getColor().matches("^#[0-9A-Fa-f]{6}$")) {
            throw new ValidationException("color", modelo.getColor(), "must be a valid hexadecimal color (#RRGGBB)");
        }
    }
}