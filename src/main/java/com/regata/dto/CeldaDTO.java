package com.regata.dto;

import com.regata.entities.Celda.TipoCelda;

public class CeldaDTO {
    private Long id;
    private Integer coordX;
    private Integer coordY;
    private TipoCelda tipo;
    
    public CeldaDTO() {}
    
    public CeldaDTO(Long id, Integer coordX, Integer coordY, TipoCelda tipo) {
        this.id = id;
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
}