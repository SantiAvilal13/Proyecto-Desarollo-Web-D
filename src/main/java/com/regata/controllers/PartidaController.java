package com.regata.controllers;

import com.regata.entities.*;
import com.regata.entities.EstadoPartida;
import com.regata.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/partidas")
public class PartidaController {
    
    @Autowired
    private PartidaService partidaService;
    
    @Autowired
    private JugadorService jugadorService;
    
    @Autowired
    private BarcoService barcoService;
    
    @Autowired
    private MapaService mapaService;
    
    @GetMapping
    public String listar(Model model) {
        List<Partida> partidas = partidaService.findAll();
        List<Partida> partidasDisponibles = partidaService.findPartidasDisponibles();
        List<Partida> partidasEnJuego = partidaService.findPartidasEnJuego();
        List<Partida> partidasTerminadas = partidaService.findPartidasTerminadas();
        
        model.addAttribute("partidas", partidas);
        model.addAttribute("partidasDisponibles", partidasDisponibles);
        model.addAttribute("partidasEnJuego", partidasEnJuego);
        model.addAttribute("partidasTerminadas", partidasTerminadas);
        model.addAttribute("totalPartidas", partidaService.count());
        model.addAttribute("partidasEsperando", partidaService.countByEstado(EstadoPartida.ESPERANDO));
        model.addAttribute("partidasActivas", partidaService.countByEstado(EstadoPartida.EN_JUEGO));
        
        return "partidas/listar";
    }
    
    @GetMapping("/crear")
    public String mostrarFormularioCrear(Model model) {
        model.addAttribute("partida", new Partida());
        model.addAttribute("mapas", mapaService.findAll());
        return "partidas/crear";
    }
    
    @PostMapping("/crear")
    public String crear(@Valid @ModelAttribute Partida partida, 
                       BindingResult result, 
                       Model model, 
                       RedirectAttributes redirectAttributes) {
        
        if (result.hasErrors()) {
            model.addAttribute("mapas", mapaService.findAll());
            return "partidas/crear";
        }
        
        if (partidaService.existsByNombre(partida.getNombre())) {
            result.rejectValue("nombre", "error.partida", "Ya existe una partida con este nombre");
            model.addAttribute("mapas", mapaService.findAll());
            return "partidas/crear";
        }
        
        try {
            partidaService.save(partida);
            redirectAttributes.addFlashAttribute("mensaje", "Partida creada exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
            return "redirect:/partidas";
        } catch (Exception e) {
            result.rejectValue("nombre", "error.partida", "Error al crear la partida: " + e.getMessage());
            model.addAttribute("mapas", mapaService.findAll());
            return "partidas/crear";
        }
    }
    
    @GetMapping("/{id}")
    public String detalle(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Partida> partidaOpt = partidaService.findById(id);
        
        if (partidaOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("mensaje", "Partida no encontrada");
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
            return "redirect:/partidas";
        }
        
        Partida partida = partidaOpt.get();
        List<ParticipantePartida> participantes = partidaService.getParticipantes(id);
        List<ParticipantePartida> ganadores = partidaService.getGanadores(id);
        
        model.addAttribute("partida", partida);
        model.addAttribute("participantes", participantes);
        model.addAttribute("ganadores", ganadores);
        model.addAttribute("jugadores", jugadorService.findAll());
        model.addAttribute("barcos", barcoService.findAll());
        
        return "partidas/detalle";
    }
    
    @PostMapping("/{id}/unirse")
    public String unirse(@PathVariable Long id, 
                        @RequestParam Long jugadorId, 
                        @RequestParam Long barcoId, 
                        RedirectAttributes redirectAttributes) {
        try {
            partidaService.unirseAPartida(id, jugadorId, barcoId);
            redirectAttributes.addFlashAttribute("mensaje", "Te has unido a la partida exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error al unirse a la partida: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        }
        
        return "redirect:/partidas/" + id;
    }
    
    @PostMapping("/{id}/salir")
    public String salir(@PathVariable Long id, 
                       @RequestParam Long jugadorId, 
                       RedirectAttributes redirectAttributes) {
        try {
            partidaService.salirDePartida(id, jugadorId);
            redirectAttributes.addFlashAttribute("mensaje", "Has salido de la partida");
            redirectAttributes.addFlashAttribute("tipoMensaje", "info");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error al salir de la partida: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        }
        
        return "redirect:/partidas/" + id;
    }
    
    @PostMapping("/{id}/iniciar")
    public String iniciar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            partidaService.iniciarPartida(id);
            redirectAttributes.addFlashAttribute("mensaje", "Partida iniciada exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
            return "redirect:/partidas/" + id + "/jugar";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error al iniciar la partida: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
            return "redirect:/partidas/" + id;
        }
    }
    
    @GetMapping("/{id}/jugar")
    public String jugar(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Partida> partidaOpt = partidaService.findById(id);
        
        if (partidaOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("mensaje", "Partida no encontrada");
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
            return "redirect:/partidas";
        }
        
        Partida partida = partidaOpt.get();
        
        if (!partida.estaEnJuego()) {
            redirectAttributes.addFlashAttribute("mensaje", "La partida no está en curso");
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
            return "redirect:/partidas/" + id;
        }
        
        List<ParticipantePartida> participantes = partidaService.getParticipantes(id);
        
        model.addAttribute("partida", partida);
        model.addAttribute("participantes", participantes);
        
        return "partidas/jugar";
    }
    
    @PostMapping("/{id}/mover")
    public String mover(@PathVariable Long id, 
                       @RequestParam Long jugadorId, 
                       @RequestParam Integer nuevaX, 
                       @RequestParam Integer nuevaY, 
                       RedirectAttributes redirectAttributes) {
        try {
            ParticipantePartida participante = partidaService.moverJugador(id, jugadorId, nuevaX, nuevaY);
            
            if (participante.getHaLlegadoMeta()) {
                redirectAttributes.addFlashAttribute("mensaje", "¡Felicidades! Has llegado a la meta");
                redirectAttributes.addFlashAttribute("tipoMensaje", "success");
            } else {
                // Pasar al siguiente turno
                partidaService.siguienteTurno(id);
                redirectAttributes.addFlashAttribute("mensaje", "Movimiento realizado exitosamente");
                redirectAttributes.addFlashAttribute("tipoMensaje", "success");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error al realizar el movimiento: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        }
        
        return "redirect:/partidas/" + id + "/jugar";
    }
    
    @PostMapping("/{id}/terminar")
    public String terminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            partidaService.terminarPartida(id);
            redirectAttributes.addFlashAttribute("mensaje", "Partida terminada");
            redirectAttributes.addFlashAttribute("tipoMensaje", "info");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error al terminar la partida: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        }
        
        return "redirect:/partidas/" + id;
    }
    
    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            partidaService.deleteById(id);
            redirectAttributes.addFlashAttribute("mensaje", "Partida eliminada exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error al eliminar la partida: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        }
        
        return "redirect:/partidas";
    }
}