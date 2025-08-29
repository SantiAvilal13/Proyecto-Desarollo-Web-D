package co.edu.puj.regata.web.exception;

import co.edu.puj.regata.domain.exception.EntityNotFoundException;
import co.edu.puj.regata.domain.exception.ValidationException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(EntityNotFoundException.class)
    public String handleEntityNotFoundException(EntityNotFoundException ex, RedirectAttributes redirectAttributes) {
        logger.error("Entity not found: {}", ex.getMessage());
        redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        return "redirect:/";
    }

    @ExceptionHandler(ValidationException.class)
    public String handleValidationException(ValidationException ex, RedirectAttributes redirectAttributes) {
        logger.error("Validation error: {}", ex.getMessage());
        redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        return "redirect:/";
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgumentException(IllegalArgumentException ex, RedirectAttributes redirectAttributes) {
        logger.error("Illegal argument: {}", ex.getMessage());
        redirectAttributes.addFlashAttribute("errorMessage", "Argumento inválido: " + ex.getMessage());
        return "redirect:/";
    }

    @ExceptionHandler(Exception.class)
    public String handleGenericException(Exception ex, Model model) {
        logger.error("Unexpected error occurred", ex);
        model.addAttribute("errorMessage", "Ha ocurrido un error inesperado. Por favor, contacte al administrador.");
        model.addAttribute("errorDetails", ex.getMessage());
        return "error/generic";
    }

    @ExceptionHandler(RuntimeException.class)
    public String handleRuntimeException(RuntimeException ex, RedirectAttributes redirectAttributes) {
        logger.error("Runtime error: {}", ex.getMessage());
        redirectAttributes.addFlashAttribute("errorMessage", "Error del sistema: " + ex.getMessage());
        return "redirect:/";
    }
}