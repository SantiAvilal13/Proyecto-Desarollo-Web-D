package com.regata.entities;

import com.regata.validation.ValidPosition;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "barcos")
@ValidPosition
public class Barco {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "El jugador es obligatorio")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "jugador_id", nullable = false)
    @JsonIgnore
    private Jugador jugador;
    
    @NotNull(message = "El modelo es obligatorio")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "modelo_id", nullable = false)
    @JsonIgnore
    private Modelo modelo;
    
    @Min(value = 0, message = "La posición X debe ser mayor o igual a 0")
    @Column(nullable = false)
    private Integer posX;
    
    @Min(value = 0, message = "La posición Y debe ser mayor o igual a 0")
    @Column(nullable = false)
    private Integer posY;
    
    @Column(nullable = false)
    private Integer velX;
    
    @Column(nullable = false)
    private Integer velY;
    
    // Constructores
    public Barco() {}
    
    public Barco(Jugador jugador, Modelo modelo, Integer posX, Integer posY, Integer velX, Integer velY) {
        this.jugador = jugador;
        this.modelo = modelo;
        this.posX = posX;
        this.posY = posY;
        this.velX = velX;
        this.velY = velY;
    }
    
    // Getters y Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Jugador getJugador() {
        return jugador;
    }
    
    public void setJugador(Jugador jugador) {
        this.jugador = jugador;
    }
    
    public Modelo getModelo() {
        return modelo;
    }
    
    public void setModelo(Modelo modelo) {
        this.modelo = modelo;
    }
    
    public Integer getPosX() {
        return posX;
    }
    
    public void setPosX(Integer posX) {
        this.posX = posX;
    }
    
    public Integer getPosY() {
        return posY;
    }
    
    public void setPosY(Integer posY) {
        this.posY = posY;
    }
    
    public Integer getVelX() {
        return velX;
    }
    
    public void setVelX(Integer velX) {
        this.velX = velX;
    }
    
    public Integer getVelY() {
        return velY;
    }
    
    public void setVelY(Integer velY) {
        this.velY = velY;
    }
    
    @Override
    public String toString() {
        return "Barco{" +
                "id=" + id +
                ", posX=" + posX +
                ", posY=" + posY +
                ", velX=" + velX +
                ", velY=" + velY +
                '}';
    }
}