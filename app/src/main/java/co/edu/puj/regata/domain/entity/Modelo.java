package co.edu.puj.regata.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "modelo")
@Data
@NoArgsConstructor
public class Modelo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    @Column(nullable = false, unique = true)
    private String nombre;
    
    @NotBlank
    @Column(nullable = false, length = 7)
    private String color; // Color del modelo de barco en formato hexadecimal (#RRGGBB)

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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}