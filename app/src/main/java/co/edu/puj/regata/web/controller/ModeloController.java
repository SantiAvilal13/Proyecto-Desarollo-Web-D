package co.edu.puj.regata.web.controller;

import co.edu.puj.regata.domain.entity.Modelo;
import co.edu.puj.regata.domain.service.ModeloService;
import co.edu.puj.regata.domain.exception.EntityNotFoundException;
import co.edu.puj.regata.domain.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/modelos")
public class ModeloController {

    @Autowired
    private ModeloService modeloService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("modelos", modeloService.listAll());
        return "modelos/list";  // Vista para mostrar todos los modelos
    }

    @GetMapping("/new")
    public String newModelo(Model model) {
        model.addAttribute("modelo", new Modelo());
        return "modelos/form";  // Vista para crear un nuevo modelo
    }

    @PostMapping
    public String save(@Valid @ModelAttribute Modelo modelo, BindingResult result, 
                      RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "modelos/form";  // Si hay errores de validación, vuelve al formulario
        }
        
        try {
            modeloService.save(modelo);
            redirectAttributes.addFlashAttribute("successMessage", "Modelo creado exitosamente");
        } catch (ValidationException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "modelos/form";
        }
        
        return "redirect:/modelos";  // Redirige a la lista de modelos después de guardar
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Modelo modelo = modeloService.findById(id);
            model.addAttribute("modelo", modelo);
            return "modelos/detail";  // Vista para mostrar detalles del modelo
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/modelos";
        }
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Modelo modelo = modeloService.findById(id);
            model.addAttribute("modelo", modelo);
            return "modelos/form";  // Vista para editar un modelo existente
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/modelos";
        }
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute Modelo modelo, 
                        BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "modelos/form";  // Si hay errores, vuelve al formulario de edición
        }
        
        try {
            modeloService.save(modelo);
            redirectAttributes.addFlashAttribute("successMessage", "Modelo actualizado exitosamente");
        } catch (ValidationException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "modelos/form";
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/modelos";
        }
        
        return "redirect:/modelos";  // Redirige a la lista después de actualizar
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            modeloService.delete(id);
            redirectAttributes.addFlashAttribute("successMessage", "Modelo eliminado exitosamente");
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (ValidationException e) {
            redirectAttributes.addFlashAttribute("errorMessage", 
                "No se puede eliminar el modelo porque tiene barcos asociados");
        }
        
        return "redirect:/modelos";  // Redirige a la lista después de eliminar
    }
}