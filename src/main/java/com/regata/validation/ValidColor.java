package com.regata.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ValidColorValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidColor {
    String message() default "El color seleccionado no está permitido. No se pueden usar los colores del mapa (verde, rojo, gris).";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}