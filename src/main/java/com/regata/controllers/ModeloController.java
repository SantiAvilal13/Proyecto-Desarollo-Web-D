package com.regata.controllers;

import com.regata.entities.Modelo;
import com.regata.services.ModeloService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/modelos")
public class ModeloController {
    
    @Autowired
    private ModeloService modeloService;
    
    // Listar todos los modelos
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("modelos", modeloService.findAll());
        return "modelos/listar";
    }
    
    // Mostrar formulario para crear nuevo modelo
    @GetMapping("/nuevo")
    public String mostrarFormularioCrear(Model model) {
        model.addAttribute("modelo", new Modelo());
        return "modelos/crear";
    }
    
    // Procesar creación de modelo
    @PostMapping
    public String crear(@Valid @ModelAttribute Modelo modelo, 
                       BindingResult result, 
                       RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "modelos/crear";
        }
        
        try {
            modeloService.save(modelo);
            redirectAttributes.addFlashAttribute("mensaje", "Modelo creado exitosamente");
            return "redirect:/modelos";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/modelos/nuevo";
        }
    }
    
    // Mostrar detalles de un modelo
    @GetMapping("/{id}")
    public String detalle(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Modelo> modelo = modeloService.findById(id);
        if (modelo.isPresent()) {
            model.addAttribute("modelo", modelo.get());
            return "modelos/detalle";
        } else {
            redirectAttributes.addFlashAttribute("error", "Modelo no encontrado");
            return "redirect:/modelos";
        }
    }
    
    // Mostrar formulario para editar modelo
    @GetMapping("/{id}/editar")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Modelo> modelo = modeloService.findById(id);
        if (modelo.isPresent()) {
            model.addAttribute("modelo", modelo.get());
            return "modelos/editar";
        } else {
            redirectAttributes.addFlashAttribute("error", "Modelo no encontrado");
            return "redirect:/modelos";
        }
    }
    
    // Procesar edición de modelo
    @PostMapping("/{id}")
    public String editar(@PathVariable Long id, 
                        @Valid @ModelAttribute Modelo modelo, 
                        BindingResult result, 
                        RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "modelos/editar";
        }
        
        try {
            modelo.setId(id);
            modeloService.save(modelo);
            redirectAttributes.addFlashAttribute("mensaje", "Modelo actualizado exitosamente");
            return "redirect:/modelos";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/modelos/" + id + "/editar";
        }
    }
    
    // Mostrar confirmación de eliminación
    @GetMapping("/{id}/eliminar")
    public String mostrarConfirmacionEliminar(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Modelo> modelo = modeloService.findById(id);
        if (modelo.isPresent()) {
            model.addAttribute("modelo", modelo.get());
            return "modelos/eliminar";
        } else {
            redirectAttributes.addFlashAttribute("error", "Modelo no encontrado");
            return "redirect:/modelos";
        }
    }
    
    // Procesar eliminación de modelo
    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            modeloService.deleteById(id);
            redirectAttributes.addFlashAttribute("mensaje", "Modelo eliminado exitosamente");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/modelos";
    }
}