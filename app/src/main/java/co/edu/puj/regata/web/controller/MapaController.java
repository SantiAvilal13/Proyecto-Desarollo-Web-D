package co.edu.puj.regata.web.controller;

import co.edu.puj.regata.domain.entity.Mapa;
import co.edu.puj.regata.domain.entity.Celda;
import co.edu.puj.regata.domain.entity.Participacion;
import co.edu.puj.regata.domain.service.MapaService;
import co.edu.puj.regata.domain.service.ParticipacionService;
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
@RequestMapping("/mapas")
public class MapaController {

    @Autowired
    private MapaService mapaService;

    @Autowired
    private ParticipacionService participacionService;

    @GetMapping
    public String list(Model model) {
        List<Mapa> mapas = mapaService.listAll();
        model.addAttribute("mapas", mapas);
        return "mapas/list";  // Vista para listar todos los mapas
    }

    @GetMapping("/new")
    public String newMapa(Model model) {
        model.addAttribute("mapa", new Mapa());
        return "mapas/form";  // Vista para crear un nuevo mapa
    }

    @PostMapping
    public String save(@Valid @ModelAttribute Mapa mapa, BindingResult result, 
                      Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "mapas/form";  // Si hay errores de validación, vuelve al formulario
        }
        
        try {
            mapaService.save(mapa);
            redirectAttributes.addFlashAttribute("successMessage", "Mapa creado exitosamente");
        } catch (ValidationException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "mapas/form";
        }
        
        return "redirect:/mapas";  // Redirige a la lista de mapas después de guardar
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Mapa mapa = mapaService.findById(id);
            List<Celda> celdas = mapaService.getCeldasDeMapa(id);
            model.addAttribute("mapa", mapa);
            model.addAttribute("celdas", celdas);
            return "mapas/detail";  // Vista para mostrar detalles del mapa
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/mapas";
        }
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Mapa mapa = mapaService.findById(id);
            model.addAttribute("mapa", mapa);
            return "mapas/form";  // Vista para editar un mapa existente
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/mapas";
        }
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute Mapa mapa, 
                        BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "mapas/form";  // Si hay errores, vuelve al formulario de edición
        }
        
        try {
            mapa.setId(id);
            mapaService.save(mapa);
            redirectAttributes.addFlashAttribute("successMessage", "Mapa actualizado exitosamente");
        } catch (ValidationException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "mapas/form";
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/mapas";
        }
        
        return "redirect:/mapas";  // Redirige a la lista después de actualizar
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            mapaService.delete(id);
            redirectAttributes.addFlashAttribute("successMessage", "Mapa eliminado exitosamente");
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/mapas";
    }

    @GetMapping("/partida/{id}")
    public String verMapa(@PathVariable Long id, Model model) {
        Mapa mapa = mapaService.findById(id);
        List<Celda> celdas = mapaService.getCeldasDeMapa(id);
        List<Participacion> participaciones = participacionService.findByPartidaId(id);

        model.addAttribute("mapa", mapa);
        model.addAttribute("celdas", celdas);
        model.addAttribute("participaciones", participaciones);
        return "mapa/ver";  // Vista donde se renderiza el mapa
    }
}