package com.regata.controllers;

import com.regata.entities.Jugador;
import com.regata.services.JugadorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/jugadores")
public class JugadorController {
    
    @Autowired
    private JugadorService jugadorService;
    
    // Listar todos los jugadores
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("jugadores", jugadorService.findAll());
        return "jugadores/listar";
    }
    
    // Mostrar formulario para crear nuevo jugador
    @GetMapping("/nuevo")
    public String mostrarFormularioCrear(Model model) {
        model.addAttribute("jugador", new Jugador());
        return "jugadores/crear";
    }
    
    // Procesar creación de jugador
    @PostMapping
    public String crear(@Valid @ModelAttribute Jugador jugador, 
                       BindingResult result, 
                       RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "jugadores/crear";
        }
        
        try {
            jugadorService.save(jugador);
            redirectAttributes.addFlashAttribute("mensaje", "Jugador creado exitosamente");
            return "redirect:/jugadores";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/jugadores/nuevo";
        }
    }
    
    // Mostrar detalles de un jugador
    @GetMapping("/{id}")
    public String detalle(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Jugador> jugador = jugadorService.findById(id);
        if (jugador.isPresent()) {
            model.addAttribute("jugador", jugador.get());
            return "jugadores/detalle";
        } else {
            redirectAttributes.addFlashAttribute("error", "Jugador no encontrado");
            return "redirect:/jugadores";
        }
    }
    
    // Mostrar formulario para editar jugador
    @GetMapping("/{id}/editar")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Jugador> jugador = jugadorService.findById(id);
        if (jugador.isPresent()) {
            model.addAttribute("jugador", jugador.get());
            return "jugadores/editar";
        } else {
            redirectAttributes.addFlashAttribute("error", "Jugador no encontrado");
            return "redirect:/jugadores";
        }
    }
    
    // Procesar edición de jugador
    @PostMapping("/{id}")
    public String editar(@PathVariable Long id, 
                        @Valid @ModelAttribute Jugador jugador, 
                        BindingResult result, 
                        RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "jugadores/editar";
        }
        
        try {
            jugador.setId(id);
            jugadorService.save(jugador);
            redirectAttributes.addFlashAttribute("mensaje", "Jugador actualizado exitosamente");
            return "redirect:/jugadores";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/jugadores/" + id + "/editar";
        }
    }
    
    // Mostrar confirmación de eliminación
    @GetMapping("/{id}/eliminar")
    public String mostrarConfirmacionEliminar(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Jugador> jugador = jugadorService.findById(id);
        if (jugador.isPresent()) {
            model.addAttribute("jugador", jugador.get());
            return "jugadores/eliminar";
        } else {
            redirectAttributes.addFlashAttribute("error", "Jugador no encontrado");
            return "redirect:/jugadores";
        }
    }
    
    // Procesar eliminación de jugador
    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            jugadorService.deleteById(id);
            redirectAttributes.addFlashAttribute("mensaje", "Jugador eliminado exitosamente");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/jugadores";
    }
}