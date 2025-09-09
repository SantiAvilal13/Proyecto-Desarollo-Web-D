package com.regata.services;

import com.regata.entities.Barco;
import com.regata.entities.Jugador;
import com.regata.entities.Modelo;
import com.regata.repositories.BarcoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BarcoService {
    
    @Autowired
    private BarcoRepository barcoRepository;
    
    @Autowired
    private JugadorService jugadorService;
    
    @Autowired
    private ModeloService modeloService;
    
    public List<Barco> findAll() {
        return barcoRepository.findAllWithJugadorAndModelo();
    }
    
    public Optional<Barco> findById(Long id) {
        return barcoRepository.findByIdWithJugadorAndModelo(id);
    }
    
    public Barco save(Barco barco) {
        // Validar que el jugador existe
        if (barco.getJugador() == null || !jugadorService.existsById(barco.getJugador().getId())) {
            throw new RuntimeException("El jugador especificado no existe");
        }
        
        // Validar que el modelo existe
        if (barco.getModelo() == null || !modeloService.existsById(barco.getModelo().getId())) {
            throw new RuntimeException("El modelo especificado no existe");
        }
        
        // Validar posiciones
        if (barco.getPosX() < 0 || barco.getPosY() < 0) {
            throw new RuntimeException("Las posiciones deben ser mayores o iguales a 0");
        }
        
        return barcoRepository.save(barco);
    }
    
    public void deleteById(Long id) {
        if (!barcoRepository.existsById(id)) {
            throw new RuntimeException("Barco no encontrado con ID: " + id);
        }
        barcoRepository.deleteById(id);
    }
    
    public boolean existsById(Long id) {
        return barcoRepository.existsById(id);
    }
    
    public List<Barco> findByJugador(Jugador jugador) {
        return barcoRepository.findByJugador(jugador);
    }
    
    public List<Barco> findByJugadorId(Long jugadorId) {
        return barcoRepository.findByJugadorId(jugadorId);
    }
    
    public List<Barco> findByModelo(Modelo modelo) {
        return barcoRepository.findByModelo(modelo);
    }
    
    public List<Barco> findByModeloId(Long modeloId) {
        return barcoRepository.findByModeloId(modeloId);
    }
    
    public List<Barco> findByPosicion(Integer posX, Integer posY) {
        return barcoRepository.findByPosicion(posX, posY);
    }
    
    public Long countByJugadorId(Long jugadorId) {
        return barcoRepository.countByJugadorId(jugadorId);
    }
    
    public long count() {
        return barcoRepository.count();
    }
    
    public List<Barco> listarTodos() {
        return barcoRepository.findAll();
    }
}