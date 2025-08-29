package co.edu.puj.regata.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "jugador")
@Data
@NoArgsConstructor
public class Jugador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    @Column(nullable = false, unique = true)
    private String nombre;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RolUsuario rol;  // ADMIN, JUGADOR (aún sin autenticación en esta entrega)
    
    @Column(nullable = false)
    private String contraseña; // Se agrega para futura implementación de autenticación
    
    public enum RolUsuario {
        ADMIN, JUGADOR
    }

    // Getters y setters manuales
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public RolUsuario getRol() {
        return rol;
    }

    public void setRol(RolUsuario rol) {
        this.rol = rol;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }
}