package co.edu.puj.regata.web.controller;

import co.edu.puj.regata.domain.entity.Jugador;
import co.edu.puj.regata.domain.repo.JugadorRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.HttpSession;
import java.util.Optional;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private JugadorRepo jugadorRepo;

    /**
     * Muestra la página de login
     */
    @GetMapping("/login")
    public String mostrarLogin(Model model, HttpSession session) {
        // Si ya está autenticado, redirigir según el rol
        Long jugadorId = (Long) session.getAttribute("jugadorId");
        if (jugadorId != null) {
            Jugador.RolUsuario rol = (Jugador.RolUsuario) session.getAttribute("rol");
            if (rol == Jugador.RolUsuario.ADMIN) {
                return "redirect:/regata";
            } else {
                return "redirect:/juego";
            }
        }
        
        return "auth/login";
    }

    /**
     * Procesa el login
     */
    @PostMapping("/login")
    public String procesarLogin(
            @RequestParam String nombre,
            @RequestParam String password,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        
        try {
            // Buscar jugador por nombre
            Optional<Jugador> jugadorOpt = jugadorRepo.findByNombre(nombre);
            
            if (!jugadorOpt.isPresent()) {
                redirectAttributes.addFlashAttribute("error", "Usuario no encontrado");
                return "redirect:/auth/login";
            }
            
            Jugador jugador = jugadorOpt.get();
            
            // Verificar contraseña (por simplicidad, comparación directa)
            if (!password.equals(jugador.getContraseña())) {
                redirectAttributes.addFlashAttribute("error", "Contraseña incorrecta");
                return "redirect:/auth/login";
            }
            
            // Establecer sesión
            session.setAttribute("jugadorId", jugador.getId());
            session.setAttribute("jugadorNombre", jugador.getNombre());
            session.setAttribute("rol", jugador.getRol());
            
            // Redirigir según el rol
            if (jugador.getRol() == Jugador.RolUsuario.ADMIN) {
                return "redirect:/regata";
            } else {
                return "redirect:/juego";
            }
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error interno del servidor");
            return "redirect:/auth/login";
        }
    }

    /**
     * Cierra la sesión
     */
    @GetMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        session.invalidate();
        redirectAttributes.addFlashAttribute("message", "Sesión cerrada exitosamente");
        return "redirect:/auth/login";
    }

    /**
     * Muestra la página de registro
     */
    @GetMapping("/register")
    public String mostrarRegistro(Model model) {
        model.addAttribute("jugador", new Jugador());
        return "auth/register";
    }

    /**
     * Procesa el registro
     */
    @PostMapping("/register")
    public String procesarRegistro(
            @ModelAttribute Jugador jugador,
            RedirectAttributes redirectAttributes) {
        
        try {
            // Verificar que el nombre no esté en uso
            Optional<Jugador> existente = jugadorRepo.findByNombre(jugador.getNombre());
            if (existente.isPresent()) {
                redirectAttributes.addFlashAttribute("error", "El nombre de usuario ya está en uso");
                return "redirect:/auth/register";
            }
            
            // Guardar nuevo jugador
            jugadorRepo.save(jugador);
            
            redirectAttributes.addFlashAttribute("message", 
                "Usuario registrado exitosamente. Puedes iniciar sesión ahora.");
            return "redirect:/auth/login";
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al registrar usuario");
            return "redirect:/auth/register";
        }
    }

    /**
     * Página de acceso denegado
     */
    @GetMapping("/denied")
    public String accesoDenegado() {
        return "auth/denied";
    }
}