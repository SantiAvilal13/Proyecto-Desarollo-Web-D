package com.regata.controllers;

import com.regata.entities.Celda;
import com.regata.entities.Mapa;
import com.regata.entities.Barco;
import com.regata.services.MapaService;
import com.regata.services.BarcoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/mapas")
public class MapaController {
    
    @Autowired
    private MapaService mapaService;
    
    @Autowired
    private BarcoService barcoService;
    
    @GetMapping
    public String mostrarMapa(Model model, RedirectAttributes redirectAttributes) {
        try {
            Mapa mapa = mapaService.obtenerMapaPrincipal();
            List<Celda> celdas = mapaService.obtenerCeldasDelMapa(mapa.getId());
            List<Barco> barcos = barcoService.listarTodos();
            
            // Enviar datos básicos
            model.addAttribute("gridSize", mapa.getTamColumnas());
            model.addAttribute("gridRows", mapa.getTamFilas());
            model.addAttribute("totalCeldas", celdas.size());
            model.addAttribute("totalBarcos", barcos.size());
            
            // Crear listas simples con solo los datos necesarios
            List<Map<String, Object>> celdasSimples = celdas.stream()
                .map(celda -> {
                    Map<String, Object> celdaMap = new HashMap<>();
                    celdaMap.put("id", celda.getId());
                    celdaMap.put("coordX", celda.getCoordX());
                    celdaMap.put("coordY", celda.getCoordY());
                    celdaMap.put("tipo", celda.getTipo().toString());
                    return celdaMap;
                })
                .collect(Collectors.toList());
                
            List<Map<String, Object>> barcosSimples = barcos.stream()
                .map(barco -> {
                    Map<String, Object> barcoMap = new HashMap<>();
                    barcoMap.put("id", barco.getId());
                    barcoMap.put("posX", barco.getPosX());
                    barcoMap.put("posY", barco.getPosY());
                    barcoMap.put("velX", barco.getVelX());
                    barcoMap.put("velY", barco.getVelY());
                    barcoMap.put("jugadorNombre", barco.getJugador().getNombre());
                    barcoMap.put("modeloNombre", barco.getModelo().getNombre());
                    barcoMap.put("modeloColor", barco.getModelo().getColor());
                    return barcoMap;
                })
                .collect(Collectors.toList());
            
            model.addAttribute("celdas", celdasSimples);
            model.addAttribute("barcos", barcosSimples);
            
            return "mapa";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al cargar el mapa: " + e.getMessage());
            return "redirect:/";
        }
    }
}