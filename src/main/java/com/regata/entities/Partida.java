package com.regata.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "partidas")
public class Partida {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "El nombre de la partida es obligatorio")
    @Column(nullable = false, length = 100)
    private String nombre;
    
    @NotNull(message = "El mapa es obligatorio")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mapa_id", nullable = false)
    private Mapa mapa;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoPartida estado = EstadoPartida.ESPERANDO;
    
    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();
    
    @Column(name = "fecha_inicio")
    private LocalDateTime fechaInicio;
    
    @Column(name = "fecha_fin")
    private LocalDateTime fechaFin;
    
    @Column(name = "turno_actual")
    private Integer turnoActual = 0;
    
    @Column(name = "jugador_turno_id")
    private Long jugadorTurnoId;
    
    @Column(name = "max_jugadores")
    private Integer maxJugadores = 4;
    
    @Column(name = "pos_salida_x")
    private Integer posSalidaX = 0;
    
    @Column(name = "pos_salida_y")
    private Integer posSalidaY = 0;
    
    @Column(name = "pos_meta_x")
    private Integer posMetaX = 9;
    
    @Column(name = "pos_meta_y")
    private Integer posMetaY = 9;
    
    @OneToMany(mappedBy = "partida", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ParticipantePartida> participantes;
    
    // Constructores
    public Partida() {}
    
    public Partida(String nombre, Mapa mapa) {
        this.nombre = nombre;
        this.mapa = mapa;
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
    
    public Mapa getMapa() {
        return mapa;
    }
    
    public void setMapa(Mapa mapa) {
        this.mapa = mapa;
    }
    
    public EstadoPartida getEstado() {
        return estado;
    }
    
    public void setEstado(EstadoPartida estado) {
        this.estado = estado;
    }
    
    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }
    
    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    
    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }
    
    public void setFechaInicio(LocalDateTime fechaInicio) {
        this.fechaInicio = fechaInicio;
    }
    
    public LocalDateTime getFechaFin() {
        return fechaFin;
    }
    
    public void setFechaFin(LocalDateTime fechaFin) {
        this.fechaFin = fechaFin;
    }
    
    public Integer getTurnoActual() {
        return turnoActual;
    }
    
    public void setTurnoActual(Integer turnoActual) {
        this.turnoActual = turnoActual;
    }
    
    public Long getJugadorTurnoId() {
        return jugadorTurnoId;
    }
    
    public void setJugadorTurnoId(Long jugadorTurnoId) {
        this.jugadorTurnoId = jugadorTurnoId;
    }
    
    public Integer getMaxJugadores() {
        return maxJugadores;
    }
    
    public void setMaxJugadores(Integer maxJugadores) {
        this.maxJugadores = maxJugadores;
    }
    
    public Integer getPosSalidaX() {
        return posSalidaX;
    }
    
    public void setPosSalidaX(Integer posSalidaX) {
        this.posSalidaX = posSalidaX;
    }
    
    public Integer getPosSalidaY() {
        return posSalidaY;
    }
    
    public void setPosSalidaY(Integer posSalidaY) {
        this.posSalidaY = posSalidaY;
    }
    
    public Integer getPosMetaX() {
        return posMetaX;
    }
    
    public void setPosMetaX(Integer posMetaX) {
        this.posMetaX = posMetaX;
    }
    
    public Integer getPosMetaY() {
        return posMetaY;
    }
    
    public void setPosMetaY(Integer posMetaY) {
        this.posMetaY = posMetaY;
    }
    
    public List<ParticipantePartida> getParticipantes() {
        return participantes;
    }
    
    public void setParticipantes(List<ParticipantePartida> participantes) {
        this.participantes = participantes;
    }
    
    // Métodos de utilidad
    public boolean puedeIniciar() {
        return estado == EstadoPartida.ESPERANDO && participantes != null && participantes.size() >= 2;
    }
    
    public boolean estaEnJuego() {
        return estado == EstadoPartida.EN_JUEGO;
    }
    
    public boolean haTerminado() {
        return estado == EstadoPartida.TERMINADA;
    }
    
    @Override
    public String toString() {
        return "Partida{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", estado=" + estado +
                ", turnoActual=" + turnoActual +
                '}';
    }
}