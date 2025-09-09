package com.regata.services;

import com.regata.entities.Mapa;
import com.regata.entities.Celda;
import com.regata.repositories.MapaRepository;
import com.regata.repositories.CeldaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MapaService {
    
    @Autowired
    private MapaRepository mapaRepository;
    
    @Autowired
    private CeldaRepository celdaRepository;
    
    public List<Mapa> findAll() {
        return mapaRepository.findAll();
    }
    
    public Optional<Mapa> findById(Long id) {
        return mapaRepository.findById(id);
    }
    
    public Mapa save(Mapa mapa) {
        // Validar que el nombre no esté duplicado
        if (mapa.getId() == null && mapaRepository.existsByNombre(mapa.getNombre())) {
            throw new RuntimeException("Ya existe un mapa con este nombre");
        }
        
        // Si es una actualización, verificar que el nombre no esté en uso por otro mapa
        if (mapa.getId() != null) {
            Optional<Mapa> existingMapa = mapaRepository.findByNombre(mapa.getNombre());
            if (existingMapa.isPresent() && !existingMapa.get().getId().equals(mapa.getId())) {
                throw new RuntimeException("Ya existe un mapa con este nombre");
            }
        }
        
        return mapaRepository.save(mapa);
    }
    
    public void deleteById(Long id) {
        if (!mapaRepository.existsById(id)) {
            throw new RuntimeException("Mapa no encontrado con ID: " + id);
        }
        mapaRepository.deleteById(id);
    }
    
    public boolean existsById(Long id) {
        return mapaRepository.existsById(id);
    }
    
    public Optional<Mapa> findByNombre(String nombre) {
        return mapaRepository.findByNombre(nombre);
    }
    
    public boolean existsByNombre(String nombre) {
        return mapaRepository.existsByNombre(nombre);
    }
    
    public List<Celda> getCeldasByMapaId(Long mapaId) {
        return celdaRepository.findByMapaIdOrderedByCoordinates(mapaId);
    }
    
    public Optional<Celda> getCeldaByCoordinates(Long mapaId, Integer coordX, Integer coordY) {
        return celdaRepository.findByMapaIdAndCoordenadas(mapaId, coordX, coordY);
    }
    
    public long count() {
        return mapaRepository.count();
    }
    
    public Mapa obtenerMapaPrincipal() {
        List<Mapa> mapas = mapaRepository.findAll();
        if (mapas.isEmpty()) {
            throw new RuntimeException("No hay mapas disponibles");
        }
        return mapas.get(0); // Retorna el primer mapa como principal
    }
    
    public List<Celda> obtenerCeldasDelMapa(Long mapaId) {
        return celdaRepository.findByMapaId(mapaId);
    }
}