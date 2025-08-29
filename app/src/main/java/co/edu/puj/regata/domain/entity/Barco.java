package co.edu.puj.regata.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.NoArgsConstructor;
import co.edu.puj.regata.domain.entity.EstadoBarco;

@Entity
@Table(name = "barco")
@Data
@NoArgsConstructor
public class Barco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "id_modelo")
    private Modelo modelo;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "id_jugador")
    private Jugador jugador;
    
    @Column(nullable = false)
    private int velocidadX = 0;  // Velocidad en el eje X (puede ser negativa)
    
    @Column(nullable = false)
    private int velocidadY = 0;  // Velocidad en el eje Y (puede ser negativa)
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoBarco estado = EstadoBarco.VIVO;  // Estado: VIVO, DESTRUIDO, LLEGO
    


    // Getters y setters manuales
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Modelo getModelo() {
        return modelo;
    }

    public void setModelo(Modelo modelo) {
        this.modelo = modelo;
    }

    public Jugador getJugador() {
        return jugador;
    }

    public void setJugador(Jugador jugador) {
        this.jugador = jugador;
    }

    public int getVelocidadX() {
        return velocidadX;
    }

    public void setVelocidadX(int velocidadX) {
        this.velocidadX = velocidadX;
    }

    public int getVelocidadY() {
        return velocidadY;
    }

    public void setVelocidadY(int velocidadY) {
        this.velocidadY = velocidadY;
    }

    public EstadoBarco getEstado() {
        return estado;
    }

    public void setEstado(EstadoBarco estado) {
        this.estado = estado;
    }
}