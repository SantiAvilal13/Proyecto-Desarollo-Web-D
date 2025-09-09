package com.regata.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.List;

public class ValidColorValidator implements ConstraintValidator<ValidColor, String> {
    
    // Colores prohibidos del mapa
    private static final List<String> FORBIDDEN_COLORS = Arrays.asList(
        "#28a745", // Verde - color de inicio/partida
        "#dc3545", // Rojo - color de meta/fin
        "#6c757d"  // Gris - color de obstáculos/pared
    );
    
    @Override
    public void initialize(ValidColor constraintAnnotation) {
        // No necesita inicialización especial
    }
    
    @Override
    public boolean isValid(String color, ConstraintValidatorContext context) {
        if (color == null || color.trim().isEmpty()) {
            return false; // El color es requerido
        }
        
        // Normalizar el color a minúsculas para comparación
        String normalizedColor = color.toLowerCase().trim();
        
        // Verificar si el color está en la lista de colores prohibidos
        return FORBIDDEN_COLORS.stream()
                .noneMatch(forbiddenColor -> forbiddenColor.toLowerCase().equals(normalizedColor));
    }
}