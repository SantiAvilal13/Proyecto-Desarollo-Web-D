package com.regata.controllers;

import com.regata.services.JugadorService;
import com.regata.services.ModeloService;
import com.regata.services.BarcoService;
import com.regata.services.MapaService;
import com.regata.services.PartidaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    
    @Autowired
    private JugadorService jugadorService;
    
    @Autowired
    private ModeloService modeloService;
    
    @Autowired
    private BarcoService barcoService;
    
    @Autowired
    private MapaService mapaService;
    
    @Autowired
    private PartidaService partidaService;
    
    @GetMapping("/")
    public String home(Model model) {
        // Agregar estadísticas básicas para mostrar en la página principal
        model.addAttribute("totalJugadores", jugadorService.count());
        model.addAttribute("totalModelos", modeloService.count());
        model.addAttribute("totalBarcos", barcoService.count());
        model.addAttribute("totalMapas", mapaService.count());
        
        // Agregar estadísticas de partidas
        model.addAttribute("partidasEsperando", partidaService.countByEstado(com.regata.entities.EstadoPartida.ESPERANDO));
        model.addAttribute("partidasEnJuego", partidaService.countByEstado(com.regata.entities.EstadoPartida.EN_JUEGO));
        model.addAttribute("partidasTerminadas", partidaService.countByEstado(com.regata.entities.EstadoPartida.TERMINADA));
        
        return "index";
    }
}