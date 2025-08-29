package co.edu.puj.regata.domain.service;

import co.edu.puj.regata.domain.entity.*;
import co.edu.puj.regata.domain.repo.*;
import co.edu.puj.regata.domain.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@Transactional
public class JuegoService {

    private static final Logger logger = LoggerFactory.getLogger(JuegoService.class);
    
    @Autowired
    private PartidaRepo partidaRepo;
    
    @Autowired
    private ParticipacionRepo participacionRepo;
    
    @Autowired
    private BarcoRepo barcoRepo;
    
    @Autowired
    private CeldaRepo celdaRepo;
    
    @Autowired
    private MapaRepo mapaRepo;

    /**
     * Realiza un movimiento de barco según las reglas del juego
     * @param barcoId ID del barco a mover
     * @param cambioVx Cambio en velocidad X (-1, 0, +1)
     * @param cambioVy Cambio en velocidad Y (-1, 0, +1)
     * @param jugadorId ID del jugador que hace el movimiento
     * @return true si el movimiento fue exitoso
     */
    public boolean realizarMovimiento(Long barcoId, int cambioVx, int cambioVy, Long jugadorId) {
        // Validar cambios de velocidad
        if (Math.abs(cambioVx) > 1 || Math.abs(cambioVy) > 1) {
            throw new ValidationException("Los cambios de velocidad solo pueden ser -1, 0 o +1");
        }
        
        // Obtener el barco
        Optional<Barco> barcoOpt = barcoRepo.findById(barcoId);
        if (!barcoOpt.isPresent()) {
            throw new ValidationException("Barco no encontrado");
        }
        
        Barco barco = barcoOpt.get();
        
        // Verificar que el jugador es dueño del barco
        if (!barco.getJugador().getId().equals(jugadorId)) {
            throw new ValidationException("No tienes permiso para mover este barco");
        }
        
        // Verificar que el barco está vivo
        if (barco.getEstado() != EstadoBarco.VIVO) {
            throw new ValidationException("El barco no puede moverse (está destruido o llegó a la meta)");
        }
        
        // Obtener participación actual
        Optional<Participacion> participacionOpt = participacionRepo.findByBarcoId(barcoId);
        if (!participacionOpt.isPresent()) {
            throw new ValidationException("Participación no encontrada");
        }
        
        Participacion participacion = participacionOpt.get();
        
        // Verificar que la partida está activa
        if (participacion.getPartida().getEstado() != Partida.EstadoPartida.EN_CURSO) {
            throw new ValidationException("La partida no está activa");
        }
        
        // Actualizar velocidad del barco
        int nuevaVx = barco.getVelocidadX() + cambioVx;
        int nuevaVy = barco.getVelocidadY() + cambioVy;
        
        barco.setVelocidadX(nuevaVx);
        barco.setVelocidadY(nuevaVy);
        
        // Calcular nueva posición
        int nuevaPosX = participacion.getPosX() + nuevaVx;
        int nuevaPosY = participacion.getPosY() + nuevaVy;
        
        // Verificar colisiones y límites del mapa
        Mapa mapa = participacion.getPartida().getMapa();
        if (nuevaPosX < 0 || nuevaPosX >= mapa.getTamColumnas() || 
            nuevaPosY < 0 || nuevaPosY >= mapa.getTamFilas()) {
            // Fuera de límites - destruir barco
            barco.setEstado(EstadoBarco.DESTRUIDO);
            participacion.setEliminado(true);
            logger.info("Barco {} destruido por salir de los límites del mapa", barcoId);
            return false;
        }
        
        // Verificar tipo de celda en la nueva posición
        Optional<Celda> celdaOpt = celdaRepo.findByMapaIdAndXAndY(
            mapa.getId(), nuevaPosX, nuevaPosY);
        
        if (celdaOpt.isPresent()) {
            Celda celda = celdaOpt.get();
            
            switch (celda.getTipo()) {
                case PARED:
                    // Colisión con pared - destruir barco
                    barco.setEstado(EstadoBarco.DESTRUIDO);
                    participacion.setEliminado(true);
                    logger.info("Barco {} destruido por colisión con pared en ({}, {})", 
                               barcoId, nuevaPosX, nuevaPosY);
                    return false;
                    
                case META:
                    // Llegó a la meta
                    barco.setEstado(EstadoBarco.LLEGO);
                    participacion.setPosX(nuevaPosX);
                    participacion.setPosY(nuevaPosY);
                    logger.info("Barco {} llegó a la meta en ({}, {})", 
                               barcoId, nuevaPosX, nuevaPosY);
                    
                    // Verificar si es el primer barco en llegar
                    verificarGanador(participacion.getPartida());
                    break;
                    
                case AGUA:
                case PARTIDA:
                default:
                    // Movimiento normal
                    participacion.setPosX(nuevaPosX);
                    participacion.setPosY(nuevaPosY);
                    break;
            }
        } else {
            // No hay celda definida, asumir agua
            participacion.setPosX(nuevaPosX);
            participacion.setPosY(nuevaPosY);
        }
        
        // Guardar cambios
        barcoRepo.save(barco);
        participacionRepo.save(participacion);
        
        return true;
    }
    
    /**
     * Verifica si hay un ganador en la partida
     */
    private void verificarGanador(Partida partida) {
        List<Participacion> participaciones = participacionRepo.findByPartidaId(partida.getId());
        
        // Buscar el primer barco que llegó a la meta
        Optional<Participacion> ganador = participaciones.stream()
            .filter(p -> !p.isEliminado() && p.getBarco().getEstado() == EstadoBarco.LLEGO)
            .findFirst();
        
        if (ganador.isPresent()) {
            // Hay un ganador - finalizar partida
            partida.setEstado(Partida.EstadoPartida.FINALIZADA);
            partidaRepo.save(partida);
            logger.info("Partida {} finalizada. Ganador: Jugador {} con barco {}", 
                       partida.getId(), 
                       ganador.get().getJugador().getNombre(),
                       ganador.get().getBarco().getId());
        }
    }
    
    /**
     * Obtiene el estado actual de una partida
     */
    public List<Participacion> obtenerEstadoPartida(Long partidaId) {
        return participacionRepo.findByPartidaId(partidaId);
    }
    
    /**
     * Verifica si es el turno de un jugador específico
     */
    public boolean esTurnoJugador(Long partidaId, Long jugadorId) {
        // Por simplicidad, permitir que cualquier jugador mueva en cualquier momento
        // En una implementación más compleja, se podría implementar un sistema de turnos estricto
        Partida partida = partidaRepo.findById(partidaId).orElse(null);
        return partida != null && partida.getEstado() == Partida.EstadoPartida.EN_CURSO;
    }
}