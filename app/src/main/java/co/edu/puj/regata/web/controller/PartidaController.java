package co.edu.puj.regata.web.controller;

import co.edu.puj.regata.domain.entity.Partida;
import co.edu.puj.regata.domain.service.PartidaService;
import co.edu.puj.regata.domain.service.MapaService;
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
@RequestMapping("/partidas")
public class PartidaController {

    @Autowired
    private PartidaService partidaService;
    
    @Autowired
    private MapaService mapaService;

    @GetMapping
    public String list(@RequestParam(required = false) String estado,
                      @RequestParam(required = false) Long mapaId,
                      Model model) {
        List<Partida> partidas;
        
        if (estado != null && !estado.isEmpty() && mapaId != null) {
            partidas = partidaService.findByEstadoAndMapaId(estado, mapaId);
        } else if (estado != null && !estado.isEmpty()) {
            partidas = partidaService.findByEstado(estado);
        } else if (mapaId != null) {
            partidas = partidaService.findByMapaId(mapaId);
        } else {
            partidas = partidaService.listAll();
        }
        
        model.addAttribute("partidas", partidas);
        model.addAttribute("mapas", mapaService.listAll());
        return "partidas/list";  // Vista para mostrar todas las partidas
    }

    @GetMapping("/new")
    public String newPartida(Model model) {
        model.addAttribute("partida", new Partida());
        model.addAttribute("mapas", mapaService.listAll());
        return "partidas/form";  // Vista para crear una nueva partida
    }

    @PostMapping
    public String save(@Valid @ModelAttribute Partida partida, BindingResult result, 
                      Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("mapas", mapaService.listAll());
            return "partidas/form";  // Si hay errores de validación, vuelve al formulario
        }
        
        try {
            partidaService.save(partida);
            redirectAttributes.addFlashAttribute("successMessage", "Partida creada exitosamente");
        } catch (ValidationException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            model.addAttribute("mapas", mapaService.listAll());
            return "partidas/form";
        }
        
        return "redirect:/partidas";  // Redirige a la lista de partidas después de guardar
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Partida partida = partidaService.findById(id);
            model.addAttribute("partida", partida);
            return "partidas/detail";  // Vista para mostrar detalles de la partida
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/partidas";
        }
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Partida partida = partidaService.findById(id);
            model.addAttribute("partida", partida);
            model.addAttribute("mapas", mapaService.listAll());
            return "partidas/form";  // Vista para editar una partida existente
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/partidas";
        }
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute Partida partida, 
                        BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("mapas", mapaService.listAll());
            return "partidas/form";  // Si hay errores, vuelve al formulario de edición
        }
        
        try {
            partidaService.save(partida);
            redirectAttributes.addFlashAttribute("successMessage", "Partida actualizada exitosamente");
        } catch (ValidationException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            model.addAttribute("mapas", mapaService.listAll());
            return "partidas/form";
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/partidas";
        }
        
        return "redirect:/partidas";  // Redirige a la lista después de actualizar
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            partidaService.delete(id);
            redirectAttributes.addFlashAttribute("successMessage", "Partida eliminada exitosamente");
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (ValidationException e) {
            redirectAttributes.addFlashAttribute("errorMessage", 
                "No se puede eliminar la partida porque tiene participaciones asociadas");
        }
        
        return "redirect:/partidas";  // Redirige a la lista después de eliminar
    }
}