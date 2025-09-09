package com.regata.config;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;

/**
 * Manejador global de excepciones para la aplicación.
 * Captura y maneja errores comunes de manera centralizada.
 */
// @ControllerAdvice - Temporalmente deshabilitado para debug
public class GlobalExceptionHandler {

    /**
     * Maneja errores de entidad no encontrada
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public String handleEntityNotFound(EntityNotFoundException ex, Model model) {
        model.addAttribute("error", "El recurso solicitado no fue encontrado.");
        model.addAttribute("message", ex.getMessage());
        return "error/404";
    }

    /**
     * Maneja errores de integridad de datos (claves duplicadas, etc.)
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public String handleDataIntegrityViolation(DataIntegrityViolationException ex, 
                                               RedirectAttributes redirectAttributes) {
        String errorMessage = "Error de integridad de datos";
        
        if (ex.getMessage().contains("email")) {
            errorMessage = "Ya existe un jugador con ese email.";
        } else if (ex.getMessage().contains("nombre")) {
            errorMessage = "Ya existe un registro con ese nombre.";
        } else if (ex.getMessage().contains("foreign key")) {
            errorMessage = "No se puede eliminar el registro porque está siendo utilizado por otros elementos.";
        }
        
        redirectAttributes.addFlashAttribute("error", errorMessage);
        return "redirect:/";
    }

    /**
     * Maneja errores de validación de constraints
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public String handleConstraintViolation(ConstraintViolationException ex, 
                                           RedirectAttributes redirectAttributes) {
        StringBuilder errorMessage = new StringBuilder("Errores de validación: ");
        
        ex.getConstraintViolations().forEach(violation -> {
            errorMessage.append(violation.getMessage()).append(". ");
        });
        
        redirectAttributes.addFlashAttribute("error", errorMessage.toString());
        return "redirect:/";
    }

    /**
     * Maneja errores de argumentos ilegales
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgument(IllegalArgumentException ex, 
                                       RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", 
            "Parámetros inválidos: " + ex.getMessage());
        return "redirect:/";
    }

    /**
     * Maneja cualquier otro error no específico
     */
    @ExceptionHandler(Exception.class)
    public String handleGenericException(Exception ex, Model model) {
        model.addAttribute("error", "Ha ocurrido un error inesperado.");
        model.addAttribute("message", ex.getMessage());
        model.addAttribute("trace", ex.getStackTrace());
        return "error/500";
    }
}