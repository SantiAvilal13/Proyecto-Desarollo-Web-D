package co.edu.puj.regata.web.controller;

import co.edu.puj.regata.domain.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controlador para la página principal y navegación general del sistema.
 * Maneja las rutas principales y proporciona estadísticas generales.
 */
@Controller
public class HomeController {

    @Autowired
    private ModeloService modeloService;

    @Autowired
    private JugadorService jugadorService;

    @Autowired
    private BarcoService barcoService;

    @Autowired
    private PartidaService partidaService;

    @Autowired
    private MapaService mapaService;

    /**
     * Página principal del sistema.
     * Muestra un dashboard con estadísticas generales.
     */
    @GetMapping("/")
    public String home(Model model) {
        try {
            // Obtener estadísticas generales
            long totalModelos = modeloService.listAll().size();
            long totalJugadores = jugadorService.listAll().size();
            long totalBarcos = barcoService.listAll().size();
            long totalPartidas = partidaService.listAll().size();
            long totalMapas = mapaService.listAll().size();

            // Agregar estadísticas al modelo
            model.addAttribute("totalModelos", totalModelos);
            model.addAttribute("totalJugadores", totalJugadores);
            model.addAttribute("totalBarcos", totalBarcos);
            model.addAttribute("totalPartidas", totalPartidas);
            model.addAttribute("totalMapas", totalMapas);

            // Obtener datos recientes para el dashboard
            model.addAttribute("jugadoresRecientes", jugadorService.listAll().stream().limit(5).toList());
            model.addAttribute("barcosRecientes", barcoService.listAll().stream().limit(5).toList());
            model.addAttribute("partidasRecientes", partidaService.listAll().stream().limit(5).toList());

        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error al cargar las estadísticas del sistema: " + e.getMessage());
        }

        return "home";
    }

    /**
     * Página de información sobre el sistema.
     */
    @GetMapping("/about")
    public String about() {
        return "about";
    }

    /**
     * Página de ayuda del sistema.
     */
    @GetMapping("/help")
    public String help() {
        return "help";
    }
}