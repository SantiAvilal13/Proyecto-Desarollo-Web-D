package com.regata.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "celdas")
public class Celda {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mapa_id", nullable = false)
    @JsonIgnore
    private Mapa mapa;
    
    @Min(value = 0, message = "La coordenada X debe ser mayor o igual a 0")
    @Column(nullable = false)
    private Integer coordX;
    
    @Min(value = 0, message = "La coordenada Y debe ser mayor o igual a 0")
    @Column(nullable = false)
    private Integer coordY;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoCelda tipo;
    
    // Enum para los tipos de celda
    public enum TipoCelda {
        AGUA, PARED, PARTIDA, META
    }
    
    // Constructores
    public Celda() {}
    
    public Celda(Mapa mapa, Integer coordX, Integer coordY, TipoCelda tipo) {
        this.mapa = mapa;
        this.coordX = coordX;
        this.coordY = coordY;
        this.tipo = tipo;
    }
    
    // Getters y Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Mapa getMapa() {
        return mapa;
    }
    
    public void setMapa(Mapa mapa) {
        this.mapa = mapa;
    }
    
    public Integer getCoordX() {
        return coordX;
    }
    
    public void setCoordX(Integer coordX) {
        this.coordX = coordX;
    }
    
    public Integer getCoordY() {
        return coordY;
    }
    
    public void setCoordY(Integer coordY) {
        this.coordY = coordY;
    }
    
    public TipoCelda getTipo() {
        return tipo;
    }
    
    public void setTipo(TipoCelda tipo) {
        this.tipo = tipo;
    }
    
    @Override
    public String toString() {
        return "Celda{" +
                "id=" + id +
                ", coordX=" + coordX +
                ", coordY=" + coordY +
                ", tipo=" + tipo +
                '}';
    }
}