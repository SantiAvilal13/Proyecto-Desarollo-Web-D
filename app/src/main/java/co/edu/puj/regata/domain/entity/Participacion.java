package co.edu.puj.regata.domain.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "participacion", uniqueConstraints = @UniqueConstraint(columnNames = {"id_partida", "id_barco"}))
@Data
@NoArgsConstructor
public class Participacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "id_partida")
    private Partida partida;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "id_barco")
    private Barco barco;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "id_jugador")
    private Jugador jugador;
    
    @Column(nullable = false)
    private int posX;  // Posición en X
    
    @Column(nullable = false)
    private int posY;  // Posición en Y
    
    @Column(nullable = false)
    private boolean eliminado = false;  // Si el barco ha sido eliminado

    // Getters y setters manuales para posX y posY
    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    // Getters y setters adicionales
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

    public Barco getBarco() {
        return barco;
    }

    public void setBarco(Barco barco) {
        this.barco = barco;
    }

    public Jugador getJugador() {
        return jugador;
    }

    public void setJugador(Jugador jugador) {
        this.jugador = jugador;
    }

    public boolean isEliminado() {
        return eliminado;
    }

    public void setEliminado(boolean eliminado) {
        this.eliminado = eliminado;
    }
}