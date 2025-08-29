package co.edu.puj.regata.domain.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "partida")
@Data
@NoArgsConstructor
public class Partida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 100)
    private String nombre;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "id_mapa")
    private Mapa mapa;
    
    private java.time.LocalDateTime fechaInicio;
    
    private java.time.LocalDateTime fechaFin;
    
    @Column(name = "duracion_maxima")
    private Integer duracionMaxima; // en minutos
    
    @Column(name = "max_participantes")
    private Integer maxParticipantes;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoPartida estado = EstadoPartida.CREADA;  // CREADA, EN_CURSO, FINALIZADA
    
    @ManyToOne
    @JoinColumn(name = "id_ganador")
    private Jugador ganador;
    
    public enum EstadoPartida {
        CREADA, EN_CURSO, FINALIZADA
    }

    // Getters y setters manuales
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

    public java.time.LocalDateTime getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(java.time.LocalDateTime fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public java.time.LocalDateTime getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(java.time.LocalDateTime fechaFin) {
        this.fechaFin = fechaFin;
    }

    public EstadoPartida getEstado() {
        return estado;
    }

    public void setEstado(EstadoPartida estado) {
        this.estado = estado;
    }

    public Jugador getGanador() {
        return ganador;
    }

    public void setGanador(Jugador ganador) {
        this.ganador = ganador;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public Integer getDuracionMaxima() {
        return duracionMaxima;
    }
    
    public void setDuracionMaxima(Integer duracionMaxima) {
        this.duracionMaxima = duracionMaxima;
    }
    
    public Integer getMaxParticipantes() {
        return maxParticipantes;
    }
    
    public void setMaxParticipantes(Integer maxParticipantes) {
        this.maxParticipantes = maxParticipantes;
    }
}