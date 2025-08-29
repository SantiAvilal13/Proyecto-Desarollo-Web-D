package co.edu.puj.regata.domain.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "celda", uniqueConstraints = @UniqueConstraint(columnNames = {"id_mapa", "coord_x", "coord_y"}))
@Data
@NoArgsConstructor
public class Celda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "id_mapa")
    private Mapa mapa;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoCelda tipo;  // AGUA, PARED, PARTIDA, META
    
    @Column(name = "coord_x", nullable = false)
    private int x;
    
    @Column(name = "coord_y", nullable = false)
    private int y;
    
    public enum TipoCelda {
        AGUA, PARED, PARTIDA, META
    }
    
    // Getters and Setters
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
    
    public TipoCelda getTipo() {
        return tipo;
    }
    
    public void setTipo(TipoCelda tipo) {
        this.tipo = tipo;
    }
    
    public int getX() {
        return x;
    }
    
    public void setX(int x) {
        this.x = x;
    }
    
    public int getY() {
        return y;
    }
    
    public void setY(int y) {
        this.y = y;
    }
}