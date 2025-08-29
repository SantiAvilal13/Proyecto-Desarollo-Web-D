package co.edu.puj.regata.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "mapa")
@Data
@NoArgsConstructor
public class Mapa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    @Column(nullable = false)
    private String nombre;
    
    @Column(nullable = false)
    private int tamFilas;  // Número de filas en el mapa
    
    @Column(nullable = false)
    private int tamColumnas;  // Número de columnas del mapa

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

    public int getTamFilas() {
        return tamFilas;
    }

    public void setTamFilas(int tamFilas) {
        this.tamFilas = tamFilas;
    }

    public int getTamColumnas() {
        return tamColumnas;
    }

    public void setTamColumnas(int tamColumnas) {
        this.tamColumnas = tamColumnas;
    }
}