package co.edu.puj.regata.web.controller;

import co.edu.puj.regata.domain.entity.*;
import co.edu.puj.regata.domain.service.*;
import co.edu.puj.regata.domain.repo.*;
import co.edu.puj.regata.domain.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/juego")
public class JuegoController {

    @Autowired
    private JuegoService juegoService;
    
    @Autowired
    private PartidaRepo partidaRepo;
    
    @Autowired
    private ParticipacionRepo participacionRepo;
    
    @Autowired
    private JugadorRepo jugadorRepo;
    
    @Autowired
    private BarcoRepo barcoRepo;
    
    @Autowired
    private CeldaRepo celdaRepo;

    /**
     * Muestra la lista de partidas disponibles para jugar
     */
    @GetMapping
    public String listarPartidas(Model model, HttpSession session) {
        // Verificar autenticación (por ahora simulada)
        Long jugadorId = (Long) session.getAttribute("jugadorId");
        if (jugadorId == null) {
            return "redirect:/auth/login";
        }
        
        List<Partida> partidas = partidaRepo.findAll();
        model.addAttribute("partidas", partidas);
        return "juego/partidas";
    }
    
    /**
     * Muestra la interfaz de juego para una partida específica
     */
    @GetMapping("/partida/{id}")
    public String mostrarJuego(@PathVariable Long id, Model model, HttpSession session) {
        // Verificar autenticación
        Long jugadorId = (Long) session.getAttribute("jugadorId");
        if (jugadorId == null) {
            return "redirect:/auth/login";
        }
        
        Optional<Partida> partidaOpt = partidaRepo.findById(id);
        if (!partidaOpt.isPresent()) {
            return "redirect:/juego";
        }
        
        Partida partida = partidaOpt.get();
        List<Participacion> participaciones = participacionRepo.findByPartidaId(id);
        
        // Obtener el barco del jugador actual
        Optional<Participacion> miParticipacion = participaciones.stream()
            .filter(p -> p.getJugador().getId().equals(jugadorId))
            .findFirst();
        
        // Obtener las celdas del mapa
        List<Celda> celdas = celdaRepo.findByMapaId(partida.getMapa().getId());
        
        model.addAttribute("partida", partida);
        model.addAttribute("participaciones", participaciones);
        model.addAttribute("miParticipacion", miParticipacion.orElse(null));
        model.addAttribute("celdas", celdas);
        model.addAttribute("mapa", partida.getMapa());
        
        return "juego/tablero";
    }
    
    /**
     * Procesa un movimiento de barco
     */
    @PostMapping("/mover")
    @ResponseBody
    public Map<String, Object> moverBarco(
            @RequestParam Long barcoId,
            @RequestParam int cambioVx,
            @RequestParam int cambioVy,
            HttpSession session) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Verificar autenticación
            Long jugadorId = (Long) session.getAttribute("jugadorId");
            if (jugadorId == null) {
                response.put("success", false);
                response.put("message", "No estás autenticado");
                return response;
            }
            
            // Realizar el movimiento
            boolean exito = juegoService.realizarMovimiento(barcoId, cambioVx, cambioVy, jugadorId);
            
            if (exito) {
                response.put("success", true);
                response.put("message", "Movimiento realizado exitosamente");
                
                // Obtener estado actualizado del barco
                Optional<Barco> barcoOpt = barcoRepo.findById(barcoId);
                if (barcoOpt.isPresent()) {
                    Barco barco = barcoOpt.get();
                    response.put("velocidadX", barco.getVelocidadX());
                    response.put("velocidadY", barco.getVelocidadY());
                    response.put("estado", barco.getEstado().toString());
                }
                
            } else {
                response.put("success", false);
                response.put("message", "El barco fue destruido");
            }
            
        } catch (ValidationException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error interno del servidor");
        }
        
        return response;
    }
    
    /**
     * Obtiene el estado actual de la partida (AJAX)
     */
    @GetMapping("/estado/{partidaId}")
    @ResponseBody
    public Map<String, Object> obtenerEstadoPartida(@PathVariable Long partidaId) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<Participacion> participaciones = juegoService.obtenerEstadoPartida(partidaId);
            response.put("success", true);
            response.put("participaciones", participaciones);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener estado de la partida");
        }
        
        return response;
    }
    
    /**
     * Página de ayuda para el juego
     */
    @GetMapping("/ayuda")
    public String mostrarAyuda() {
        return "juego/ayuda";
    }
}