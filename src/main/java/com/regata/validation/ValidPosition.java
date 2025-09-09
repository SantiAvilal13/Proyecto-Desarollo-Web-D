package com.regata.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

/**
 * Anotación de validación personalizada para validar posiciones en el mapa.
 * Verifica que las coordenadas estén dentro del rango válido del mapa.
 */
@Documented
@Constraint(validatedBy = PositionValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPosition {
    String message() default "La posición debe estar dentro del rango válido del mapa (0-99)";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}