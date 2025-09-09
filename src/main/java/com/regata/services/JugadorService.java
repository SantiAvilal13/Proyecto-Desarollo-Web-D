package com.regata.services;

import com.regata.entities.Jugador;
import com.regata.repositories.JugadorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class JugadorService {
    
    @Autowired
    private JugadorRepository jugadorRepository;
    
    public List<Jugador> findAll() {
        return jugadorRepository.findAll();
    }
    
    public Optional<Jugador> findById(Long id) {
        return jugadorRepository.findById(id);
    }
    
    public Jugador save(Jugador jugador) {
        // Validar que el email no esté duplicado
        if (jugador.getId() == null && jugadorRepository.existsByEmail(jugador.getEmail())) {
            throw new RuntimeException("Ya existe un jugador con este email");
        }
        
        // Si es una actualización, verificar que el email no esté en uso por otro jugador
        if (jugador.getId() != null) {
            Optional<Jugador> existingJugador = jugadorRepository.findByEmail(jugador.getEmail());
            if (existingJugador.isPresent() && !existingJugador.get().getId().equals(jugador.getId())) {
                throw new RuntimeException("Ya existe un jugador con este email");
            }
        }
        
        return jugadorRepository.save(jugador);
    }
    
    public void deleteById(Long id) {
        if (!jugadorRepository.existsById(id)) {
            throw new RuntimeException("Jugador no encontrado con ID: " + id);
        }
        jugadorRepository.deleteById(id);
    }
    
    public boolean existsById(Long id) {
        return jugadorRepository.existsById(id);
    }
    
    public Optional<Jugador> findByEmail(String email) {
        return jugadorRepository.findByEmail(email);
    }
    
    public boolean existsByEmail(String email) {
        return jugadorRepository.existsByEmail(email);
    }
    
    public Optional<Jugador> findByNombre(String nombre) {
        return jugadorRepository.findByNombreContainingIgnoreCase(nombre);
    }
    
    public long count() {
        return jugadorRepository.count();
    }
}