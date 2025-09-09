package com.regata.validation;

import com.regata.entities.Barco;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validador para la anotación @ValidPosition.
 * Verifica que las coordenadas del barco estén dentro del rango válido del mapa.
 */
public class PositionValidator implements ConstraintValidator<ValidPosition, Barco> {

    private static final int MIN_COORDINATE = 0;
    private static final int MAX_COORDINATE = 99;

    @Override
    public void initialize(ValidPosition constraintAnnotation) {
        // No se requiere inicialización especial
    }

    @Override
    public boolean isValid(Barco barco, ConstraintValidatorContext context) {
        if (barco == null) {
            return true; // Dejar que @NotNull maneje los valores nulos
        }

        boolean isValidX = isValidCoordinate(barco.getPosX());
        boolean isValidY = isValidCoordinate(barco.getPosY());

        if (!isValidX || !isValidY) {
            context.disableDefaultConstraintViolation();
            
            if (!isValidX && !isValidY) {
                context.buildConstraintViolationWithTemplate(
                    "Las coordenadas X e Y deben estar entre " + MIN_COORDINATE + " y " + MAX_COORDINATE
                ).addConstraintViolation();
            } else if (!isValidX) {
                context.buildConstraintViolationWithTemplate(
                    "La coordenada X debe estar entre " + MIN_COORDINATE + " y " + MAX_COORDINATE
                ).addConstraintViolation();
            } else {
                context.buildConstraintViolationWithTemplate(
                    "La coordenada Y debe estar entre " + MIN_COORDINATE + " y " + MAX_COORDINATE
                ).addConstraintViolation();
            }
            
            return false;
        }

        return true;
    }

    private boolean isValidCoordinate(Integer coordinate) {
        return coordinate != null && 
               coordinate >= MIN_COORDINATE && 
               coordinate <= MAX_COORDINATE;
    }
}