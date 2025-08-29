package co.edu.puj.regata.web.controller;

import co.edu.puj.regata.domain.service.SimulacionService;
import co.edu.puj.regata.domain.entity.Partida;
import co.edu.puj.regata.domain.service.PartidaService;
import co.edu.puj.regata.domain.exception.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping("/simulacion")
public class SimulacionController {

    private static final Logger logger = LoggerFactory.getLogger(SimulacionController.class);
    
    @Autowired
    private SimulacionService simulacionService;
    
    @Autowired
    private PartidaService partidaService;
    
    private ScheduledExecutorService scheduler;
    private boolean simulacionActiva = false;

    /**
     * Página principal de control de simulación
     */
    @GetMapping
    public String index(Model model) {
        model.addAttribute("simulacionActiva", simulacionActiva);
        model.addAttribute("partidas", partidaService.listAll());
        return "simulacion/index";
    }

    /**
     * Iniciar simulación automática
     */
    @PostMapping("/iniciar")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> iniciarSimulacion() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            if (simulacionActiva) {
                response.put("success", false);
                response.put("message", "La simulación ya está activa");
                return ResponseEntity.badRequest().body(response);
            }
            
            scheduler = Executors.newScheduledThreadPool(1);
            scheduler.scheduleAtFixedRate(() -> {
                try {
                    simulacionService.simularPasoMovimiento();
                } catch (Exception e) {
                    logger.error("Error en simulación automática", e);
                }
            }, 0, 1, TimeUnit.SECONDS); // Ejecutar cada segundo
            
            simulacionActiva = true;
            
            response.put("success", true);
            response.put("message", "Simulación iniciada correctamente");
            logger.info("Simulación automática iniciada");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error al iniciar simulación", e);
            response.put("success", false);
            response.put("message", "Error al iniciar simulación: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    /**
     * Detener simulación automática
     */
    @PostMapping("/detener")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> detenerSimulacion() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            if (!simulacionActiva) {
                response.put("success", false);
                response.put("message", "La simulación no está activa");
                return ResponseEntity.badRequest().body(response);
            }
            
            if (scheduler != null) {
                scheduler.shutdown();
                try {
                    if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                        scheduler.shutdownNow();
                    }
                } catch (InterruptedException e) {
                    scheduler.shutdownNow();
                    Thread.currentThread().interrupt();
                }
            }
            
            simulacionActiva = false;
            
            response.put("success", true);
            response.put("message", "Simulación detenida correctamente");
            logger.info("Simulación automática detenida");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error al detener simulación", e);
            response.put("success", false);
            response.put("message", "Error al detener simulación: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    /**
     * Ejecutar un paso manual de simulación
     */
    @PostMapping("/paso")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> ejecutarPaso() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            simulacionService.simularPasoMovimiento();
            
            response.put("success", true);
            response.put("message", "Paso de simulación ejecutado correctamente");
            logger.info("Paso manual de simulación ejecutado");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error al ejecutar paso de simulación", e);
            response.put("success", false);
            response.put("message", "Error al ejecutar paso: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    /**
     * Simular movimiento de una partida específica
     */
    @PostMapping("/partida/{id}")
    public String simularPartida(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Partida partida = partidaService.findById(id);
            simulacionService.simularMovimientoPartida(partida);
            
            redirectAttributes.addFlashAttribute("successMessage", 
                "Simulación ejecutada para la partida: " + partida.getNombre());
            logger.info("Simulación manual ejecutada para partida {}", id);
            
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (Exception e) {
            logger.error("Error al simular partida {}", id, e);
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Error al simular partida: " + e.getMessage());
        }
        
        return "redirect:/simulacion";
    }

    /**
     * Obtener estado actual de la simulación
     */
    @GetMapping("/estado")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> obtenerEstado() {
        Map<String, Object> response = new HashMap<>();
        
        response.put("simulacionActiva", simulacionActiva);
        response.put("mapaAncho", simulacionService.getMapaAncho());
        response.put("mapaAlto", simulacionService.getMapaAlto());
        response.put("metaX", simulacionService.getMetaX());
        
        return ResponseEntity.ok(response);
    }
}