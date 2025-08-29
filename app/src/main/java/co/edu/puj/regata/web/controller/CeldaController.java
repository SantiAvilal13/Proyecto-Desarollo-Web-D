package co.edu.puj.regata.web.controller;

import co.edu.puj.regata.domain.entity.Celda;
import co.edu.puj.regata.domain.service.CeldaService;
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
@RequestMapping("/celdas")
public class CeldaController {

    @Autowired
    private CeldaService celdaService;
    
    @Autowired
    private MapaService mapaService;

    @GetMapping
    public String list(@RequestParam(required = false) Long mapaId,
                      @RequestParam(required = false) String tipo,
                      Model model) {
        List<Celda> celdas;
        
        if (mapaId != null && tipo != null && !tipo.isEmpty()) {
            // Filtrar por mapa y tipo (necesitaríamos agregar este método al servicio)
            celdas = celdaService.listAll();
        } else if (mapaId != null) {
            // Filtrar por mapa (necesitaríamos agregar este método al servicio)
            celdas = celdaService.listAll();
        } else if (tipo != null && !tipo.isEmpty()) {
            // Filtrar por tipo (necesitaríamos agregar este método al servicio)
            celdas = celdaService.listAll();
        } else {
            celdas = celdaService.listAll();
        }
        
        model.addAttribute("celdas", celdas);
        model.addAttribute("mapas", mapaService.listAll());
        model.addAttribute("tipos", Celda.TipoCelda.values());
        return "celdas/list";
    }

    @GetMapping("/new")
    public String newCelda(@RequestParam(required = false) Long mapaId, Model model) {
        Celda celda = new Celda();
        if (mapaId != null) {
            try {
                celda.setMapa(mapaService.findById(mapaId));
            } catch (EntityNotFoundException e) {
                // Ignorar si el mapa no existe
            }
        }
        model.addAttribute("celda", celda);
        model.addAttribute("mapas", mapaService.listAll());
        model.addAttribute("tipos", Celda.TipoCelda.values());
        return "celdas/form";
    }

    @PostMapping
    public String save(@Valid @ModelAttribute Celda celda, BindingResult result, 
                      Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("mapas", mapaService.listAll());
            model.addAttribute("tipos", Celda.TipoCelda.values());
            return "celdas/form";
        }
        
        try {
            celdaService.save(celda);
            redirectAttributes.addFlashAttribute("successMessage", "Celda creada exitosamente");
        } catch (ValidationException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            model.addAttribute("mapas", mapaService.listAll());
            model.addAttribute("tipos", Celda.TipoCelda.values());
            return "celdas/form";
        }
        
        return "redirect:/celdas";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Celda celda = celdaService.findById(id);
            model.addAttribute("celda", celda);
            return "celdas/detail";
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/celdas";
        }
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Celda celda = celdaService.findById(id);
            model.addAttribute("celda", celda);
            model.addAttribute("mapas", mapaService.listAll());
            model.addAttribute("tipos", Celda.TipoCelda.values());
            return "celdas/form";
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/celdas";
        }
    }

    @PutMapping("/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute Celda celda, 
                        BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("mapas", mapaService.listAll());
            model.addAttribute("tipos", Celda.TipoCelda.values());
            return "celdas/form";
        }
        
        try {
            celda.setId(id);
            celdaService.save(celda);
            redirectAttributes.addFlashAttribute("successMessage", "Celda actualizada exitosamente");
        } catch (ValidationException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            model.addAttribute("mapas", mapaService.listAll());
            model.addAttribute("tipos", Celda.TipoCelda.values());
            return "celdas/form";
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/celdas";
        }
        
        return "redirect:/celdas/" + id;
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            celdaService.delete(id);
            redirectAttributes.addFlashAttribute("successMessage", "Celda eliminada exitosamente");
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al eliminar la celda: " + e.getMessage());
        }
        
        return "redirect:/celdas";
    }

    @PostMapping("/{id}")
    public String deletePost(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        return delete(id, redirectAttributes);
    }
}