package com.regata.services;

import com.regata.entities.Modelo;
import com.regata.repositories.ModeloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ModeloService {
    
    @Autowired
    private ModeloRepository modeloRepository;
    
    public List<Modelo> findAll() {
        return modeloRepository.findAll();
    }
    
    public Optional<Modelo> findById(Long id) {
        return modeloRepository.findById(id);
    }
    
    public Modelo save(Modelo modelo) {
        // Validar que el nombre no esté duplicado
        if (modelo.getId() == null && modeloRepository.existsByNombre(modelo.getNombre())) {
            throw new RuntimeException("Ya existe un modelo con este nombre");
        }
        
        // Si es una actualización, verificar que el nombre no esté en uso por otro modelo
        if (modelo.getId() != null) {
            Optional<Modelo> existingModelo = modeloRepository.findByNombre(modelo.getNombre());
            if (existingModelo.isPresent() && !existingModelo.get().getId().equals(modelo.getId())) {
                throw new RuntimeException("Ya existe un modelo con este nombre");
            }
        }
        
        return modeloRepository.save(modelo);
    }
    
    public void deleteById(Long id) {
        if (!modeloRepository.existsById(id)) {
            throw new RuntimeException("Modelo no encontrado con ID: " + id);
        }
        modeloRepository.deleteById(id);
    }
    
    public boolean existsById(Long id) {
        return modeloRepository.existsById(id);
    }
    
    public Optional<Modelo> findByNombre(String nombre) {
        return modeloRepository.findByNombre(nombre);
    }
    
    public List<Modelo> findByColor(String color) {
        return modeloRepository.findByColor(color);
    }
    
    public List<Modelo> findByNombreContaining(String nombre) {
        return modeloRepository.findByNombreContainingIgnoreCase(nombre);
    }
    
    public boolean existsByNombre(String nombre) {
        return modeloRepository.existsByNombre(nombre);
    }
    
    public long count() {
        return modeloRepository.count();
    }
}