package com.regata.services;

import com.regata.entities.*;
import com.regata.repositories.PartidaRepository;
import com.regata.repositories.ParticipantePartidaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PartidaService {
    
    @Autowired
    private PartidaRepository partidaRepository;
    
    @Autowired
    private ParticipantePartidaRepository participanteRepository;
    
    @Autowired
    private JugadorService jugadorService;
    
    @Autowired
    private BarcoService barcoService;
    
    @Autowired
    private MapaService mapaService;
    
    // CRUD básico
    public List<Partida> findAll() {
        return partidaRepository.findAll();
    }
    
    public Optional<Partida> findById(Long id) {
        return partidaRepository.findById(id);
    }
    
    public Partida save(Partida partida) {
        if (partida.getMapa() == null) {
            // Asignar mapa por defecto
            Optional<Mapa> mapaDefault = mapaService.findAll().stream().findFirst();
            if (mapaDefault.isPresent()) {
                partida.setMapa(mapaDefault.get());
            } else {
                throw new RuntimeException("No hay mapas disponibles");
            }
        }
        return partidaRepository.save(partida);
    }
    
    public void deleteById(Long id) {
        if (!partidaRepository.existsById(id)) {
            throw new RuntimeException("La partida con ID " + id + " no existe");
        }
        partidaRepository.deleteById(id);
    }
    
    public boolean existsById(Long id) {
        return partidaRepository.existsById(id);
    }
    
    public boolean existsByNombre(String nombre) {
        return partidaRepository.existsByNombre(nombre);
    }
    
    // Métodos específicos del negocio
    public List<Partida> findPartidasDisponibles() {
        return partidaRepository.findPartidasDisponibles();
    }
    
    public List<Partida> findPartidasEnJuego() {
        return partidaRepository.findPartidasEnJuego();
    }
    
    public List<Partida> findPartidasTerminadas() {
        return partidaRepository.findPartidasTerminadas();
    }
    
    public List<Partida> findPartidasByJugadorId(Long jugadorId) {
        return partidaRepository.findPartidasByJugadorId(jugadorId);
    }
    
    // Gestión de participantes
    public ParticipantePartida unirseAPartida(Long partidaId, Long jugadorId, Long barcoId) {
        Partida partida = findById(partidaId)
            .orElseThrow(() -> new RuntimeException("Partida no encontrada"));
        
        if (!partida.getEstado().name().equals("ESPERANDO")) {
            throw new RuntimeException("No se puede unir a una partida que no está esperando jugadores");
        }
        
        if (participanteRepository.existsByPartidaIdAndJugadorId(partidaId, jugadorId)) {
            throw new RuntimeException("El jugador ya está en esta partida");
        }
        
        Long cantidadParticipantes = participanteRepository.countByPartidaId(partidaId);
        if (cantidadParticipantes >= partida.getMaxJugadores()) {
            throw new RuntimeException("La partida está llena");
        }
        
        Jugador jugador = jugadorService.findById(jugadorId)
            .orElseThrow(() -> new RuntimeException("Jugador no encontrado"));
        
        Barco barco = barcoService.findById(barcoId)
            .orElseThrow(() -> new RuntimeException("Barco no encontrado"));
        
        // Determinar el orden de turno
        Optional<Integer> maxOrden = participanteRepository.findMaxOrdenTurnoByPartidaId(partidaId);
        Integer nuevoOrden = maxOrden.orElse(0) + 1;
        
        ParticipantePartida participante = new ParticipantePartida(partida, jugador, barco, nuevoOrden);
        return participanteRepository.save(participante);
    }
    
    public void salirDePartida(Long partidaId, Long jugadorId) {
        if (!participanteRepository.existsByPartidaIdAndJugadorId(partidaId, jugadorId)) {
            throw new RuntimeException("El jugador no está en esta partida");
        }
        
        Partida partida = findById(partidaId)
            .orElseThrow(() -> new RuntimeException("Partida no encontrada"));
        
        if (partida.estaEnJuego()) {
            throw new RuntimeException("No se puede salir de una partida en curso");
        }
        
        participanteRepository.deleteByPartidaIdAndJugadorId(partidaId, jugadorId);
    }
    
    // Gestión de turnos
    public Partida iniciarPartida(Long partidaId) {
        Partida partida = findById(partidaId)
            .orElseThrow(() -> new RuntimeException("Partida no encontrada"));
        
        if (!partida.puedeIniciar()) {
            throw new RuntimeException("La partida no puede iniciarse (necesita al menos 2 jugadores)");
        }
        
        List<ParticipantePartida> participantes = participanteRepository.findByPartidaIdOrderByOrdenTurno(partidaId);
        if (participantes.isEmpty()) {
            throw new RuntimeException("No hay participantes en la partida");
        }
        
        // Configurar el primer turno
        partida.setEstado(com.regata.entities.EstadoPartida.EN_JUEGO);
        partida.setFechaInicio(LocalDateTime.now());
        partida.setTurnoActual(1);
        partida.setJugadorTurnoId(participantes.get(0).getJugador().getId());
        
        return partidaRepository.save(partida);
    }
    
    public Partida siguienteTurno(Long partidaId) {
        Partida partida = findById(partidaId)
            .orElseThrow(() -> new RuntimeException("Partida no encontrada"));
        
        if (!partida.estaEnJuego()) {
            throw new RuntimeException("La partida no está en curso");
        }
        
        List<ParticipantePartida> participantesEnJuego = participanteRepository.findParticipantesEnJuegoByPartidaId(partidaId);
        
        if (participantesEnJuego.isEmpty()) {
            // Todos han llegado a la meta, terminar partida
            return terminarPartida(partidaId);
        }
        
        // Encontrar el siguiente jugador
        ParticipantePartida jugadorActual = participantesEnJuego.stream()
            .filter(p -> p.getJugador().getId().equals(partida.getJugadorTurnoId()))
            .findFirst().orElse(null);
        
        int indiceActual = jugadorActual != null ? participantesEnJuego.indexOf(jugadorActual) : -1;
        int siguienteIndice = (indiceActual + 1) % participantesEnJuego.size();
        
        ParticipantePartida siguienteJugador = participantesEnJuego.get(siguienteIndice);
        
        partida.setTurnoActual(partida.getTurnoActual() + 1);
        partida.setJugadorTurnoId(siguienteJugador.getJugador().getId());
        
        return partidaRepository.save(partida);
    }
    
    public ParticipantePartida moverJugador(Long partidaId, Long jugadorId, Integer nuevaX, Integer nuevaY) {
        Partida partida = findById(partidaId)
            .orElseThrow(() -> new RuntimeException("Partida no encontrada"));
        
        if (!partida.estaEnJuego()) {
            throw new RuntimeException("La partida no está en curso");
        }
        
        if (!partida.getJugadorTurnoId().equals(jugadorId)) {
            throw new RuntimeException("No es el turno de este jugador");
        }
        
        ParticipantePartida participante = participanteRepository.findByPartidaIdAndJugadorId(partidaId, jugadorId)
            .orElseThrow(() -> new RuntimeException("Participante no encontrado"));
        
        // Validar movimiento (aquí puedes agregar lógica de validación de movimiento)
        if (nuevaX < 0 || nuevaY < 0 || nuevaX >= 10 || nuevaY >= 10) {
            throw new RuntimeException("Movimiento fuera del mapa");
        }
        
        participante.moverA(nuevaX, nuevaY);
        
        // Verificar si llegó a la meta
        if (participante.getHaLlegadoMeta()) {
            List<ParticipantePartida> ganadores = participanteRepository.findGanadoresByPartidaId(partidaId);
            participante.setPosicionFinal(ganadores.size() + 1);
        }
        
        return participanteRepository.save(participante);
    }
    
    public Partida terminarPartida(Long partidaId) {
        Partida partida = findById(partidaId)
            .orElseThrow(() -> new RuntimeException("Partida no encontrada"));
        
        partida.setEstado(com.regata.entities.EstadoPartida.TERMINADA);
        partida.setFechaFin(LocalDateTime.now());
        
        return partidaRepository.save(partida);
    }
    
    // Métodos de consulta
    public List<ParticipantePartida> getParticipantes(Long partidaId) {
        return participanteRepository.findByPartidaIdOrderByOrdenTurno(partidaId);
    }
    
    public List<ParticipantePartida> getGanadores(Long partidaId) {
        return participanteRepository.findGanadoresByPartidaId(partidaId);
    }
    
    public Long count() {
        return partidaRepository.count();
    }
    
    public Long countByEstado(EstadoPartida estado) {
        return partidaRepository.countByEstado(estado);
    }
}