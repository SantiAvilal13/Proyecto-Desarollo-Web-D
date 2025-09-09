package com.regata.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "participantes_partida")
public class ParticipantePartida {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "La partida es obligatoria")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partida_id", nullable = false)
    private Partida partida;
    
    @NotNull(message = "El jugador es obligatorio")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "jugador_id", nullable = false)
    private Jugador jugador;
    
    @NotNull(message = "El barco es obligatorio")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "barco_id", nullable = false)
    private Barco barco;
    
    @Column(name = "orden_turno", nullable = false)
    private Integer ordenTurno;
    
    @Column(name = "pos_actual_x", nullable = false)
    private Integer posActualX = 0;
    
    @Column(name = "pos_actual_y", nullable = false)
    private Integer posActualY = 0;
    
    @Column(name = "ha_llegado_meta")
    private Boolean haLlegadoMeta = false;
    
    @Column(name = "posicion_final")
    private Integer posicionFinal;
    
    @Column(name = "movimientos_realizados")
    private Integer movimientosRealizados = 0;
    
    // Constructores
    public ParticipantePartida() {}
    
    public ParticipantePartida(Partida partida, Jugador jugador, Barco barco, Integer ordenTurno) {
        this.partida = partida;
        this.jugador = jugador;
        this.barco = barco;
        this.ordenTurno = ordenTurno;
        // Inicializar en posición de salida
        this.posActualX = partida.getPosSalidaX();
        this.posActualY = partida.getPosSalidaY();
    }
    
    // Getters y Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Partida getPartida() {
        return partida;
    }
    
    public void setPartida(Partida partida) {
        this.partida = partida;
    }
    
    public Jugador getJugador() {
        return jugador;
    }
    
    public void setJugador(Jugador jugador) {
        this.jugador = jugador;
    }
    
    public Barco getBarco() {
        return barco;
    }
    
    public void setBarco(Barco barco) {
        this.barco = barco;
    }
    
    public Integer getOrdenTurno() {
        return ordenTurno;
    }
    
    public void setOrdenTurno(Integer ordenTurno) {
        this.ordenTurno = ordenTurno;
    }
    
    public Integer getPosActualX() {
        return posActualX;
    }
    
    public void setPosActualX(Integer posActualX) {
        this.posActualX = posActualX;
    }
    
    public Integer getPosActualY() {
        return posActualY;
    }
    
    public void setPosActualY(Integer posActualY) {
        this.posActualY = posActualY;
    }
    
    public Boolean getHaLlegadoMeta() {
        return haLlegadoMeta;
    }
    
    public void setHaLlegadoMeta(Boolean haLlegadoMeta) {
        this.haLlegadoMeta = haLlegadoMeta;
    }
    
    public Integer getPosicionFinal() {
        return posicionFinal;
    }
    
    public void setPosicionFinal(Integer posicionFinal) {
        this.posicionFinal = posicionFinal;
    }
    
    public Integer getMovimientosRealizados() {
        return movimientosRealizados;
    }
    
    public void setMovimientosRealizados(Integer movimientosRealizados) {
        this.movimientosRealizados = movimientosRealizados;
    }
    
    // Métodos de utilidad
    public void moverA(Integer x, Integer y) {
        this.posActualX = x;
        this.posActualY = y;
        this.movimientosRealizados++;
        
        // Verificar si llegó a la meta
        if (x.equals(partida.getPosMetaX()) && y.equals(partida.getPosMetaY())) {
            this.haLlegadoMeta = true;
        }
    }
    
    public boolean esSuTurno() {
        return partida.getJugadorTurnoId() != null && 
               partida.getJugadorTurnoId().equals(jugador.getId());
    }
    
    public boolean estaEnPosicionInicial() {
        return posActualX.equals(partida.getPosSalidaX()) && 
               posActualY.equals(partida.getPosSalidaY());
    }
    
    @Override
    public String toString() {
        return "ParticipantePartida{" +
                "id=" + id +
                ", jugador=" + (jugador != null ? jugador.getNombre() : "null") +
                ", ordenTurno=" + ordenTurno +
                ", posActual=(" + posActualX + "," + posActualY + ")" +
                ", haLlegadoMeta=" + haLlegadoMeta +
                '}';
    }
}