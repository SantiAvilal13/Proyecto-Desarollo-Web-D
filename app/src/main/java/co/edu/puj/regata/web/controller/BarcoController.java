package co.edu.puj.regata.web.controller;

import co.edu.puj.regata.domain.entity.Barco;
import co.edu.puj.regata.domain.service.BarcoService;
import co.edu.puj.regata.domain.service.ModeloService;
import co.edu.puj.regata.domain.service.JugadorService;
import co.edu.puj.regata.domain.exception.EntityNotFoundException;
import co.edu.puj.regata.domain.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/barcos")
public class BarcoController {

    @Autowired
    private BarcoService barcoService;
    
    @Autowired
    private ModeloService modeloService;
    
    @Autowired
    private JugadorService jugadorService;

    @GetMapping
    public String list(@RequestParam(required = false) Long jugadorId,
                      @RequestParam(required = false) Long modeloId,
                      Model model) {
        List<Barco> barcos;
        
        if (jugadorId != null && modeloId != null) {
            barcos = barcoService.findByJugadorIdAndModeloId(jugadorId, modeloId);
        } else if (jugadorId != null) {
            barcos = barcoService.findByJugadorId(jugadorId);
        } else if (modeloId != null) {
            barcos = barcoService.findByModeloId(modeloId);
        } else {
            barcos = barcoService.listAll();
        }
        
        model.addAttribute("barcos", barcos);
        model.addAttribute("jugadores", jugadorService.listAll());
        model.addAttribute("modelos", modeloService.listAll());
        return "barcos/list";  // Vista para mostrar todos los barcos
    }

    @GetMapping("/new")
    public String newBarco(Model model) {
        model.addAttribute("barco", new Barco());
        model.addAttribute("modelos", modeloService.listAll());
        model.addAttribute("jugadores", jugadorService.listAll());
        return "barcos/form";  // Vista para crear un nuevo barco
    }

    @PostMapping
    public String save(@Valid @ModelAttribute Barco barco, BindingResult result, 
                      Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("modelos", modeloService.listAll());
            model.addAttribute("jugadores", jugadorService.listAll());
            return "barcos/form";  // Si hay errores de validación, vuelve al formulario
        }
        
        try {
            barcoService.save(barco);
            redirectAttributes.addFlashAttribute("successMessage", "Barco creado exitosamente");
        } catch (ValidationException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            model.addAttribute("modelos", modeloService.listAll());
            model.addAttribute("jugadores", jugadorService.listAll());
            return "barcos/form";
        }
        
        return "redirect:/barcos";  // Redirige a la lista de barcos después de guardar
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Barco barco = barcoService.findById(id);
            model.addAttribute("barco", barco);
            return "barcos/detail";  // Vista para mostrar detalles del barco
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/barcos";
        }
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Barco barco = barcoService.findById(id);
            model.addAttribute("barco", barco);
            model.addAttribute("modelos", modeloService.listAll());
            model.addAttribute("jugadores", jugadorService.listAll());
            return "barcos/form";  // Vista para editar un barco existente
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/barcos";
        }
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute Barco barco, 
                        BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("modelos", modeloService.listAll());
            model.addAttribute("jugadores", jugadorService.listAll());
            return "barcos/form";  // Si hay errores, vuelve al formulario de edición
        }
        
        try {
            barcoService.save(barco);
            redirectAttributes.addFlashAttribute("successMessage", "Barco actualizado exitosamente");
        } catch (ValidationException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            model.addAttribute("modelos", modeloService.listAll());
            model.addAttribute("jugadores", jugadorService.listAll());
            return "barcos/form";
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/barcos";
        }
        
        return "redirect:/barcos";  // Redirige a la lista después de actualizar
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            barcoService.delete(id);
            redirectAttributes.addFlashAttribute("successMessage", "Barco eliminado exitosamente");
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (ValidationException e) {
            redirectAttributes.addFlashAttribute("errorMessage", 
                "No se puede eliminar el barco porque está participando en partidas");
        }
        
        return "redirect:/barcos";  // Redirige a la lista después de eliminar
    }
}