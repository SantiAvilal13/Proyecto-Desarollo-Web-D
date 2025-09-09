package com.regata.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;

@Entity
@Table(name = "mapas")
public class Mapa {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "El nombre es requerido")
    @Column(nullable = false)
    private String nombre;
    
    @Min(value = 1, message = "El tamaño de filas debe ser mayor a 0")
    @Column(nullable = false)
    private Integer tamFilas;
    
    @Min(value = 1, message = "El tamaño de columnas debe ser mayor a 0")
    @Column(nullable = false)
    private Integer tamColumnas;
    
    @OneToMany(mappedBy = "mapa", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Celda> celdas;
    
    // Constructores
    public Mapa() {}
    
    public Mapa(String nombre, Integer tamFilas, Integer tamColumnas) {
        this.nombre = nombre;
        this.tamFilas = tamFilas;
        this.tamColumnas = tamColumnas;
    }
    
    // Getters y Setters
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
    
    public Integer getTamFilas() {
        return tamFilas;
    }
    
    public void setTamFilas(Integer tamFilas) {
        this.tamFilas = tamFilas;
    }
    
    public Integer getTamColumnas() {
        return tamColumnas;
    }
    
    public void setTamColumnas(Integer tamColumnas) {
        this.tamColumnas = tamColumnas;
    }
    
    public List<Celda> getCeldas() {
        return celdas;
    }
    
    public void setCeldas(List<Celda> celdas) {
        this.celdas = celdas;
    }
    
    @Override
    public String toString() {
        return "Mapa{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", tamFilas=" + tamFilas +
                ", tamColumnas=" + tamColumnas +
                '}';
    }
}