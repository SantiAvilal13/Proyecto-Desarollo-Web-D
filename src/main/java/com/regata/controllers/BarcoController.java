package com.regata.controllers;

import com.regata.entities.Barco;
import com.regata.services.BarcoService;
import com.regata.services.JugadorService;
import com.regata.services.ModeloService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/barcos")
public class BarcoController {
    
    @Autowired
    private BarcoService barcoService;
    
    @Autowired
    private JugadorService jugadorService;
    
    @Autowired
    private ModeloService modeloService;
    
    @Autowired
    private Validator validator;
    
    // Listar todos los barcos
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("barcos", barcoService.findAll());
        return "barcos/listar";
    }
    
    // Mostrar formulario para crear nuevo barco
    @GetMapping("/crear")
    public String mostrarFormularioCrear(Model model) {
        Barco barco = new Barco();
        // Inicializar velocidades por defecto
        barco.setVelX(0);
        barco.setVelY(0);
        model.addAttribute("barco", barco);
        model.addAttribute("jugadores", jugadorService.findAll());
        model.addAttribute("modelos", modeloService.findAll());
        return "barcos/crear";
    }
    
    // Mostrar detalles de un barco
    @GetMapping("/{id}")
    public String detalle(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Barco> barco = barcoService.findById(id);
        if (barco.isPresent()) {
            model.addAttribute("barco", barco.get());
            return "barcos/detalle";
        } else {
            redirectAttributes.addFlashAttribute("error", "Barco no encontrado");
            return "redirect:/barcos";
        }
    }
    
    // Procesar creación de barco
    @PostMapping
    public String crear(@RequestParam("jugadorId") Long jugadorId,
                       @RequestParam("modeloId") Long modeloId,
                       @RequestParam("posX") Integer posX,
                       @RequestParam("posY") Integer posY,
                       @RequestParam("velX") Integer velX,
                       @RequestParam("velY") Integer velY,
                       Model model,
                       RedirectAttributes redirectAttributes) {
        
        // Crear el objeto Barco manualmente
        Barco barco = new Barco();
        barco.setPosX(posX);
        barco.setPosY(posY);
        barco.setVelX(velX);
        barco.setVelY(velY);
        
        // Establecer las relaciones
        try {
            com.regata.entities.Jugador jugador = jugadorService.findById(jugadorId)
                .orElseThrow(() -> new RuntimeException("Jugador no encontrado"));
            com.regata.entities.Modelo modelo = modeloService.findById(modeloId)
                .orElseThrow(() -> new RuntimeException("Modelo no encontrado"));
            
            barco.setJugador(jugador);
            barco.setModelo(modelo);
        } catch (RuntimeException e) {
            model.addAttribute("jugadores", jugadorService.findAll());
            model.addAttribute("modelos", modeloService.findAll());
            model.addAttribute("error", e.getMessage());
            return "barcos/crear";
        }
        
        // Crear BindingResult para validación manual
        org.springframework.validation.BeanPropertyBindingResult result = 
            new org.springframework.validation.BeanPropertyBindingResult(barco, "barco");
        
        // Validar después de establecer las relaciones
        validator.validate(barco, result);
        
        if (result.hasErrors()) {
            // Preservar las selecciones del usuario cuando hay errores de validación
            try {
                if (jugadorId != null) {
                    com.regata.entities.Jugador jugador = jugadorService.findById(jugadorId).orElse(null);
                    barco.setJugador(jugador);
                }
                if (modeloId != null) {
                    com.regata.entities.Modelo modelo = modeloService.findById(modeloId).orElse(null);
                    barco.setModelo(modelo);
                }
            } catch (Exception e) {
                // Si hay error al cargar jugador/modelo, continuar sin ellos
            }
            
            model.addAttribute("jugadores", jugadorService.findAll());
            model.addAttribute("modelos", modeloService.findAll());
            return "barcos/crear";
        }
        
        try {
            barcoService.save(barco);
            redirectAttributes.addFlashAttribute("mensaje", "Barco creado exitosamente");
            return "redirect:/barcos";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/barcos/crear";
        }
    }
    

    
    // Mostrar formulario para editar barco
    @GetMapping("/{id}/editar")
    @Transactional(readOnly = true)
    public String mostrarFormularioEditar(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Barco> barco = barcoService.findById(id);
        if (barco.isPresent()) {
            model.addAttribute("barco", barco.get());
            model.addAttribute("jugadores", jugadorService.findAll());
            model.addAttribute("modelos", modeloService.findAll());
            return "barcos/editar";
        } else {
            redirectAttributes.addFlashAttribute("error", "Barco no encontrado");
            return "redirect:/barcos";
        }
    }
    
    // Procesar edición de barco
    @PostMapping("/{id}")
    public String editar(@PathVariable Long id, 
                        @Valid @ModelAttribute Barco barco, 
                        BindingResult result, 
                        Model model,
                        RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("jugadores", jugadorService.findAll());
            model.addAttribute("modelos", modeloService.findAll());
            return "barcos/editar";
        }
        
        try {
            barco.setId(id);
            barcoService.save(barco);
            redirectAttributes.addFlashAttribute("mensaje", "Barco actualizado exitosamente");
            return "redirect:/barcos";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/barcos/" + id + "/editar";
        }
    }
    
    // Mostrar confirmación de eliminación
    @GetMapping("/{id}/eliminar")
    public String mostrarConfirmacionEliminar(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Barco> barco = barcoService.findById(id);
        if (barco.isPresent()) {
            model.addAttribute("barco", barco.get());
            return "barcos/eliminar";
        } else {
            redirectAttributes.addFlashAttribute("error", "Barco no encontrado");
            return "redirect:/barcos";
        }
    }
    
    // Procesar eliminación de barco
    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            barcoService.deleteById(id);
            redirectAttributes.addFlashAttribute("mensaje", "Barco eliminado exitosamente");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/barcos";
    }
}