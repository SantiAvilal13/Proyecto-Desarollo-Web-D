package com.regata.dto;

public class BarcoDTO {
    private Long id;
    private Integer posX;
    private Integer posY;
    private Integer velX;
    private Integer velY;
    private String jugadorNombre;
    private String modeloNombre;
    private String modeloColor;
    
    public BarcoDTO() {}
    
    public BarcoDTO(Long id, Integer posX, Integer posY, Integer velX, Integer velY, 
                   String jugadorNombre, String modeloNombre, String modeloColor) {
        this.id = id;
        this.posX = posX;
        this.posY = posY;
        this.velX = velX;
        this.velY = velY;
        this.jugadorNombre = jugadorNombre;
        this.modeloNombre = modeloNombre;
        this.modeloColor = modeloColor;
    }
    
    // Getters y Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
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
    
    public String getJugadorNombre() {
        return jugadorNombre;
    }
    
    public void setJugadorNombre(String jugadorNombre) {
        this.jugadorNombre = jugadorNombre;
    }
    
    public String getModeloNombre() {
        return modeloNombre;
    }
    
    public void setModeloNombre(String modeloNombre) {
        this.modeloNombre = modeloNombre;
    }
    
    public String getModeloColor() {
        return modeloColor;
    }
    
    public void setModeloColor(String modeloColor) {
        this.modeloColor = modeloColor;
    }
}