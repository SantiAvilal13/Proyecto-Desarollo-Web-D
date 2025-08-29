package co.edu.puj.regata.domain.service;

import co.edu.puj.regata.domain.entity.Partida;
import co.edu.puj.regata.domain.entity.Participacion;
import co.edu.puj.regata.domain.entity.Barco;
import co.edu.puj.regata.domain.entity.EstadoBarco;
import co.edu.puj.regata.domain.repo.PartidaRepo;
import co.edu.puj.regata.domain.repo.ParticipacionRepo;
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
public class SimulacionService {

    private static final Logger logger = LoggerFactory.getLogger(SimulacionService.class);
    
    // Dimensiones del mapa (pueden ser configurables)
    private static final int MAPA_ANCHO = 800;
    private static final int MAPA_ALTO = 600;
    private static final int META_X = 750; // Línea de meta cerca del borde derecho

    @Autowired
    private PartidaRepo partidaRepo;
    
    @Autowired
    private ParticipacionRepo participacionRepo;

    /**
     * Simula un paso de movimiento para todas las partidas activas
     */
    public void simularPasoMovimiento() {
        List<Partida> partidasActivas = partidaRepo.findPartidasActivas();
        
        for (Partida partida : partidasActivas) {
            simularMovimientoPartida(partida);
        }
    }

    /**
     * Simula el movimiento de todos los barcos en una partida específica
     */
    public void simularMovimientoPartida(Partida partida) {
        if (partida == null) {
            throw new ValidationException("La partida no puede ser null");
        }
        
        List<Participacion> participaciones = participacionRepo.findByPartidaId(partida.getId());
        
        for (Participacion participacion : participaciones) {
            if (!participacion.isEliminado() && participacion.getBarco().getEstado() == EstadoBarco.VIVO) {
                moverBarco(participacion);
            }
        }
        
        // Verificar si hay ganador
        verificarGanador(partida);
    }

    /**
     * Mueve un barco individual basándose en su velocidad
     */
    private void moverBarco(Participacion participacion) {
        Barco barco = participacion.getBarco();
        
        // Calcular nueva posición
        int nuevaPosX = participacion.getPosX() + barco.getVelocidadX();
        int nuevaPosY = participacion.getPosY() + barco.getVelocidadY();
        
        // Verificar colisiones con paredes
        if (nuevaPosX < 0 || nuevaPosX >= MAPA_ANCHO) {
            // Colisión con pared lateral - destruir barco
            barco.setEstado(EstadoBarco.DESTRUIDO);
            participacion.setEliminado(true);
            logger.info("Barco {} destruido por colisión con pared lateral en posición ({}, {})", 
                       barco.getId(), nuevaPosX, nuevaPosY);
            return;
        }
        
        if (nuevaPosY < 0 || nuevaPosY >= MAPA_ALTO) {
            // Colisión con pared superior/inferior - destruir barco
            barco.setEstado(EstadoBarco.DESTRUIDO);
            participacion.setEliminado(true);
            logger.info("Barco {} destruido por colisión con pared superior/inferior en posición ({}, {})", 
                       barco.getId(), nuevaPosX, nuevaPosY);
            return;
        }
        
        // Verificar si llegó a la meta
        if (nuevaPosX >= META_X) {
            barco.setEstado(EstadoBarco.LLEGO);
            nuevaPosX = META_X; // Fijar en la línea de meta
            logger.info("Barco {} llegó a la meta en posición ({}, {})", 
                       barco.getId(), nuevaPosX, nuevaPosY);
        }
        
        // Actualizar posición
        participacion.setPosX(nuevaPosX);
        participacion.setPosY(nuevaPosY);
        
        // Guardar cambios
        participacionRepo.save(participacion);
    }

    /**
     * Verifica si hay un ganador en la partida
     */
    private void verificarGanador(Partida partida) {
        List<Participacion> participaciones = participacionRepo.findByPartidaId(partida.getId());
        
        // Buscar barcos que hayan llegado a la meta
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
        } else {
            // Verificar si todos los barcos están destruidos
            boolean todosDestruidos = participaciones.stream()
                .allMatch(p -> p.isEliminado() || p.getBarco().getEstado() == EstadoBarco.DESTRUIDO);
            
            if (todosDestruidos) {
                // No hay ganador - finalizar partida
                partida.setEstado(Partida.EstadoPartida.FINALIZADA);
                partidaRepo.save(partida);
                logger.info("Partida {} finalizada sin ganador. Todos los barcos fueron destruidos", partida.getId());
            }
        }
    }

    /**
     * Obtiene las dimensiones del mapa
     */
    public int getMapaAncho() {
        return MAPA_ANCHO;
    }

    public int getMapaAlto() {
        return MAPA_ALTO;
    }

    public int getMetaX() {
        return META_X;
    }
}