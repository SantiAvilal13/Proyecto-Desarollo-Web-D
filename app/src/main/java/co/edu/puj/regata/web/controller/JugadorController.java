package co.edu.puj.regata.web.controller;

import co.edu.puj.regata.domain.entity.Jugador;
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

@Controller
@RequestMapping("/jugadores")
public class JugadorController {

    @Autowired
    private JugadorService jugadorService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("jugadores", jugadorService.listAll());
        return "jugadores/list";  // Vista para mostrar todos los jugadores
    }

    @GetMapping("/new")
    public String newJugador(Model model) {
        model.addAttribute("jugador", new Jugador());
        return "jugadores/form";  // Vista para crear un nuevo jugador
    }

    @PostMapping
    public String save(@Valid @ModelAttribute Jugador jugador, BindingResult result, 
                      RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "jugadores/form";  // Si hay errores de validación, vuelve al formulario
        }
        
        try {
            jugadorService.save(jugador);
            redirectAttributes.addFlashAttribute("successMessage", "Jugador creado exitosamente");
        } catch (ValidationException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "jugadores/form";
        }
        
        return "redirect:/jugadores";  // Redirige a la lista de jugadores después de guardar
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Jugador jugador = jugadorService.findById(id);
            model.addAttribute("jugador", jugador);
            return "jugadores/detail";  // Vista para mostrar detalles del jugador
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/jugadores";
        }
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Jugador jugador = jugadorService.findById(id);
            model.addAttribute("jugador", jugador);
            return "jugadores/form";  // Vista para editar un jugador existente
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/jugadores";
        }
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute Jugador jugador, 
                        BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "jugadores/form";  // Si hay errores, vuelve al formulario de edición
        }
        
        try {
            jugadorService.save(jugador);
            redirectAttributes.addFlashAttribute("successMessage", "Jugador actualizado exitosamente");
        } catch (ValidationException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "jugadores/form";
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/jugadores";
        }
        
        return "redirect:/jugadores";  // Redirige a la lista después de actualizar
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            jugadorService.delete(id);
            redirectAttributes.addFlashAttribute("successMessage", "Jugador eliminado exitosamente");
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (ValidationException e) {
            redirectAttributes.addFlashAttribute("errorMessage", 
                "No se puede eliminar el jugador porque tiene barcos asociados");
        }
        
        return "redirect:/jugadores";  // Redirige a la lista después de eliminar
    }
}