package co.edu.puj.regata.domain.entity;

/**
 * Enum que representa los posibles estados de un barco durante una partida.
 */
public enum EstadoBarco {
    /**
     * El barco está activo y puede moverse
     */
    VIVO,
    
    /**
     * El barco ha sido destruido (por colisión con pared u otro obstáculo)
     */
    DESTRUIDO,
    
    /**
     * El barco ha llegado a la meta
     */
    LLEGO
}